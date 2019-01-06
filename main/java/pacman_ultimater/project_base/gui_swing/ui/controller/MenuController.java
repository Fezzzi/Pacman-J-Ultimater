package pacman_ultimater.project_base.gui_swing.ui.controller;

import pacman_ultimater.project_base.core.HighScoreClass;
import pacman_ultimater.project_base.core.KeyBindings;
import pacman_ultimater.project_base.core.LoadMap;
import pacman_ultimater.project_base.custom_utils.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MenuController{

    //<editor-fold desc="- VARIABLES Block -">

    private ArrayList<JLabel> activeElements = new ArrayList<>();
    private Pair<MenuController.mn, JLabel> menuSelected;
    private MenuController.mn menuLayer;
    private ArrayList<Character> symbols = new ArrayList<>();
    private MainFrameController mfc;

    //</editor-fold>

    //<editor-fold desc="- CONSTRUCTION Block -">

    MenuController(MainFrameController controller){
        mfc = controller;

        initListeners();
        addKeyBindings();
    }

    /**
     * Put listeners on selected labels and mainFrame.
     */
    private void initListeners(){
        mfc.mainFrame.addWindowListener(new MenuController.windowListener());

        JLabel[] labels = {mfc.vsLbl, mfc.orgGameLbl, mfc.selectMapLbl, mfc.settingsLbl, mfc.highScrLbl};
        for(JLabel label : labels)
            label.addMouseListener(new labelListener(label, mouseAdapterType.highlight_menu));

        mfc.errorInfoLbl.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) { }

            @Override
            public void componentMoved(ComponentEvent e) { }

            /**
             * Kills the program 2,5s after the error label has appeared.
             * @param e ComponentEvent
             */
            @Override
            public void componentShown(ComponentEvent e) {
                try{
                    Thread.sleep(2500);
                }
                catch(InterruptedException ignore){ }
                mfc.mainFrame.dispose();
            }

            @Override
            public void componentHidden(ComponentEvent e) { }
        });
        mfc.escLabelLbl.addMouseListener(new labelListener(mfc.escLabelLbl, mouseAdapterType.clickToEscape));
        mfc.pressEnterLbl.addMouseListener(new labelListener(mfc.pressEnterLbl, mouseAdapterType.clickToEnter));
        mfc.musicButtonLbl.addMouseListener(new labelListener(mfc.musicButtonLbl, mouseAdapterType.highlight_settings));
        mfc.soundsButtonLbl.addMouseListener(new labelListener(mfc.soundsButtonLbl, mouseAdapterType.highlight_settings));
        mfc.tryAgainButLbl.addMouseListener(new labelListener(mfc.tryAgainButLbl, mouseAdapterType.tryAgainBtn));
        mfc.advancedLdButLbl.addMouseListener(new labelListener(mfc.advancedLdButLbl, mouseAdapterType.advancedLdBtn));
    }

    //</editor-fold>

    //<editor-fold desc="- MENU Block -"

    /**
     * Function that makes Menu work by simple enabling and disabling visibility of selected controls.
     * @param menu_Func Function to be called (Built-in delegate for void function that takes no parameters).
     * @throws IllegalAccessException should be handled once in parent method.
     * @throws InvocationTargetException should be handled once in parent method.
     */
    void menu(Method menu_Func) throws IllegalAccessException, InvocationTargetException
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
        activeElements.add(mfc.pressEnterLbl);
        activeElements.add(mfc.pacmanLbl);
        activeElements.add(mfc.ultimateLbl);
        activeElements.add(mfc.copyrightLbl);
    }

    private void menu_MainMenu()
    {
        activeElements.add(mfc.selectMapLbl);
        activeElements.add(mfc.orgGameLbl);
        activeElements.add(mfc.settingsLbl);
        activeElements.add(mfc.escLabelLbl);
        activeElements.add(mfc.highScrLbl);
        activeElements.add(mfc.vsLbl);
    }

    private void menu_SelectMap()
    {
        activeElements.add(mfc.errorLdMapLbl);
        activeElements.add(mfc.advancedLdButLbl);
        activeElements.add(mfc.tryAgainButLbl);
        activeElements.add(mfc.escLabelLbl);
    }

    private void menu_HighScore1P() throws IOException
    {
        if (mfc.score != 0)
        {
            mfc.gameOverLabelLbl.setText("GAME OVER");
            mfc.gameOverLabelLbl.setForeground(Color.red);
            mfc.gameOverLabelLbl.setLocation(52, 33);
            mfc.scoreLabelLbl.setText("Your Score");
            mfc.scoreNumLbl.setText(Integer.toString(mfc.score));
        }
        else
        {
            mfc.scoreLabelLbl.setText("");
            mfc.scoreNumLbl.setText("");
            mfc.gameOverLabelLbl.setText("");
        }
        if (mfc.highScore == -1)
        {
            //In case the HighScore is not loaded yet (value is -1) do so
            mfc.highScore = HighScoreClass.loadHighScore(mfc.resourcesPath);
        }
        mfc.highScoreLabelLbl.setText("Highest Score");
        mfc.highScoreLabelLbl.setForeground(Color.yellow);
        mfc.highScoreNumLbl.setForeground(Color.yellow);
        mfc.highScoreNumLbl.setText(Integer.toString(mfc.highScore));
    }

    private void menu_HighScore2P()
    {
        //Game selects the winner as the pleyer with highest score
        //In case of tie chooses the winner by remaining pacman lives
        if (mfc.score < mfc.score2 || (mfc.score == mfc.score2 && mfc.lives <= 0))
        {
            mfc.gameOverLabelLbl.setText("GHOSTS WIN");
            mfc.gameOverLabelLbl.setForeground(Color.red);
            mfc.gameOverLabelLbl.setLocation(36, 33);
            mfc.highScoreLabelLbl.setText("2UP");
            mfc.highScoreLabelLbl.setForeground(Color.red);
            mfc.highScoreNumLbl.setText(Integer.toString(mfc.score2));
            mfc.highScoreNumLbl.setForeground(Color.red);
            mfc.scoreLabelLbl.setText("1UP");
            mfc.scoreNumLbl.setText(Integer.toString(mfc.score));
        }
        else
        {
            mfc.gameOverLabelLbl.setText("PACMAN WINS");
            mfc.gameOverLabelLbl.setForeground(Color.yellow);
            mfc.gameOverLabelLbl.setLocation(34, 33);
            mfc.highScoreLabelLbl.setText("1UP");
            mfc.highScoreNumLbl.setText(Integer.toString(mfc.score));
            mfc.scoreLabelLbl.setText("2UP");
            mfc.scoreNumLbl.setText(Integer.toString(mfc.score2));
        }
    }

    private void menu_HighScore() throws IOException
    {
        activeElements.add(mfc.gameOverLabelLbl);
        activeElements.add(mfc.scoreLabelLbl);
        activeElements.add(mfc.scoreNumLbl);
        activeElements.add(mfc.highScoreLabelLbl);
        activeElements.add(mfc.highScoreNumLbl);
        activeElements.add(mfc.escLabelLbl);

        //Two branches depending on the mode player has chosen - normal x VS
        if (!mfc.player2)
            menu_HighScore1P();
        else
            menu_HighScore2P();

        //It is necessary to set scre and player boolean in order to be able to access default Highscore
        //page later on from menu
        mfc.score = 0;
        mfc.player2 = false;
    }

    private void menu_Settings()
    {
        activeElements.add(mfc.musicButtonLbl);
        activeElements.add(mfc.soundsButtonLbl);
        activeElements.add(mfc.escLabelLbl);
        mfc.musicBtnSelectorLbl.setVisible(true);

        // Buttons load with color depending on associated booleans.
        // Music button is by default selected by arrows.
        if (mfc.music)
            mfc.musicButtonLbl.setText("<html><div style='padding-left: 8px; background-color: black; width: 140px;'>MUSIC</div></html>");
        else
            mfc.musicButtonLbl.setText("<html><div style='padding-left: 8px; background-color: gray; width: 140px;'>MUSIC</div></html>");

        if (mfc.sound)
            mfc.soundsButtonLbl.setText("<html><div style='padding-left: 8px; background-color: black; width: 150px;'>SOUND</div></html>");
        else
            mfc.soundsButtonLbl.setText("<html><div style='padding-left: 8px; background-color: gray; width: 150px;'>SOUND</div></html>");
    }

    /**
     * Loads predefined original map and calls makeItHappen() to procced to gameplay.
     */
    private void orgGame_Click() throws InvocationTargetException, IllegalAccessException
    {
        mfc.loadedMap = new LoadMap(mfc.resourcesPath + "\\OriginalMap.txt");
        if (mfc.loadedMap.Map != null) {
            menu(null);
            mfc.makeItHappen();
        }
    }

    /**
     * Opens menu for advanced map loading.
     */
    private void advancedLdBut_Click()
    {
        menuLayer = mn.submenu;
        activeElements.add(mfc.typeSymbolsLbl);
        activeElements.add(mfc.typedSymbolsLbl);
        mfc.typedSymbolsLbl.setText("");
        mfc.typedSymbolsLbl.grabFocus();
        activeElements.add(mfc.typeHintLbl);
        for (JLabel label : activeElements)
            label.setVisible(true);
    }

    /**
     * Opens file dialog after clinking on Select Map in menu.
     * Tries to open and load selected map from the file afterwards.
     * Calls procedure makeItHappen() in case of success.
     */
    private void selectMap_Click() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        if (mfc.openFileDialog1.showOpenDialog(mfc.mainFrame) == JFileChooser.APPROVE_OPTION)
        {
            menuLayer = mn.submenu;
            menu(this.getClass().getDeclaredMethod("menu_SelectMap"));
            String path = mfc.openFileDialog1.getSelectedFile().getAbsolutePath();
            if (symbols.size() == 0)
                mfc.loadedMap = new LoadMap(path);
            else
            {
                Character[] chars = symbols.toArray(new Character[0]);
                mfc.loadedMap = new LoadMap(path, chars);
                symbols = new ArrayList<>();
            }
            if (mfc.loadedMap.Map != null) {
                menu(null);
                mfc.makeItHappen();
            }
        }
    }

    /**
     * Enables/Disables Music and Music button in menu - settings.
     */
    private void musicButton_Click()
    {
        mfc.music = !mfc.music;
        if (mfc.music)
        {
            mfc.musicButtonLbl.setText("<html><div style='padding-left: 8px; background-color: black; width: 140px;'>MUSIC</div></html>");
            mfc.musicButtonLbl.setForeground(Color.white);
        }
        else
        {
            mfc.musicButtonLbl.setText("<html><div style='padding-left: 8px; background-color: gray; width: 140px;'>MUSIC</div></html>");
            mfc.musicButtonLbl.setForeground(Color.black);
        }
    }

    /**
     * Enables/Disables Sounds and Sounds button in menu - settings.
     */
    private void soundsButton_Click()
    {
        mfc.sound = !mfc.sound;
        if (mfc.sound)
        {
            mfc.soundsButtonLbl.setText("<html><div style='padding-left: 8px; background-color: black; width: 150px;'>SOUND</div></html>");
            mfc.soundsButtonLbl.setForeground(Color.white);
        }
        else
        {
            mfc.soundsButtonLbl.setText("<html><div style='padding-left: 8px; background-color: gray; width: 150px;'>SOUND</div></html>");
            mfc.soundsButtonLbl.setForeground(Color.black);
        }
    }

    /**
     * Simulates select map click and sets number of players to 2 in case of successfull map load.
     * @throws NoSuchMethodException exceptions are handled in parent.
     * @throws IllegalAccessException exceptions are handled in parent.
     * @throws InvocationTargetException exceptions are handled in parent.
     */
    private void vs_Click() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        selectMap_Click();
        mfc.player2 = true;
        if (!mfc.gameOn)
            mfc.player2 = false;
    }

    private void settings_Click() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        menuLayer = mn.submenu;
        menu(this.getClass().getDeclaredMethod("menu_Settings"));
    }

    private void highScr_Click() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        menuLayer = mn.submenu;
        menu(this.getClass().getDeclaredMethod("menu_HighScore"));
    }

    /**
     * Function to convert typed characters separated by ';' to string for output.
     * @param source Input characters.
     * @return String of characters separated with " ; ".
     */
    private String charListToString(ArrayList<Character> source)
    {
        StringBuilder output = new StringBuilder();
        if (source.size() > 0)
            output.append(source.get(0).toString());

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < source.size(); i++)
        {
            output.append(" ; ");
            output.append(source.get(i));
        }
        return output.toString();
    }

    /**
     * Changes selected element in menu.
     * @param delta Indicates whether to move up or down in menu (Values +1/-1).
     */
    private void moveInMenu(int delta)
    {
        final byte menuSize = 5;
        JLabel newLabel = enumToLabel(mn.values()[((menuSelected.item1.ordinal() + delta + menuSize) % menuSize)]);
        highlightSelected(menuSelected.item2, newLabel);
        menuSelected = new Pair<>(mn.values()[((menuSelected.item1.ordinal() + delta + menuSize) % menuSize)], newLabel);
    }

    /**
     * Changes selected element in settings.
     */
    private void moveInSettings()
    {
        if (mfc.soundsBtnSelectorLbl.isVisible())
            musicButton_MouseEnter();
        else
            soundsButton_MouseEnter();
    }

    /**
     * Selects SoundsButton when mouse cursor enters its domain.
     */
    private void soundsButton_MouseEnter()
    {
        mfc.soundsBtnSelectorLbl.setVisible(true);
        mfc.musicBtnSelectorLbl.setVisible(false);
    }

    /**
     * Selects MusicButton when mouse cursor enters its domain.
     */
    private void musicButton_MouseEnter()
    {
        mfc.soundsBtnSelectorLbl.setVisible(false);
        mfc.musicBtnSelectorLbl.setVisible(true);
    }

    /**
     * Function handling key pressing in menu.
     * @param keyCode int code of pressed key.
     */
    private void menuKeyDownHandler(int keyCode) {
        try {
            if (menuLayer == mn.start) {
                if (keyCode == KeyEvent.VK_ESCAPE)
                    mfc.mainFrame.dispose();
                else {
                    menu(this.getClass().getDeclaredMethod("menu_MainMenu"));
                    highlightSelected(menuSelected.item2, mfc.orgGameLbl);
                    menuSelected = new Pair<>(mn.game, mfc.orgGameLbl);
                    menuLayer = mn.game;
                }
            } else {
                if (keyCode == KeyEvent.VK_ESCAPE) {
                    // Escape returns you to menu form everywhere except from menu itself.
                    if (menuLayer == mn.submenu) {
                        if (menuSelected.item1 == mn.settings) {
                            mfc.musicBtnSelectorLbl.setVisible(false);
                            mfc.soundsBtnSelectorLbl.setVisible(false);
                        }
                        menu(this.getClass().getDeclaredMethod("menu_MainMenu"));
                        menuLayer = mn.game;
                    } else {
                        menuLayer = mn.start;
                        menu(this.getClass().getDeclaredMethod("menu_Start"));
                    }
                } else if (mfc.typedSymbolsLbl.isVisible()) {
                    // Branch accessible during typing of symbols used on Map to load.
                    if (keyCode == KeyEvent.VK_BACK_SPACE && symbols.size() > 0) {
                        symbols.remove(symbols.size() - 1);
                        mfc.typedSymbolsLbl.setText(charListToString(symbols));
                    }
                    if (!symbols.contains((char)keyCode) && keyCode != KeyEvent.VK_BACK_SPACE) {
                        symbols.add((char)keyCode);
                        mfc.typedSymbolsLbl.setText(charListToString(symbols));
                    }
                    mfc.mainFrame.revalidate();
                    final byte symbolsLimit = 5;
                    if (symbols.size() == symbolsLimit)
                        selectMap_Click();
                } else if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
                    if (menuLayer == mn.submenu && menuSelected.item1 == mn.settings)
                        moveInSettings();
                    else if (menuLayer == mn.game)
                        moveInMenu(-1);
                } else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
                    if (menuLayer == mn.submenu && menuSelected.item1 == mn.settings)
                        moveInSettings();
                    else if (menuLayer == mn.game)
                        moveInMenu(+1);
                } else if (keyCode == KeyEvent.VK_ENTER)
                    if (menuLayer == mn.game) {
                        Method action = enumToAction(menuSelected.item1);
                        if (action != null)
                            action.invoke(this);
                    } else if (menuSelected.item1 == mn.settings) {
                        if (mfc.musicBtnSelectorLbl.isVisible())
                            musicButton_Click();
                        else
                            soundsButton_Click();
                    }
            }
        }
        catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException exception){
            mfc.tryToSaveScore();
            mfc.handleExceptions(exception.toString());
        }
    }

    /**
     * Function that switches active labels by unhighlighting the old one and highlighting the new one.
     * @param prevLabel Previous active label.
     * @param newLabel New active label.
     */
    private void highlightSelected(JLabel prevLabel, JLabel newLabel)
    {
        if(prevLabel != null) {
            prevLabel.setForeground(Color.white);
            prevLabel.setFont(new Font(prevLabel.getFont().getFontName(), Font.BOLD, 34));
        }
        if(newLabel != null) {
            newLabel.setForeground(Color.yellow);
            newLabel.setFont(new Font(newLabel.getFont().getFontName(), Font.BOLD, 36));
        }
    }

    //</editor-fold>

    //<editor-fold desc="- KEY BINDINGS Block -">

    private void addKeyBindings(){
        KeyBindings kb = new KeyBindings(mfc.mainPanel, this);

        InputMap iMap = kb.getIMap();
        ActionMap aMap = kb.getAMap();
    }

    public void keyDownHandler(int key)
    {
        if(!mfc.gameOn)
            menuKeyDownHandler(key);
        else
            ;//gameKeyDownHandler(key);

        mfc.mainFrame.revalidate();
    }

    //</editor-fold>

    //<editor-fold desc="- MN enum -"

    /**
     * Enumeration for identifying menu states.
     */
    enum mn { game, selectmap, settings, vs, highscore, start, submenu, none };

    /**
     * Function that associates selected labels to menu states.
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
            case "Vs":
                return mn.vs;
            case "HighScr":
                return mn.highscore;
            case "Settings":
                return mn.settings;
            default:
                return mn.start;
        }
    }

    /**
     * Function that associates menu states to selected labels.
     * @param selected Menu state to be associated.
     * @return Resulting associated label (default: OrgGame).
     */
    private JLabel enumToLabel(mn selected)
    {
        switch(selected) {
            case game:
                return this.mfc.orgGameLbl;
            case selectmap:
                return this.mfc.selectMapLbl;
            case vs:
                return this.mfc.vsLbl;
            case highscore:
                return this.mfc.highScrLbl;
            case settings:
                return this.mfc.settingsLbl;
            default:
                return this.mfc.orgGameLbl;
        }
    }

    /**
     * Function that associates on-click functions to menu states.
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
            case vs:
                return this.getClass().getDeclaredMethod("vs_Click");
            case highscore:
                return this.getClass().getDeclaredMethod("highScr_Click");
            case settings:
                return this.getClass().getDeclaredMethod("settings_Click");
            default:
                return null;
        }
    }

    //</editor-fold>

    //<editor-fold desc="- LISTENERS Block -">

    /**
     * Handles events like window minimisation, window closing, atc...
     */
    private class windowListener implements WindowListener {

        /**
         * Method handling form's load request.
         * @param e WindowEvent
         */
        @Override
        public void windowOpened(WindowEvent e) {
            menuLayer = mn.start;
            menuSelected = new Pair<>(mn.game, mfc.orgGameLbl);
            try {
                menu(MenuController.class.getDeclaredMethod("menu_Start"));
            }
            catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException exception){
                mfc.tryToSaveScore();
                mfc.handleExceptions(exception.getMessage());
            }
        }

        /**
         * Method handling score saving and form disposing in case of application termination from outside the code.
         * @param e WindowEvent
         */
        @Override
        public void windowClosing(WindowEvent e){
            mfc.tryToSaveScore();
            mfc.mainFrame.dispose();
        }

        /**
         * Method handling score saving in case of from code exit (pressing esc/exception)
         * @param e WindowEvent
         */
        @Override
        public void windowClosed(WindowEvent e){
            mfc.tryToSaveScore();
        }

        /**
         * Method handling timers and music pausing on window minimisation.
         * @param e WindowEvent
         */
        @Override
        public void windowIconified(WindowEvent e) {
            mfc.pacUpdater.stop();
            mfc.pacSmoothTimer.stop();
            mfc.ghostUpdater.stop();
            mfc.ghostSmoothTimer.stop();
        }

        /**
         * Resumes timers and music and redraws game graphics after window minimisation.
         * @param e WindowEvent
         */
        @Override
        public void windowDeiconified(WindowEvent e) {
            if(mfc.gameOn){
                mfc.pacUpdater.start();
                mfc.pacSmoothTimer.start();
                mfc.ghostUpdater.start();
                mfc.ghostSmoothTimer.start();
            }
        }

        @Override
        public void windowActivated(WindowEvent e) { }

        @Override
        public void windowDeactivated(WindowEvent e) { }
    }

    private enum mouseAdapterType { highlight_menu, highlight_settings, clickToEnter, clickToEscape, tryAgainBtn, advancedLdBtn }

    private class labelListener extends MouseAdapter {
        private JLabel label;
        private mouseAdapterType mat;

        labelListener(JLabel label, mouseAdapterType mat){
            this.label = label;
            this.mat = mat;
        }

        /**
         * Function simulating enter/escape key push on label clicked.
         * @param e MouseEvent
         */
        @Override
        public void mouseClicked(MouseEvent e){
            try {
                switch (mat) {
                    case clickToEscape:
                        menuKeyDownHandler(KeyEvent.VK_ESCAPE);
                        break;
                    case tryAgainBtn:
                        selectMap_Click();
                        break;
                    case advancedLdBtn:
                        advancedLdBut_Click();
                        break;
                    default:
                        menuKeyDownHandler(KeyEvent.VK_ENTER);
                        break;
                }
            }
            catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException exception){
                mfc.tryToSaveScore();
                mfc.handleExceptions(exception.getMessage());
            }
        }

        /**
         * Function that provides color change of labels in menu on cursor entry of label's domain.
         * @param e MouseEvent
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            switch(mat){
                case highlight_settings:
                    if(label.getName().equals("MusicButton"))
                        musicButton_MouseEnter();
                    else
                        soundsButton_MouseEnter();
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
                    mfc.tryAgainButLbl.setText("<html><div style='background-color: yellow; width: 148px; padding-left: 5px'>TRY AGAIN</div><html>");
                    break;
                case advancedLdBtn:
                    mfc.advancedLdButLbl.setText("<html><div style='background-color: yellow; width: 230px; padding-left: 5px'>ADVANCED LOAD</div></html>");
                    break;
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            switch (mat){
                case clickToEscape:
                    label.setForeground(Color.yellow);
                    break;
                case clickToEnter:
                    label.setForeground(Color.white);
                    break;
                case tryAgainBtn:
                    mfc.tryAgainButLbl.setText("<html><div style='background-color: white; width: 148px; padding-left: 5px'>TRY AGAIN</div><html>");
                    break;
                case advancedLdBtn:
                    mfc.advancedLdButLbl.setText("<html><div style='background-color: white; width: 230px; padding-left: 5px'>ADVANCED LOAD</div></html>");
                    break;
            }

        }
    }

    //</editor-fold>
}
