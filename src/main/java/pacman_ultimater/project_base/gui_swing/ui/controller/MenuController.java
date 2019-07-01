package pacman_ultimater.project_base.gui_swing.ui.controller;

import pacman_ultimater.project_base.core.*;
import pacman_ultimater.project_base.custom_utils.Pair;
import pacman_ultimater.project_base.gui_swing.model.GameModel;
import pacman_ultimater.project_base.gui_swing.model.MainFrameModel;
import pacman_ultimater.project_base.core.ClasspathFileReader;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Controlls menu state of the game.
 * If game play is initiated, instantiates and passes controll to gamePlayController.
 */
public class MenuController implements IKeyDownHandler
{
    //<editor-fold desc="- VARIABLES Block -">

    private ArrayList<JLabel> activeElements = new ArrayList<>();
    private int menuComponentsCount;
    private Pair<MenuController.mn, JLabel> menuSelected;
    private MenuController.mn menuLayer;
    private ArrayList<Character> symbols = new ArrayList<>();
    private MainFrameModel model;
    private GameModel vars;
    private GameplayController gp;
    private KeyBindings kb;

    // Must be public to enable MainFrameController to force its redraw
    public Editor editor;

    //</editor-fold>

    //<editor-fold desc="- CONSTRUCTION Block -">

    MenuController(MainFrameModel model, GameModel vars)
    {
        this.model = model;
        this.vars = vars;
        menuLayer = MenuController.mn.start;
        menuSelected = new Pair<>(MenuController.mn.game, model.orgGameLbl);

        initListeners();
        kb = new KeyBindings(model.mainPanel, this);
        kb.init();
    }

    /**
     * Put listeners on selected labels.
     */
    private void initListeners()
    {
        JLabel[] labels = {
            model.multiplayerLbl, model.orgGameLbl, model.selectMapLbl, model.settingsLbl, model.highScrLbl,
            model.editorLbl, model.howToLbl
        };
        for(JLabel label : labels)
            label.addMouseListener(new labelListener(label, mouseAdapterType.highlight_menu, model));

        model.errorInfoLbl.addComponentListener(new ErrorLblListener(model));
        model.escLabelLbl.addMouseListener(new labelListener(model.escLabelLbl, mouseAdapterType.clickToEscape, model));
        model.pressEnterLbl.addMouseListener(new labelListener(model.pressEnterLbl, mouseAdapterType.clickToEnter, model));
        model.musicButtonLbl.addMouseListener(new labelListener(model.musicButtonLbl, mouseAdapterType.highlight_settings, model));
        model.soundsButtonLbl.addMouseListener(new labelListener(model.soundsButtonLbl, mouseAdapterType.highlight_settings, model));
        model.editExistingButLbl.addMouseListener(new labelListener(model.editExistingButLbl, mouseAdapterType.highlight_editor, model));
        model.createNewButLbl.addMouseListener(new labelListener(model.createNewButLbl, mouseAdapterType.highlight_editor, model));
        model.tryAgainButLbl.addMouseListener(new labelListener(model.tryAgainButLbl, mouseAdapterType.tryAgainBtn, model));
        model.advancedLdButLbl.addMouseListener(new labelListener(model.advancedLdButLbl, mouseAdapterType.advancedLdBtn, model));
    }

