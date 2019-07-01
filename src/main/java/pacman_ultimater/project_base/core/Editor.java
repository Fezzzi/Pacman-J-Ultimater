package pacman_ultimater.project_base.core;

import pacman_ultimater.project_base.custom_utils.IntPair;
import pacman_ultimater.project_base.custom_utils.Quintet;
import pacman_ultimater.project_base.gui_swing.model.GameModel;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Class handling map creating and editing.
 */
public class Editor
{
    //<editor-fold desc="- VARIABLES Block -">

    private enum simpleTiles {WALL, PELLET, POWERPELLET, GATE, FREE};
    private enum toolsEnum {WALL, PELLET, POWER, GATE, COLOR, EDIT, PREVIEW, SAVE, HELP};
    private GameModel vars;

    private Integer tileSize, pelletSize, powerSize, paddingSize;
    private Graphics g;
    private Color newColor;
    private simpleTiles[][] newTiles;
    private JLabel editor;
    private JLabel[] tools = {
        new JLabel("Wall"),
        new JLabel("Pellet"),
        new JLabel("Power"),
        new JLabel("Gate"),
        new JLabel("Color"),
        new JLabel("Edit"),
        new JLabel("View"),
        new JLabel("Save"),
        new JLabel("Help"),
    };
    private JPanel mainPanel;
    private IntPair currentTile;
    private simpleTiles currentTool = simpleTiles.WALL;
    private boolean edit;
    private boolean fromFile;
    private boolean randomColor;

    //</editor-fold>

    //<editor-fold desc="- CONSTRUCTION Block -">

    public Editor(JPanel mainPanel, GameModel vars)
    {
        this.mainPanel = mainPanel;
        this.vars = vars;
        newColor = new Color(0,0,255);
        newTiles = new simpleTiles[LoadMap.MAPWIDTHINTILES][LoadMap.MAPHEIGHTINTILES];
        fromFile = false;
    }

    public Editor(JPanel mainPanel, GameModel vars, Quintet<Tile[][], Integer, IntPair, Color, ArrayList<Point>> map)
    {
        this.mainPanel = mainPanel;
        this.vars = vars;
        if (map.item4 != LoadMap.TRANSPARENT) {
            newColor = map.item4;
            randomColor = false;
        } else {
            newColor = LoadMap.chooseRandomColor();
            randomColor = true;
        }
        newTiles = convertToSimpleTiles(map.item1);
        fromFile = true;
    }

    /**
     * Renders tiles in case of preloaded map
     */
    private void renderTiles()
    {
        if (fromFile) {
            for (int i = 0; i < LoadMap.MAPHEIGHTINTILES; ++i) {
                for (int j = 0; j < LoadMap.MAPWIDTHINTILES; ++j) {
                    currentTile = new IntPair(j, i);
                    repaintCanvas(newTiles[j][i]);
                }
            }
        }
    }

