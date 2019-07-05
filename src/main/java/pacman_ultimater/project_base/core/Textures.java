package pacman_ultimater.project_base.core;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Class simulating textures loading by drawing predefined curves
 */
public class Textures
{
    /**
     * Draws entity image
     *
     * @param mainPanel JPAnel
     * @param minMult float
     * @param name String
     * @param direction String [LEFT | RIGHT | UP | DOWN]
     * @param special int, used to retrieve variants of selected images
     * @return Image
     */
    public static Image drawEntity(JPanel mainPanel, float minMult, String name, String direction, int special)
    {
        int entitySize = (int) (28 * minMult);
        Image entity = mainPanel.createImage(entitySize, entitySize);
        Graphics g = entity.getGraphics();
        ((Graphics2D) g).setStroke(new BasicStroke(2 * minMult));
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, entitySize, entitySize);

        // Draw Bodies
        switch (name) {
            case "Eyes":
                break;
            case "Entity1":
                drawPacman(g, entitySize, direction, special);
                return entity;
            case "PacExplode":
                drawPacExplode(g, entitySize);
                return entity;
            case "CanBeEaten":
                drawGhostBody(g, name, entitySize, minMult, special);
                drawCanBeEatenFace(g, entitySize, special);
                return entity;
            default:
                drawGhostBody(g, name, entitySize, minMult,special);
                break;
        }

        // Draw Eyes
        drawEyes(g, entitySize, direction);