    /**
     * Calls menu controller "entry point" displaying first menu screen.
     *
     * @throws NoSuchMethodException To be handled by calling procedure.
     * @throws IllegalAccessException To be handled by calling procedure.
     * @throws InvocationTargetException To be handled by calling procedure.
     */
    void openMenu()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        menu(MenuController.class.getDeclaredMethod("menu_Start"));
    }

    //</editor-fold>

    //<editor-fold desc="- MENU Block -"

    /**
     * Function that makes Menu work by simple enabling and disabling visibility of selected controls.
     *
     * @param menu_Func Function to be called (Built-in delegate for void function that takes no parameters).
     * @throws IllegalAccessException should be handled once in parent method.
     * @throws InvocationTargetException should be handled once in parent method.
     */
    private void menu(Method menu_Func)
            throws IllegalAccessException, InvocationTargetException
    {
        for (JLabel label : activeElements)
            label.setVisible(false);

        activeElements = new ArrayList<>();
        if(menu_Func != null)
            menu_Func.invoke(this);

        for (JLabel label : activeElements)
            label.setVisible(true);
    }

    private void menu_Start()
    {
        activeElements.add(model.pressEnterLbl);
        activeElements.add(model.pacmanLbl);
        activeElements.add(model.ultimateLbl);
        activeElements.add(model.copyrightLbl);
    }

    private void menu_MainMenu()
    {
        activeElements.add(model.selectMapLbl);
        activeElements.add(model.orgGameLbl);
        activeElements.add(model.settingsLbl);
        activeElements.add(model.escLabelLbl);
        activeElements.add(model.highScrLbl);
        activeElements.add(model.multiplayerLbl);
        activeElements.add(model.editorLbl);
        activeElements.add(model.howToLbl);
    }

    private void menu_SelectMap()
    {
        activeElements.add(model.errorLdMapLbl);
        activeElements.add(model.advancedLdButLbl);
        activeElements.add(model.tryAgainButLbl);
        activeElements.add(model.escLabelLbl);
    }

    private void menu_HighScore1P()
            throws IOException
    {
        if (vars.score != 0)
        {
            model.gameOverLabelLbl.setText("GAME OVER");
            model.gameOverLabelLbl.setForeground(Color.red);
            model.gameOverLabelLbl.setLocation(52, 33);
            model.scoreLabelLbl.setText("Your Score");
            model.scoreNumLbl.setText(Integer.toString(vars.score));
        }
        else
        {
            model.scoreLabelLbl.setText("");
            model.scoreNumLbl.setText("");
            model.gameOverLabelLbl.setText("");
        }
        if (vars.highScore == -1)
        {
            //In case the HighScore is not loaded yet (value is -1) do so
            vars.highScore = HighScoreClass.loadHighScore();
        }
        model.highScoreLabelLbl.setText("Highest Score");
        model.highScoreLabelLbl.setForeground(Color.yellow);
        model.highScoreNumLbl.setForeground(Color.yellow);
        model.highScoreNumLbl.setText(Integer.toString(vars.highScore));
    }

    private void menu_HighScore2P()
    {
        //Game selects the winner as the player with highest score
        //In case of tie chooses the winner by remaining pacman lives
        if (vars.score < vars.score2 || (vars.score == vars.score2 && vars.lives <= 0))
        {
            model.gameOverLabelLbl.setText("GHOSTS WIN");
            model.gameOverLabelLbl.setForeground(Color.red);
            model.gameOverLabelLbl.setLocation(36, 33);
            model.highScoreLabelLbl.setText("2UP");
            model.highScoreLabelLbl.setForeground(Color.red);
            model.highScoreNumLbl.setText(Integer.toString(vars.score2));
            model.highScoreNumLbl.setForeground(Color.red);
            model.scoreLabelLbl.setText("1UP");
            model.scoreNumLbl.setText(Integer.toString(vars.score));
        }
        else
        {
            model.gameOverLabelLbl.setText("PACMAN WINS");
            model.gameOverLabelLbl.setForeground(Color.yellow);
            model.gameOverLabelLbl.setLocation(34, 33);
            model.highScoreLabelLbl.setText("1UP");
            model.highScoreNumLbl.setText(Integer.toString(vars.score));
            model.scoreLabelLbl.setText("2UP");
            model.scoreNumLbl.setText(Integer.toString(vars.score2));
        }
    }

    private void menu_HighScore()
            throws IOException
    {
        activeElements.add(model.gameOverLabelLbl);
        activeElements.add(model.scoreLabelLbl);
        activeElements.add(model.scoreNumLbl);
        activeElements.add(model.highScoreLabelLbl);
        activeElements.add(model.highScoreNumLbl);
        activeElements.add(model.escLabelLbl);

        if (!vars.player2)
            menu_HighScore1P();
        else
            menu_HighScore2P();

        //To be able to access default Highscore page later from menu.
        vars.score = 0;
        vars.player2 = false;
    }

    private void menu_Editor()
    {
        activeElements.add(model.escLabelLbl);
        activeElements.add(model.editExistingButLbl);
        activeElements.add(model.createNewButLbl);
        model.createBtnSelectorLbl.setVisible(true);
    }

    private void menu_HowTo()
    {
        activeElements.add(model.escLabelLbl);
    }

    private void menu_Settings()
    {
        activeElements.add(model.musicButtonLbl);
        activeElements.add(model.soundsButtonLbl);
        activeElements.add(model.escLabelLbl);
        model.musicBtnSelectorLbl.setVisible(true);

        if (vars.music)
            model.musicButtonLbl.setText("<html><div style='padding-left: 8px; background-color: black; width: 140px;'>MUSIC</div></html>");
        else
            model.musicButtonLbl.setText("<html><div style='padding-left: 8px; background-color: gray; width: 140px;'>MUSIC</div></html>");

        if (vars.sound)
            model.soundsButtonLbl.setText("<html><div style='padding-left: 8px; background-color: black; width: 150px;'>SOUND</div></html>");
        else
            model.soundsButtonLbl.setText("<html><div style='padding-left: 8px; background-color: gray; width: 150px;'>SOUND</div></html>");
    }

    /**
     * Loads predefined original map and calls proceedToGameplay() to proceed to gameplay.
     *
     * @throws InvocationTargetException Exception si to be handled by caller.
     * @throws IllegalAccessException Exception si to be handled by caller.
     */
    private void orgGame_Click()
            throws InvocationTargetException, IllegalAccessException
    {
        vars.loadedMap = new LoadMap(ClasspathFileReader.getORIGINAL_MAP());
        if (vars.loadedMap.Map != null) {
            menu(null);
            proceedToGameplay();
        }
    }

    /**
     * Opens menu for advanced map loading.
     */
    private void advancedLdBut_Click()
    {
        menuLayer = mn.submenu;
        activeElements.add(model.typeSymbolsLbl);
        activeElements.add(model.typedSymbolsLbl);
        model.typedSymbolsLbl.setText("");
        model.typedSymbolsLbl.grabFocus();
        activeElements.add(model.typeHintLbl);
        for (JLabel label : activeElements)
            label.setVisible(true);
    }

    /**
     * Simulates select map click and sets number of players to 2 in case of successful map load.
     *
     * @throws NoSuchMethodException To be handled by calling procedure.
     * @throws IllegalAccessException To be handled by calling procedure.
     * @throws InvocationTargetException To be handled by calling procedure.
     * @throws FileNotFoundException To be handled by calling procedure.
     */
    private void multiplayer_Click()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, FileNotFoundException
    {
        selectMap_Click();
        vars.player2 = vars.gameOn;
    }

    /**
     * Opens file dialog after clinking on Select Map in menu.
     * Tries to open and load selected map from the file afterwards.
     * Calls procedure proceedToGameplay() in case of success.
     *
     * @throws NoSuchMethodException To be handled by calling procedure.
     * @throws IllegalAccessException To be handled by calling procedure.
     * @throws InvocationTargetException To be handled by calling procedure.
     * @throws FileNotFoundException To be handled by calling procedure.
     */
    private void selectMap_Click()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, FileNotFoundException
    {
        if (model.openFileDialog1.showOpenDialog(model.mainPanel) == JFileChooser.APPROVE_OPTION)
        {
            menuLayer = mn.submenu;
            menu(this.getClass().getDeclaredMethod("menu_SelectMap"));
            String path = model.openFileDialog1.getSelectedFile().getAbsolutePath();
            if (symbols.size() == 0)
                vars.loadedMap = new LoadMap(new FileInputStream(path));
            else
            {
                Character[] chars = symbols.toArray(new Character[0]);
                vars.loadedMap = new LoadMap(new FileInputStream(path), chars);
                symbols = new ArrayList<>();
            }
            if (vars.loadedMap.Map != null) {
                menu(null);
                proceedToGameplay();
            }
        }
    }

    /**
     * Used as bridge from gamePlayController back to MenuController
     */
    private class GameOverHandler implements IGameOverHandler
    {
        public void handleGameOverRequest(){
            returnToMenu();
        }
    }

    /**
     * Function that provides bridge from menu to gameplay.
     * Switches key binding's handler to GamePlayController's one.
     * Initializes Map and on success calls function that makes the game load process start.
     */
    private void proceedToGameplay()
    {
        model.mainPanel.requestFocus();
        vars.level = 1;
        vars.gameOn = true;
        vars.map = vars.loadedMap.Map;
        menuComponentsCount = model.mainPanel.getComponentCount();
        for(int i = 0; i < menuComponentsCount; ++i)
            model.mainPanel.getComponent(i).setVisible(false);

        try {
            gp = new GameplayController(model, vars, new GameOverHandler());
            kb.changeHandler(gp);
            gp.loadGame();
        }
        catch(LineUnavailableException e)
        {
            MainFrameController.handleExceptions(e.getMessage(), model);
            model.disposeMainFrame();
        }
    }

    /**
     * Function that provides bridge from gameplay to menu.
     * Switches key binding's handler back to MenuController's one.
     */
    private void returnToMenu()
    {
        kb.changeHandler(this);
        gp = null;
        int componentsCount = model.mainPanel.getComponentCount();
        for(int i = componentsCount - 1; i >= 0; --i){
            if(i >= menuComponentsCount)
                model.mainPanel.remove(i);
        }
        highlightSelected(menuSelected.item2, model.highScrLbl);
        menuSelected = new Pair<>(mn.highscore, model.highScrLbl);
        menuLayer = mn.submenu;
        try {
            menu(this.getClass().getDeclaredMethod("menu_HighScore"));
        }
        catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
            MainFrameController.handleExceptions(e.getMessage(), model);
            model.disposeMainFrame();
        }

        model.mainPanel.repaint();
        model.mainPanel.revalidate();
        model.mainPanel.requestFocus();
    }

    /**
     * Enables/Disables Sound and Sound button in menu - settings.
     */
    private void soundsButton_Click()
    {
        vars.sound = !vars.sound;
        if (vars.sound)
        {
            model.soundsButtonLbl.setText("<html><div style='padding-left: 8px; background-color: black; width: 150px;'>SOUND</div></html>");
            model.soundsButtonLbl.setForeground(Color.white);
        }
        else
        {
            model.soundsButtonLbl.setText("<html><div style='padding-left: 8px; background-color: gray; width: 150px;'>SOUND</div></html>");
            model.soundsButtonLbl.setForeground(Color.black);
        }
    }

    /**
     * Enables/Disables Music and Music button in menu - settings.
     */
    private void musicButton_Click()
    {
        vars.music = !vars.music;
        if (vars.music)
        {
            model.musicButtonLbl.setText("<html><div style='padding-left: 8px; background-color: black; width: 140px;'>MUSIC</div></html>");
            model.musicButtonLbl.setForeground(Color.white);
        }
        else
        {
            model.musicButtonLbl.setText("<html><div style='padding-left: 8px; background-color: gray; width: 140px;'>MUSIC</div></html>");
            model.musicButtonLbl.setForeground(Color.black);
        }
    }

    /**
     * Opens file selector for editor.
     */
    private void editButton_Click()
        throws FileNotFoundException
    {
        if (model.openFileDialog1.showOpenDialog(model.mainPanel) == JFileChooser.APPROVE_OPTION) {
            String path = model.openFileDialog1.getSelectedFile().getAbsolutePath();
            LoadMap map = new LoadMap(new FileInputStream(path));
            if (map.Map != null) {
                model.editBtnSelectorLbl.setVisible(false);
                model.createBtnSelectorLbl.setVisible(false);
                model.editExistingButLbl.setVisible(false);
                model.createNewButLbl.setVisible(false);
                vars.editor = true;
                editor = new Editor(model.mainPanel, vars, map.Map);
                editor.show();
            } else {
                UIManager.put("Panel.background", Color.black);
                JOptionPane.showMessageDialog(model.mainPanel, "Not a playable map");
                UIManager.put("Panel.background", Color.white);
            }
        }
    }

    /**
     * Opens editor with empty template.
     */
    private void createButton_Click()
    {
        model.editBtnSelectorLbl.setVisible(false);
        model.createBtnSelectorLbl.setVisible(false);
        model.editExistingButLbl.setVisible(false);
        model.createNewButLbl.setVisible(false);
        vars.editor = true;
        editor = new Editor(model.mainPanel,vars);
        editor.show();
    }

    private void settings_Click() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        menuLayer = mn.submenu;
        menu(this.getClass().getDeclaredMethod("menu_Settings"));
    }

    private void editor_Click() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        menuLayer = mn.submenu;
        menu(this.getClass().getDeclaredMethod("menu_Editor"));
    }

    private void howTo_Click() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        menuLayer = mn.submenu;
        menu(this.getClass().getDeclaredMethod("menu_HowTo"));
    }

    private void highScr_Click() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        menuLayer = mn.submenu;
        menu(this.getClass().getDeclaredMethod("menu_HighScore"));
    }

    /**
     * Function to convert typed characters separated by ';' to string for output.
     *
     * @param source Input characters.
     * @return String of characters separated with " ; ".
     */
    private String charListToString(ArrayList<Character> source)
    {
        StringBuilder output = new StringBuilder();
        if (source.size() > 0)
            output.append(source.get(0).toString());

        for (int i = 1; i < source.size(); i++)
        {
            output.append(" ; ");
            output.append(source.get(i));
        }
        return output.toString();
    }

    /**
     * Changes selected element in menu.
     *
     * @param delta Indicates whether to move up or down in menu (Values +1/-1).
     */
    private void moveInMenu(int delta)
    {
        final byte menuSize = 7;

        JLabel newLabel = enumToLabel(mn.values()[((menuSelected.item1.ordinal() + delta + menuSize) % menuSize)]);
        highlightSelected(menuSelected.item2, newLabel);
        menuSelected = new Pair<>(mn.values()[((menuSelected.item1.ordinal() + delta + menuSize) % menuSize)], newLabel);
    }

    /**
     * Changes selected element in settings.
     */
    private void moveInSettings()
    {
        if (model.soundsBtnSelectorLbl.isVisible())
            musicButton_MouseEnter();
        else
            soundsButton_MouseEnter();
    }

    /**
     * Changes selected element in editor
     */
    private void moveInEditor()
    {
        if (model.editBtnSelectorLbl.isVisible()) {
            createButton_MouseEnter();
        } else {
            editButton_MouseEnter();
        }
    }

    /**
     * Selects SoundsButton when mouse cursor enters its domain.
     */
    private void soundsButton_MouseEnter()
    {
        model.soundsBtnSelectorLbl.setVisible(true);
        model.musicBtnSelectorLbl.setVisible(false);
    }

    /**
     * Selects MusicButton when mouse cursor enters its domain.
     */
    private void musicButton_MouseEnter()
    {
        model.soundsBtnSelectorLbl.setVisible(false);
        model.musicBtnSelectorLbl.setVisible(true);
    }

    /**
     * Selects CreateButton when mouse cursor enters its domain.
     */
    private void createButton_MouseEnter()
    {
        model.editBtnSelectorLbl.setVisible(false);
        model.createBtnSelectorLbl.setVisible(true);
    }

    /**
     * Selects EditButton when mouse cursor enters its domain.
     */
    private void editButton_MouseEnter()
    {
        model.createBtnSelectorLbl.setVisible(false);
        model.editBtnSelectorLbl.setVisible(true);
    }

    /**
     * Function implementing key press handler in menu.
     *
     * @param keyCode int code of pressed key.
     */
    public void handleKey(int keyCode)
    {
        try {
            if (menuLayer == mn.start) {
                if (keyCode == KeyEvent.VK_ESCAPE) {
                    model.disposeMainFrame();
                }
                else {
                    menu(this.getClass().getDeclaredMethod("menu_MainMenu"));
                    highlightSelected(menuSelected.item2, model.orgGameLbl);
                    menuSelected = new Pair<>(mn.game, model.orgGameLbl);
                    menuLayer = mn.game;
                }
            } else {
                if (keyCode == KeyEvent.VK_ESCAPE) {
                    // Escape returns you to menu form everywhere except from menu itself.
                    if (menuLayer == mn.submenu) {
                        if (menuSelected.item1 == mn.settings) {
                            model.musicBtnSelectorLbl.setVisible(false);
                            model.soundsBtnSelectorLbl.setVisible(false);
                        } else if (menuSelected.item1 == mn.editor) {
                            model.editBtnSelectorLbl.setVisible(false);
                            model.createBtnSelectorLbl.setVisible(false);
                            if (editor != null && vars.editor) {
                                if (!editor.close()) {
                                    return;
                                }

                                vars.editor = false;
                                editor = null;
                                model.mainPanel.repaint();
                            }
                        }
                        menu(this.getClass().getDeclaredMethod("menu_MainMenu"));
                        menuLayer = mn.game;
                    } else {
                        menuLayer = mn.start;
                        menu(this.getClass().getDeclaredMethod("menu_Start"));
                    }
                } else if (model.typedSymbolsLbl.isVisible()) {
                    // Branch accessible during typing of symbols used on Map to load.
                    if (keyCode == KeyEvent.VK_BACK_SPACE && symbols.size() > 0) {
                        symbols.remove(symbols.size() - 1);
                        model.typedSymbolsLbl.setText(charListToString(symbols));
                    }
                    if (!symbols.contains((char)keyCode) && keyCode != KeyEvent.VK_BACK_SPACE) {
                        symbols.add((char)keyCode);
                        model.typedSymbolsLbl.setText(charListToString(symbols));
                    }
                    model.mainPanel.revalidate();
                    final byte symbolsLimit = 5;
                    if (symbols.size() == symbolsLimit)
                        selectMap_Click();
                } else if (menuSelected.item1 == mn.editor && editor != null && vars.editor) {
                    editor.handleKey(keyCode);
                } else if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
                    handleKeyWS(-1);
                } else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
                    handleKeyWS(1);
                } else if (keyCode == KeyEvent.VK_ENTER)
                    if (menuLayer == mn.game) {
                        Method action = enumToAction(menuSelected.item1);
                        if (action != null)
                            action.invoke(this);
                    } else if (menuSelected.item1 == mn.settings) {
                        if (model.musicBtnSelectorLbl.isVisible())
                            musicButton_Click();
                        else
                            soundsButton_Click();
                    } else if (menuSelected.item1 == mn.editor) {
                        if (model.editBtnSelectorLbl.isVisible())
                            editButton_Click();
                        else
                            createButton_Click();
                    }
            }
        }
        catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException | FileNotFoundException exception)
        {
            if (vars.score > vars.highScore) {
                try {
                    HighScoreClass.tryToSaveScore(vars.player2, vars.score);
                } catch (IOException ignore) { /* TODO: Notify user score weren't saved due to exception message */ }
            }
            MainFrameController.handleExceptions(exception.toString(), model);
        }

        model.mainPanel.revalidate();
    }

    /**
     * Handles W, S, up, down key resolving
     *
     * @param delta movement in menu
     */
    private void handleKeyWS(int delta)
    {
        if (menuLayer == mn.submenu) {
            if (menuSelected.item1 == mn.settings) {
                moveInSettings();
            } else if (menuSelected.item1 == mn.editor) {
                moveInEditor();
            }
        } else if (menuLayer == mn.game)
            moveInMenu(delta);
    }

    /**
     * Function that switches active labels by unhighlighting the old one and highlighting the new one.
     *
     * @param prevLabel Previous active label.
     * @param newLabel New active label.
     */
    private void highlightSelected(JLabel prevLabel, JLabel newLabel)
    {
        if(prevLabel != null) {
            prevLabel.setForeground(Color.white);
        }
        if(newLabel != null) {
            newLabel.setForeground(Color.yellow);
        }
    }

    //</editor-fold>

    //<editor-fold desc="- MN Enum -"

    /**
     * Enumeration for identifying menu states.
     */
    enum mn { game, selectmap, multiplayer, settings, editor, howTo, highscore, start, submenu, none }

    /**
     * Function that associates selected labels to menu states.
     *
     * @param label Label to be associated.
     * @return Resulting associated menu state (default: mn.start).
     */
    private mn labelToEnum(JLabel label)
    {
        switch(label.getName()) {
            case "OrgGame":
                return mn.game;
            case "SelectMap":
                return mn.selectmap;
            case "Multiplayer":
                return mn.multiplayer;
            case "HighScr":
                return mn.highscore;
            case "Settings":
                return mn.settings;
            case "Editor":
                return mn.editor;
            case "HowTo":
                return mn.howTo;
            default:
                return mn.start;
        }
    }

    /**
     * Function that associates menu states to selected labels.
     *
     * @param selected Menu state to be associated.
     * @return Resulting associated label (default: OrgGame).
     */
    private JLabel enumToLabel(mn selected)
    {
        switch(selected) {
            case game:
                return model.orgGameLbl;
            case selectmap:
                return model.selectMapLbl;
            case multiplayer:
                return model.multiplayerLbl;
            case highscore:
                return model.highScrLbl;
            case settings:
                return model.settingsLbl;
            case editor:
                return model.editorLbl;
            case howTo:
                return model.howToLbl;
            default:
                return model.orgGameLbl;
        }
    }

    /**
     * Function that associates on-click functions to menu states.
     *
     * @param selected Menu state to be associated.
     * @return Resulting on-click function (default: OrgGame_Click).
     * @throws NoSuchMethodException Exception is to be handled in parent.
     */
    private Method enumToAction(mn selected) throws NoSuchMethodException
    {
        switch(selected){
            case game:
                return this.getClass().getDeclaredMethod("orgGame_Click");
            case selectmap:
                return this.getClass().getDeclaredMethod("selectMap_Click");
            case multiplayer:
                return this.getClass().getDeclaredMethod("multiplayer_Click");
            case highscore:
                return this.getClass().getDeclaredMethod("highScr_Click");
            case settings:
                return this.getClass().getDeclaredMethod("settings_Click");
            case editor:
                return this.getClass().getDeclaredMethod("editor_Click");
            case howTo:
                return this.getClass().getDeclaredMethod("howTo_Click");
            default:
                return null;
        }
    }

    //</editor-fold>

    //<editor-fold desc="- LISTENERS Block -">

    private enum mouseAdapterType {
        highlight_menu, highlight_settings, highlight_editor, clickToEnter, clickToEscape, tryAgainBtn, advancedLdBtn
    }

    private class labelListener extends MouseAdapter
    {
        private JLabel label;
        private mouseAdapterType mat;
        private MainFrameModel model;

        labelListener(JLabel label, mouseAdapterType mat, MainFrameModel model)
        {
            this.label = label;
            this.mat = mat;
            this.model = model;
        }

        /**
         * Function simulating enter/escape key push on label clicked.
         *
         * @param e MouseEvent
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            try {
                switch (mat) {
                    case clickToEscape:
                        handleKey(KeyEvent.VK_ESCAPE);
                        break;
                    case tryAgainBtn:
                        selectMap_Click();
                        break;
                    case advancedLdBtn:
                        advancedLdBut_Click();
                        break;
                    default:
                        handleKey(KeyEvent.VK_ENTER);
                        break;
                }
            }
            catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | FileNotFoundException exception)
            {
                if (vars.score > vars.highScore) {
                    try {
                        HighScoreClass.tryToSaveScore(vars.player2, vars.score);
                    } catch (IOException ignore) { /* TODO: Notify user score weren't saved due to exception message */ }
                }
                MainFrameController.handleExceptions(exception.getMessage(), model);
            }
        }

        /**
         * Function that provides color change of labels in menu on cursor entry of label's domain.
         *
         * @param e MouseEvent
         */
        @Override
        public void mouseEntered(MouseEvent e)
        {
            switch (mat){
                case highlight_settings:
                    if (label.getName().equals("MusicButton"))
                        musicButton_MouseEnter();
                    else
                        soundsButton_MouseEnter();
                    break;
                case highlight_editor:
                    if (label.getName().equals("EditExistingBut"))
                        editButton_MouseEnter();
                    else
                        createButton_MouseEnter();
                    break;
                case clickToEscape:
                    label.setForeground(Color.white);
                    break;
                case clickToEnter:
                    label.setForeground(Color.yellow);
                    break;
                case highlight_menu:
                    highlightSelected(menuSelected.item2, label);
                    menuSelected = new Pair<>(labelToEnum(label), label);
                    break;
                case tryAgainBtn:
                    model.tryAgainButLbl.setBackground(Color.yellow);
                    break;
                case advancedLdBtn:
                    model.advancedLdButLbl.setBackground(Color.yellow);
                    break;
            }
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
            switch (mat){
                case clickToEscape:
                    label.setForeground(Color.yellow);
                    break;
                case clickToEnter:
                    label.setForeground(Color.white);
                    break;
                case tryAgainBtn:
                    model.tryAgainButLbl.setBackground(Color.white);
                    break;
                case advancedLdBtn:
                    model.advancedLdButLbl.setBackground(Color.white);
                    break;
            }

        }
    }

    private class ErrorLblListener implements ComponentListener
    {
        MainFrameModel model;

        ErrorLblListener(MainFrameModel model)
        {
            this.model = model;
        }

        @Override
        public void componentResized(ComponentEvent e) { }

        @Override
        public void componentMoved(ComponentEvent e) { }

        /**
         * Kills the program 2,5s after the error label has appeared.
         *
         * @param e ComponentEvent
         */
        @Override
        public void componentShown(ComponentEvent e)
        {
            try {
                Thread.sleep(5000);
            }
            catch(InterruptedException ignore){ }
            model.disposeMainFrame();
        }

        @Override
        public void componentHidden(ComponentEvent e) { }
    }

    //</editor-fold>
}