    /**
     * Renders editor's tooltip
     */
    private void renderTools()
    {
        int y = 25;
        int toolId = 0;
        for (JLabel tool: tools) {
            vars.addedComponents.add(tool);
            tool.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, (int)(18 * Math.min(vars.vMult, vars.hMult))));
            tool.setForeground(Color.white);
            tool.setLocation(paddingSize + 25 + LoadMap.MAPWIDTHINTILES * tileSize, y);
            tool.setName(tool.getText() + "Tool");
            tool.setSize(100, 50);
            tool.addMouseListener(new toolListener(toolsEnum.values()[toolId], tool));
            mainPanel.add(tool);
            y += 35 * vars.vMult;
            toolId++;
        }
    }

    /**
     * Renders editor grid
     */
    private void renderGrid()
    {
        vars.addedComponents = new ArrayList<JLabel>();
        float minMult = Math.min(vars.vMult, vars.hMult);
        tileSize = (int)(12 * minMult);
        pelletSize = (int)(2 * minMult);
        powerSize = tileSize - 2;
        // horizontal shift for default size - enlargement
        paddingSize = (int)(
                ((25 * vars.hMult) + (((LoadMap.MAPWIDTHINTILES * 12) + 90) * (vars.hMult - 1)) / 2)
                - (tileSize - 12) * (LoadMap.MAPWIDTHINTILES) / 2);

        Image bufferImage = mainPanel.createImage(
                LoadMap.MAPWIDTHINTILES * tileSize + 4, LoadMap.MAPHEIGHTINTILES * tileSize + 4);
        g = bufferImage.getGraphics();
        Graphics2D bg2D = (Graphics2D)g;
        bg2D.setStroke(new BasicStroke(1));
        g.setColor(Color.BLACK);
        g.fillRect(0,0,
                LoadMap.MAPWIDTHINTILES * tileSize + 4, LoadMap.MAPHEIGHTINTILES * tileSize + 4);

        g.setColor(new Color(255, 255, 0));
        g.fillRect(0, 0, 1, LoadMap.MAPHEIGHTINTILES * tileSize + 4);
        g.fillRect(LoadMap.MAPWIDTHINTILES * tileSize + 3, 0, 1, LoadMap.MAPHEIGHTINTILES * tileSize + 4);
        g.fillRect(0, 0, LoadMap.MAPWIDTHINTILES * tileSize + 4, 1);
        g.fillRect(0, LoadMap.MAPHEIGHTINTILES * tileSize + 3, LoadMap.MAPWIDTHINTILES * tileSize + 4, 1);

        g.setColor(new Color(77, 77, 77));
        for (int x = 1; x < LoadMap.MAPWIDTHINTILES; ++x) {
            g.fillRect(x*tileSize + 2, 2, 1, LoadMap.MAPHEIGHTINTILES * tileSize);
        }
        for (int y = 1; y < LoadMap.MAPHEIGHTINTILES; ++y) {
            g.fillRect(2, y*tileSize + 2, LoadMap.MAPWIDTHINTILES * tileSize, 1);
        }

        editor = new JLabel();
        editor.setName("canvas");
        editor.setLocation(paddingSize, 25);
        editor.setSize(LoadMap.MAPWIDTHINTILES * tileSize + 4,LoadMap.MAPHEIGHTINTILES * tileSize + 4);
        editor.setIcon(new ImageIcon(bufferImage));
        vars.addedComponents.add(editor);

        canvasListener cl = new canvasListener();
        editor.addMouseMotionListener(cl);
        editor.addMouseListener(cl);

        mainPanel.add(editor);
    }

    //</editor-fold>

    /**
     * Shows map editor with loaded/new map.
     */
    public void show()
    {
        edit = true;
        renderGrid();
        renderTiles();
        renderTools();
        currentTile = new IntPair(13, 23);
        tools[toolsEnum.EDIT.ordinal()].setForeground(new Color(255,255,0));
        changeTool(toolsEnum.WALL);
        fromFile = true;

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Closes map editor.
     */
    public boolean close()
    {
        UIManager.put("Panel.background", Color.black);
        if (JOptionPane.showConfirmDialog(
                mainPanel, "Unsaved changes will be lost, procceed?",
                "Pac-Man J-Ultimater: Confirm exit", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.YES_OPTION) {
            UIManager.put("Panel.background", Color.white);
            mainPanel.remove(editor);
            for (JLabel tool: tools) {
                mainPanel.remove(tool);
            }
            vars.addedComponents = null;
            return true;
        }
        UIManager.put("Panel.background", Color.white);

        return false;
    }

    //<editor-fold desc="- GRAPHICS Block -">

    /**
     * Changes tool by highlighting new one ane dehighlighting the old one.
     *
     * @param tool new active tool
     */
    private void changeTool(toolsEnum tool)
    {
        tools[currentTool.ordinal()].setForeground(new Color(255,255,255));
        tools[tool.ordinal()].setForeground(new Color(255,255,0));
        currentTool = simpleTiles.values()[tool.ordinal()];
    }

    /**
     * Highlights current active tile and dehighlights old one
     *
     * @param x coord of new Tile
     * @param y coord of new Tile
     */
    private void highlightCurrentTile(int x, int y)
    {
        g.setColor(new Color(77, 77, 77));
        g.drawRect(currentTile.item1 * tileSize + 2, currentTile.item2 * tileSize + 2, tileSize, tileSize);
        g.setColor(new Color(255, 255, 0));
        g.drawRect(x * tileSize + 2, y * tileSize + 2, tileSize, tileSize);
        currentTile = new IntPair(x, y);
        mainPanel.repaint();
    }

    /**
     * Apply given tool to current active tile
     *
     * @param tool simpleTiles
     */
    private void repaintCanvas(simpleTiles tool)
    {
        int x = (currentTile.item1 * tileSize) + 3;
        int y = (currentTile.item2 * tileSize) + 3;
        if (tool == null) {
            g.setColor(new Color(0, 0, 0));
            g.fillRect(x, y, tileSize - 1, tileSize - 1);
        } else {
            switch (tool) {
                case WALL:
                    g.setColor(newColor);
                    g.fillRect(x, y, tileSize - 1, tileSize - 1);
                    break;
                case PELLET:
                    g.setColor(new Color(246, 172, 152));
                    g.fillRect(x + ((tileSize - pelletSize) / 2), y + ((tileSize - pelletSize) / 2), pelletSize, pelletSize);
                    break;
                case POWERPELLET:
                    g.setColor(new Color(246, 172, 152));
                    g.fillOval(x + ((tileSize - powerSize) / 2), y + ((tileSize - powerSize) / 2), powerSize, powerSize);
                    break;
                case GATE:
                    g.setColor(new Color(245, 187, 229));
                    g.fillRect(x, y + ((tileSize - pelletSize) / 2), tileSize - 1, pelletSize);
                    break;
                case FREE:
                    g.setColor(new Color(0, 0, 0));
                    g.fillRect(x, y, tileSize - 1, tileSize - 1);
                    break;
            }
        }
    }

    /**
     * Applies current active tool on current tile
     */
    private void applyTool()
    {
        if (edit) {
            repaintCanvas(simpleTiles.FREE);
            repaintCanvas(currentTool);
            newTiles[currentTile.item1][currentTile.item2] = currentTool;

            mainPanel.repaint();
        }
    }

    //</editor-fold>

    //<editor-fold desc="- UTILS Block -">

    /**
     * Editor's own key handlers.
     *
     * @param keyCode int
     */
    public void handleKey(int keyCode)
    {
        switch (keyCode){
            case KeyEvent.VK_C:
                colorClick();
                break;
            case KeyEvent.VK_E:
                editClick();
                break;
            case KeyEvent.VK_V:
                previewClick();
                break;
            case KeyEvent.VK_H:
                helpClick();
                break;
            case KeyEvent.VK_S:
                saveClick();
                break;
            case KeyEvent.VK_W:
                changeTool(toolsEnum.WALL);
                break;
            case KeyEvent.VK_D:
                changeTool(toolsEnum.PELLET);
                break;
            case KeyEvent.VK_F:
                changeTool(toolsEnum.POWER);
                break;
            case KeyEvent.VK_G:
                changeTool(toolsEnum.GATE);
                break;
            case KeyEvent.VK_LEFT:
                if (currentTile.item1 > 0) {
                    highlightCurrentTile(currentTile.item1 - 1, currentTile.item2);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (currentTile.item1 < LoadMap.MAPWIDTHINTILES - 1) {
                    highlightCurrentTile(currentTile.item1 + 1, currentTile.item2);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (currentTile.item2 < LoadMap.MAPHEIGHTINTILES - 1) {
                    highlightCurrentTile(currentTile.item1, currentTile.item2 + 1);
                }
                break;
            case KeyEvent.VK_UP:
                if (currentTile.item2 > 0) {
                    highlightCurrentTile(currentTile.item1, currentTile.item2 - 1);
                }
                break;
            case KeyEvent.VK_ENTER:
                applyTool();
                break;
            case KeyEvent.VK_BACK_SPACE:
                if (edit) {
                    repaintCanvas(simpleTiles.FREE);
                    newTiles[currentTile.item1][currentTile.item2] = simpleTiles.FREE;
                    mainPanel.repaint();
                }
                break;
        }
    }

    /**
     * Performs tile map to String serialization
     *
     * @return String
     */
    private String serializeMap()
    {
        String map = "";
        if (!randomColor) {
            map += String.format("#%02X%02X%02X", newColor.getRed(), newColor.getGreen(), newColor.getBlue()) + "\n";
        }
        map += convertToCharactes();
        return map;
    }

    /**
     * Converts current new tile map of simple tiles into string representation
     *
     * @return String
     */
    private String convertToCharactes()
    {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < LoadMap.MAPHEIGHTINTILES; ++i) {
            for (int j = 0; j < LoadMap.MAPWIDTHINTILES; ++j) {
                if (newTiles[j][i] == null) {
                    out.append(" ");
                } else {
                    switch (newTiles[j][i]) {
                        case WALL:
                            out.append("x");
                            break;
                        case GATE:
                            out.append("X");
                            break;
                        case PELLET:
                            out.append(".");
                            break;
                        case POWERPELLET:
                            out.append("o");
                            break;
                        default:
                            out.append(" ");
                            break;
                    }
                }
            }
            if (i != LoadMap.MAPHEIGHTINTILES - 1){
                out.append("\n");
            }
        }

        return out.toString();
    }

    /**
     * Convert tile map from Tile to simpleTile
     *
     * @param tileMap original tile map with Tiles
     * @return simplified tile map
     */
    private simpleTiles[][] convertToSimpleTiles(Tile[][] tileMap)
    {
        simpleTiles[][] newTileMap = new simpleTiles[LoadMap.MAPWIDTHINTILES][LoadMap.MAPHEIGHTINTILES];
        for (int i = 0; i < LoadMap.MAPHEIGHTINTILES; ++i) {
            for (int j = 0; j < LoadMap.MAPWIDTHINTILES; ++j) {
                switch (tileMap[i][j].tile) {
                    case GATE:
                        newTileMap[j][i] = simpleTiles.GATE;
                        break;
                    case FREE:
                        newTileMap[j][i] = simpleTiles.FREE;
                        break;
                    case POWERDOT:
                        newTileMap[j][i] = simpleTiles.POWERPELLET;
                        break;
                    case DOT:
                        newTileMap[j][i] = simpleTiles.PELLET;
                        break;
                    default:
                        newTileMap[j][i] = simpleTiles.WALL;
                        break;
                }
            }
        }

        return newTileMap;
    }

    //</editor-fold>

    //<editor-fold desc="- TOOLS_CLICK Block -"

    /**
     * Handles edit tool selection
     */
    private void editClick()
    {
        tools[toolsEnum.EDIT.ordinal() + 1].setForeground(new Color(255, 255, 255));
        tools[toolsEnum.EDIT.ordinal()].setForeground(new Color(255, 255, 0));
        edit = true;
    }

    /**
     * Handles color tool selection
     */
    private void colorClick()
    {
        Color color = JColorChooser.showDialog(mainPanel,"Choose new map color", newColor);
        if (color != null && color != newColor) {
            newColor = color;
            renderTiles();
            mainPanel.repaint();
        }
    }

    /**
     * Handles view tool selection
     */
    private void previewClick()
    {
        InputStream mapStream = new ByteArrayInputStream(serializeMap().getBytes());
        LoadMap test = new LoadMap(mapStream);
        UIManager.put("Panel.background", Color.black);
        if (test.Map != null) {
            tools[toolsEnum.PREVIEW.ordinal() - 1].setForeground(new Color(255, 255, 255));
            tools[toolsEnum.PREVIEW.ordinal()].setForeground(new Color(255, 255, 0));
            edit = false;

            IntPair size = new IntPair(
                    LoadMap.MAPWIDTHINTILES * LoadMap.TILESIZEINPXS,
                    LoadMap.MAPHEIGHTINTILES * LoadMap.TILESIZEINPXS);
            JPanel previewPanel = new JPanel();
            JLabel preview = new JLabel();
            preview.setSize(size.item1, size.item2);
            Image bufferImage = mainPanel.createImage(size.item1, size.item2);
            Graphics pg = bufferImage.getGraphics();;
            pg.setColor(Color.BLACK);
            pg.fillRect(0,0, size.item1, size.item2);

            for (int i = 0; i < LoadMap.MAPHEIGHTINTILES; i++)
                for (int j = 0; j < LoadMap.MAPWIDTHINTILES; j++)
                    test.Map.item1[i][j].DrawTile(pg, new Point(j * LoadMap.TILESIZEINPXS, i * LoadMap.TILESIZEINPXS), newColor);

            preview.setIcon(new ImageIcon(bufferImage));
            previewPanel.add(preview);
            JOptionPane.showMessageDialog(mainPanel, previewPanel, "Pac-Man J-Ultimater: Map Preview", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mainPanel, "This map is invalid!", "Pac-Man J-Ultimater: Map Preview", JOptionPane.INFORMATION_MESSAGE);
        }
        UIManager.put("Panel.background", Color.white);
        editClick();
    }

    /**
     * Handles save tool selection
     */
    private void saveClick()
    {
        tools[toolsEnum.SAVE.ordinal()].setForeground(new Color(255, 255, 0));
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Pac-Man J-Ultimater: Save map");
        if (chooser.showSaveDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
            UIManager.put("Panel.background", Color.black);
            try(FileWriter fw = new FileWriter(chooser.getSelectedFile()+".txt")) {
                fw.write(serializeMap());
                JOptionPane.showMessageDialog(mainPanel, "File was saved!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainPanel, "Error occured, map was not saved!");
            }
            UIManager.put("Panel.background", Color.white);
        }
    }

    /**
     * Handles help tool selection
     */
    private void helpClick()
    {
        tools[toolsEnum.HELP.ordinal()].setForeground(new Color(255, 255, 0));
        UIManager.put("Panel.background", Color.black);
        JOptionPane.showMessageDialog(mainPanel,
                "<html>" +
                            "<div style='background-color: black; color: white'>" +
                                "<h2>SHORTCUTS</h2>" +
                                "<ul>" +
                                    "<li>Wall, shortcut [w]</li>" +
                                    "<li>Pellet, shortcut [d]</li>" +
                                    "<li>Power Pellet, shortcut [f]</li>" +
                                    "<li>Gate, shortcut [g]</li>" +
                                    "<li>Color, shortcut [c]</li>" +
                                    "<li>Edit, shortcut [e]</li>" +
                                    "<li>View, shortcut [v]</li>" +
                                    "<li>Save, shortcut [s]</li>" +
                                    "<li>Help, shortcut [h]</li>" +
                                    "<li>------------------</li>" +
                                    "<li>Apply tool, shortcut [enter]/[left mouse button]</li>" +
                                    "<li>Clear tile, shortcut [backspace]/[right mouse button]</li>" +
                                "</ul>" +
                            "</div><div style='background-color: black; color: white'>" +
                                "<h2>RULES</h2>" +
                                "<ul>" +
                                    "<li>Pacman's initial position is between 24,13 and 24,15.</li>" +
                                    "<li>Blinky's initial position is above the GhostHouse.</li>" +
                                    "<li>Blinky's initial position must be accessible from pacman's initial position.</li>" +
                                    "<li>Every pellet must be accessible from pacman's initial position.</li>" +
                                    "<li>Pacman's initial position is to be kept free.</li>" +
                                    "<li>Every map must contain a GhostHouse.</li>" +
                                "</ul>" +
                            "</div><div style='background-color: black; color: white'>" +
                                "<h2>GHOSTHOUSE</h2>" +
                                "<ul>" +
                                    "<li>GhostHouse is a 8 tiles wide, 5 tiles high strcutre.</li>" +
                                    "<li>The first line contains 2 GATES surrounded by 3 WALLS from both sides.</li>" +
                                    "<li>Following 3 lines contains 6 FREE tiles with 1 WALL from each side.</li>" +
                                    "<li>The last line consits of 8 consecutive WALLS.</li>" +
                                "</ul>" +
                            "</div>" +
                        "<html>",
                "Pac-Man J-Ultimater: Map Preview", JOptionPane.PLAIN_MESSAGE);
        UIManager.put("Panel.background", Color.white);
    }

    //</editor-fold>

    //<editor-fold desc="- LISTENERS Block -"

    /**
     * Editor canvas mouse listener
     */
    private class canvasListener implements MouseMotionListener, MouseInputListener
    {
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}

        /**
         * @inheritDoc
         */
        public void mouseClicked(MouseEvent e)
        {
            if (SwingUtilities.isLeftMouseButton(e)) {
                applyTool();
            } else if (SwingUtilities.isRightMouseButton(e)) {
                if (edit) {
                    repaintCanvas(simpleTiles.FREE);
                    newTiles[currentTile.item1][currentTile.item2] = simpleTiles.FREE;
                    mainPanel.repaint();
                }
            }
        }

        /**
         * @inheritDoc
         */
        public void mouseDragged(MouseEvent e)
        {
            mouseMoved(e);
            mouseClicked(e);
        }

        /**
         * @inheritDoc
         */
        public void mouseMoved(MouseEvent e)
        {
            int x = e.getX() / tileSize;
            int y = e.getY() / tileSize;
            if ((x != currentTile.item1 || y != currentTile.item2)
            && (x < LoadMap.MAPWIDTHINTILES && x >= 0 && y < LoadMap.MAPHEIGHTINTILES) && y >= 0) {
                highlightCurrentTile(x, y);
            }
        }
    }

    /**
     * Tools tooltip mouse listener
     */
    private class toolListener extends MouseAdapter
    {
        private toolsEnum tool;
        private JLabel toolLbl;

        toolListener(toolsEnum tool, JLabel toolLbl)
        {
            this.tool = tool;
            this.toolLbl = toolLbl;
        }

        /**
         * @inheritDoc
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (tool.ordinal() < 4) {
                changeTool(tool);
            } else {
                switch (tool) {
                    case COLOR:
                        colorClick();
                        break;
                    case EDIT:
                        editClick();
                        break;
                    case PREVIEW:
                        previewClick();
                        break;
                    case SAVE:
                        saveClick();
                        break;
                    case HELP:
                        helpClick();
                        break;
                }
            }
        }

        /**
         * @inheritDoc
         */
        @Override
        public void mouseEntered(MouseEvent e)
        {
            if (tool.ordinal() != currentTool.ordinal()
            && ((tool != toolsEnum.EDIT && edit) || (tool != toolsEnum.PREVIEW && !edit))) {
                toolLbl.setForeground(new Color(255, 254, 137));
            }
        }

        /**
         * @inheritDoc
         */
        @Override
        public void mouseExited(MouseEvent e)
        {
            if (tool.ordinal() != currentTool.ordinal()
            && ((tool != toolsEnum.EDIT && edit) || (tool != toolsEnum.PREVIEW && !edit))) {
                toolLbl.setForeground(new Color(255, 255, 255));
            }
        }
    }

    //</editor-fold>
}