        return entity;
    }

    /**
     *  Draws an ifruit image correspodning to given level
     *
     * @param mainPanel JPanel
     * @param minMult float
     * @param level int
     * @return Image
     */
    public static Image drawFruit(JPanel mainPanel, float minMult, int level)
    {
        int entitySize = (int)(26 * minMult);
        Image entity = mainPanel.createImage(entitySize, entitySize);
        Graphics g = entity.getGraphics();
        ((Graphics2D)g).setStroke(new BasicStroke(3 * minMult));
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, entitySize, entitySize);

        switch(level){
            case 1:
                g.setColor(new Color(255,0,0));
                g.fillOval(0,entitySize/2,2*entitySize/5,2*entitySize/5);
                g.fillOval(entitySize/2,2*entitySize/4,2*entitySize/5,2*entitySize/5);
                g.setColor(new Color(191, 85,0));
                g.drawPolyline(
                        new int[]{entitySize/4, entitySize/4 + 4, entitySize, entitySize/2 + entitySize/6 + 2, entitySize/2 + entitySize/6},
                        new int[]{entitySize/2 + 1,entitySize/2 - 4,0,entitySize/2 - 3, entitySize/2 + 2},
                        5);
                g.setColor(Color.WHITE);
                g.fillOval(entitySize/12,entitySize/2 + 2*entitySize/5 - entitySize/4,entitySize/8,entitySize/8);
                g.fillOval(entitySize/2 + entitySize/8,entitySize/2 + 2*entitySize/5 - entitySize/4,entitySize/8,entitySize/8);
                break;
            case 2:
                g.setColor(new Color(255,0,0));
                g.fillOval(0,entitySize/10,2*entitySize/3,2*entitySize/3);
                g.fillOval(entitySize/3,entitySize/10,2*entitySize/3,2*entitySize/3);
                g.fillPolygon(
                        new int[]{entitySize/2, entitySize/16, 15*entitySize/16},
                        new int[]{entitySize, entitySize/2,entitySize/2}, 3);
                g.setColor(new Color(0, 240, 69));
                g.drawPolyline(new int[]{entitySize/4, entitySize/2,3*entitySize/4}, new int[]{0,entitySize/8, 0}, 3);
                g.setColor(Color.WHITE);
                g.drawLine(entitySize/2,0, entitySize/2, entitySize/8);
                g.fillOval(entitySize/8,entitySize/2 - entitySize/4,entitySize/8,entitySize/8);
                g.fillOval(6*entitySize/8,entitySize/2  - entitySize/4,entitySize/8,entitySize/8);
                g.fillOval(entitySize/2,entitySize/2 - entitySize/5,entitySize/8,entitySize/8);
                g.fillOval(entitySize/4,entitySize/2 + entitySize/5 - entitySize/4,entitySize/8,entitySize/8);
                g.fillOval(2*entitySize/3,entitySize/2 + entitySize/5 - entitySize/4,entitySize/8,entitySize/8);
                g.fillOval(entitySize/2,entitySize/2 + entitySize/5,entitySize/8,entitySize/8);
                break;
            case 3:
            case 4:
                g.setColor(new Color(255, 145, 42));
                g.fillOval(0,entitySize/8, entitySize, entitySize - entitySize/8);
                g.setColor(new Color(0, 240, 69));
                g.drawPolyline(new int[]{entitySize/2,3*entitySize/4}, new int[]{entitySize/7, 0}, 2);
                break;
            case 5:
            case 6:
                g.setColor(new Color(255,255,0));
                g.fillOval(0,0,entitySize, 5*entitySize/2);
                g.setColor(new Color(81,255,255));
                g.fillRect(entitySize/5, entitySize - entitySize/8, 3*entitySize/5, entitySize/8);
                g.setColor(Color.WHITE);
                g.fillRect(5*entitySize/8, entitySize - entitySize/8, entitySize/8, entitySize/8);
                g.drawLine(2*entitySize/9,4*entitySize/5, 3*entitySize/8,entitySize/5);
                break;
            case 7:
            case 8:
                g.setColor(new Color(0, 240, 69));
                g.drawPolyline(new int[]{entitySize/4, 5*entitySize/12,3*entitySize/5}, new int[]{0,entitySize/8, 0}, 3);
                g.setColor(new Color(255,0,0));
                g.fillOval(0,entitySize/10, entitySize/2, 9*entitySize/10);
                g.fillOval(entitySize/3,entitySize/16, 3*entitySize/5, 15*entitySize/16);
                g.setColor(Color.WHITE);
                g.drawLine(entitySize/6, entitySize/2, 2*entitySize/10, entitySize/3);
                break;
            case 9:
            case 10:
                g.setColor(new Color(191, 85,0));
                g.drawPolyline(new int[]{entitySize/5, 4*entitySize/5,3*entitySize/5,3*entitySize/5}, new int[]{0,0,0,entitySize/2}, 4);
                g.setColor(new Color(0, 240, 69));
                g.fillOval(entitySize/7, entitySize/5, 5*entitySize/7,4*entitySize/5);
                g.setColor(new Color(93, 124, 0));
                g.drawOval(entitySize/7, entitySize/5, 5*entitySize/7, 4*entitySize/5);
                g.drawLine(2*entitySize/7, 4*entitySize/5, 5*entitySize/7, entitySize/5);
                break;
            case 11:
            case 12:
                g.setColor(new Color(74, 145, 255));
                g.fillPolygon(new int[]{0,0,entitySize/2}, new int[]{0,3*entitySize/7, 5*entitySize/7}, 3);
                g.fillPolygon(new int[]{entitySize,entitySize,entitySize/2}, new int[]{0,3*entitySize/7,5*entitySize/7}, 3);
                g.setColor(new Color(255,255,0));
                g.fillPolygon(new int[]{0, entitySize, entitySize/2}, new int[]{entitySize/5, entitySize/5, 3*entitySize/5}, 3);
                g.drawLine(entitySize/2, entitySize/2, entitySize/2, entitySize);
                g.setColor(new Color(255,0,0));
                g.fillPolygon(new int[]{entitySize/2, 4*entitySize/5, entitySize/5}, new int[]{0,3*entitySize/10, 3*entitySize/10}, 3);
                break;
            default:
                g.setColor(new Color(190, 191, 190));
                g.fillOval(2*entitySize/5,entitySize/5,3*entitySize/10,4*entitySize/5);
                g.setColor(Color.BLACK);
                g.drawLine(3*entitySize/5, entitySize/5, 3*entitySize/5, 4*entitySize/5);
                g.setColor(new Color(67, 248, 255));
                g.fillOval(entitySize/5,0,3*entitySize/5,2*entitySize/5);
                g.setColor(Color.BLACK);
                g.fillOval(2*entitySize/5,entitySize/10,entitySize/5,entitySize/5);
                break;
        }

        return entity;
    }

    /**
     * Draws either normal or power pellet
     *
     * @param mainPanel JPanel
     * @param minMult float
     * @param power boolean
     * @return Image
     */
    public static Image drawPellet(JPanel mainPanel, float minMult, boolean power)
    {
        int entitySize = (int) (28 * minMult);
        int pelletSize = (int)(2 * minMult);
        int powerSize = entitySize - 2;

        Image entity = mainPanel.createImage(entitySize, entitySize);
        Graphics g = entity.getGraphics();;
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, entitySize, entitySize);

        if (!power) {
            g.setColor(new Color(246, 172, 152));
            g.fillOval((entitySize - powerSize) / 2, (entitySize - powerSize) / 2, powerSize, powerSize);
        } else {
            g.setColor(new Color(246, 172, 152));
            g.fillRect((entitySize - pelletSize) / 2, (entitySize - pelletSize) / 2, pelletSize, pelletSize);
        }

        return entity;
    }

    /**
     * Draws panel of fruits
     *
     * @param mainPanel JPanel
     * @param minMult int
     * @param levels List of collected fruits, last 7 will be drawn
     * @return Image
     */
    public static Image getFruits (JPanel mainPanel, float minMult, ArrayList<Integer> levels)
    {
        int entitySize = (int)(7 * 26 * minMult + 56);
        Image entity = mainPanel.createImage(entitySize, 35);
        Graphics g = entity.getGraphics();
        ((Graphics2D)g).setStroke(new BasicStroke(2 * minMult));
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, entitySize, 35);

        int count =  Math.min(7, levels.size());
        for (int i = 0; i < count; ++i)
        {
            Image fruit = drawFruit(mainPanel, minMult, levels.get(levels.size() - (i+1)));
            g.drawImage(fruit, (int)(((7-count)+i) * 26 * minMult + ((7-count)+i) * 8),0, null);
        }

        return entity;
    }

    /**
     * Draw ghosts' eyes
     *
     * @param g Graphics
     * @param entitySize int
     * @param direction String
     */
    private static void drawEyes(Graphics g, int entitySize, String direction)
    {
        int yDelta = 0;
        int xDelta = 0;
        switch(direction) {
            case "LEFT":
                xDelta = -entitySize/8;
                break;
            case "RIGHT":
                xDelta = entitySize/8;
                break;
            case "UP":
                yDelta = -entitySize/6;
                break;
            case "DOWN":
                yDelta = entitySize/6;
                break;
        }

        g.setColor(Color.WHITE);
        g.fillOval(entitySize/8 + xDelta/2, entitySize/3 - entitySize / 6 + yDelta/2,
                entitySize/4 + 1,entitySize/3 + 1);
        g.fillOval((5*entitySize)/8 + xDelta/2, entitySize/3 - entitySize / 6 + yDelta/2,
                entitySize/4 + 1,entitySize/3 + 1);

        g.setColor(new Color(76, 57,255));
        g.fillRect(entitySize/8 + entitySize/16 + 2 + xDelta,entitySize/3 - entitySize / 6 + entitySize/8 + yDelta,
                entitySize/8,entitySize/6);
        g.fillRect((5*entitySize)/8 + entitySize/16 + 2 + xDelta,entitySize/3 - entitySize / 6 + entitySize/8 + yDelta,
                entitySize/8,entitySize/6);
    }

    private static void drawCanBeEatenFace(Graphics g, int entitySize, int blink)
    {
        if (blink < 2) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(new Color(255,0,0));
        }

        g.fillRect(entitySize/8 + entitySize/16 + 5,entitySize/3 - entitySize / 6 + entitySize/8 - 4,
                entitySize/8 + 1,entitySize/6 + 1);
        g.fillRect((5*entitySize)/8 + entitySize/16 - 1,entitySize/3 - entitySize / 6 + entitySize/8 - 4,
                entitySize/8 + 1,entitySize/6 + 1);
        int dx = (entitySize)/9;
        int y = entitySize - 2*entitySize/6 - 2;
        g.drawPolyline(new int[]{2*dx,3*dx,4*dx,5*dx,6*dx,7*dx,8*dx}, new int[]{y,y-dx,y,y-dx,y,y-dx,y},7);
    }

    /**
     * Draws ghost's body
     *
     * @param g Graphics
     * @param name String
     * @param entitySize int
     * @param blink int [0 | 1]
     */
    private static void drawGhostBody(Graphics g, String name, int entitySize, float minMult, int blink)
    {
        switch(name)
        {
            case "Entity2":
                g.setColor(new Color(255,0,0));
                break;
            case "Entity3":
                g.setColor(new Color(255, 170, 227));
                break;
            case "Entity4":
                g.setColor(new Color(81,255,255));
                break;
            case "Entity5":
                g.setColor(new Color(255, 180, 95));
                break;
            case "CanBeEaten":
                if (blink < 2) {
                    g.setColor(new Color(29, 21, 255));
                } else {
                    g.setColor(new Color(249, 242, 235));
                }
                break;
        }

        int delta = (int)(2 * minMult); //former 4
        g.fillOval(delta,(int)(minMult), entitySize - delta, entitySize);
        g.fillRect(delta, entitySize/2, entitySize - delta, entitySize);
        g.setColor(Color.BLACK);
        if (blink == 0 || blink == 2) {
            g.fillRect(entitySize / 2 - entitySize / 16, entitySize - entitySize / 5, entitySize / 6, entitySize / 5);
            g.fillPolygon(new Polygon(
                    new int[]{(int)(1.5*minMult), entitySize / 8 + delta, entitySize / 3 + (int)(2.5 * minMult)},
                    new int[]{entitySize, entitySize - entitySize / 4, entitySize},
                    3));
            g.fillPolygon(new Polygon(
                    new int[]{entitySize - delta, entitySize - entitySize / 8 - delta, entitySize - entitySize / 3 - delta},
                    new int[]{entitySize, entitySize - entitySize / 4, entitySize},
                    3));
        } else {
            g.fillPolygon(new Polygon(
                    new int[]{delta, entitySize / 8 + 2*delta, entitySize / 3 + 3*delta},
                    new int[]{entitySize, entitySize - entitySize / 4 - (int)minMult, entitySize},
                    3));
            g.fillPolygon(new Polygon(
                    new int[]{entitySize - delta, entitySize - entitySize / 8 - 2*delta, entitySize - entitySize / 3 - 3*delta},
                    new int[]{entitySize, entitySize - entitySize / 4 - (int)(0.5 * minMult), entitySize},
                    3));
        }
        g.fillRect(0, entitySize - delta, entitySize, delta);
    }

    /**
     * Draws last frame of pacman's death
     *
     * @param g Graphics
     * @param entitySize int
     */
    private static void drawPacExplode(Graphics g, int entitySize)
    {
        g.setColor(Color.yellow);
        g.fillOval(0,0, entitySize, entitySize);
        g.setColor(Color.BLACK);
        g.fillOval(entitySize/4,entitySize/4, entitySize/2, entitySize/2);

        for (int angle = 0; angle < 360; angle += 45)
        {
            g.fillArc(-5,-5, entitySize + 10, entitySize + 10, angle, 35);
        }
    }

    /**
     * Draw pacman Image
     *
     * @param g Graphics
     * @param entitySize int
     * @param direction String [LEFT | RIGHT | UP | DOWN]
     * @param dying int dying progress indicator [0-10]
     */
    private static void drawPacman(Graphics g, int entitySize, String direction, int dying)
    {
        g.setColor(Color.yellow);
        g.fillOval(0,0, entitySize, entitySize);
        g.setColor(Color.BLACK);

        int delta = (int)(entitySize / 1.8);
        int dyingInc = 30 * dying;
        switch (direction)
        {
            case "LEFT":
                g.fillArc(-5,-5, entitySize + delta, entitySize + 10, -215, 70 + dyingInc);
                break;
            case "RIGHT":
                g.fillArc(5 - delta,-5, entitySize + delta, entitySize + 10, -35, 70 + dyingInc);
                break;
            case "UP":
                g.fillArc(-5,-5, entitySize + 10, entitySize + delta, -305, 70 + dyingInc);
                break;
            case "DOWN":
                g.fillArc(-5,5 - delta, entitySize + 10, entitySize + delta, -125, 70 + dyingInc);
        }
    }
}
