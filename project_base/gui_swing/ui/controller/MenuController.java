package pacman_ultimater.project_base.gui_swing.ui.controller;

import pacman_ultimater.project_base.core.HighScoreClass;
import pacman_ultimater.project_base.core.LoadMap;
import pacman_ultimater.project_base.custom_utils.Pair;
import pacman_ultimater.project_base.gui_swing.ui.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MenuController {
    private int score, score2, lives = 0;

    //<editor-fold desc="- VARIABLES Block -">

    private int highScore = -1;
    private boolean gameOn = false;
    private boolean player2 = false;
    private boolean sound = true, music = true;
    private ArrayList<JLabel> activeElements = new ArrayList<>();
    private Pair<MenuController.mn, JLabel> menuSelected;
    private MenuController.mn menuLayer;
    private JLabel scoreBox = new JLabel(), highScoreBox = new JLabel(), score2Box = new JLabel();
    private ArrayList<String> symbols = new ArrayList<>();

    private String resourcesPath;
    private MainFrame mainFrame;
    private JPanel mainPanel;
    private JFileChooser openFileDialog1;

    private JLabel pacmanLbl;
    private JLabel ultimateLbl;
    private JLabel copyrightLbl;
    private JLabel pressEnterLbl;
    private JLabel selectMapLbl;
    private JLabel orgGameLbl;
    private JLabel settingsLbl;
    private JLabel escLabelLbl;
    private JLabel highScrLbl;
    private JLabel vsLbl;
    private JLabel highScoreLabelLbl;
    private JLabel scoreLabelLbl;
    private JLabel highScoreNumLbl;
    private JLabel scoreNumLbl;
    private JLabel musicButtonLbl;
    private JLabel soundsButtonLbl;
    private JLabel musicBtnSelectorLbl;
    private JLabel soundsBtnSelectorLbl;
    private JLabel gameOverLabelLbl;
    private JLabel errorLdMapLbl;
    private JLabel errorInfoLbl;
    private JLabel tryAgainButLbl;
    private JLabel advancedLdButLbl;
    private JLabel typeSymbolsLbl;
    private JLabel typedSymbolsLbl;
    private JLabel typeHintLbl;

    //</editor-fold>

    //<editor-fold desc="- GENERAL Block -">

    MenuController(){
        initComponents();
        initListeners();
        addKeyBindings();

        mainFrame.setVisible(true);
        mainPanel.grabFocus();
    }

    /**
     * Initializes components from form for further usage.
     */
    private void initComponents(){
        mainFrame = new MainFrame();
        mainPanel = mainFrame.getMainPanel();
        resourcesPath = mainFrame.getResourcesPath();
        openFileDialog1 = mainFrame.getOpenFileDialog1();

        pacmanLbl = mainFrame.getPacmanLbl();
        ultimateLbl = mainFrame.getUltimateLbl();
        copyrightLbl = mainFrame.getCopyrightLbl();
        pressEnterLbl = mainFrame.getPressEnterLbl();
        selectMapLbl = mainFrame.getSelectMapLbl();
        orgGameLbl = mainFrame.getOrgGameLbl();
        settingsLbl = mainFrame.getSettingsLbl();
        escLabelLbl = mainFrame.getEscLabelLbl();
        highScrLbl = mainFrame.getHighScrLbl();
        vsLbl = mainFrame.getVsLbl();
//        highScoreLabelLbl = mainFrame.getHighScoreLabelLbl();
//        scoreLabelLbl = mainFrame.getScoreLabelLbl();
//        highScoreNumLbl = mainFrame.getHighScoreNumLbl();
//        scoreNumLbl = mainFrame.getScoreNumLbl();
//        musicButtonLbl = mainFrame.getMusicButtonLbl();
//        soundsButtonLbl = mainFrame.getSoundsButtonLbl();
//        musicBtnSelectorLbl = mainFrame.getMusicBtnSelectorLbl();
//        soundsBtnSelectorLbl = mainFrame.getSoundsBtnSelectorLbl();
//        gameOverLabelLbl = mainFrame.getGameOverLabelLbl();
//        errorLdMapLbl = mainFrame.getErrorLdMapLbl();
//        errorInfoLbl = mainFrame.getErrorInfoLbl();
//        tryAgainButLbl = mainFrame.getTryAgainButLbl();
//        advancedLdButLbl = mainFrame.getAdvancedLdButLbl();
//        typeSymbolsLbl = mainFrame.getTypeSymbolsLbl();
//        typedSymbolsLbl = mainFrame.getTypedSymbolsLbl();
//        typeHintLbl = mainFrame.getTypeHintLbl();
    }

    /**
     * Put listeners on selected labels and mainFrame.
     */
    private void initListeners(){
        mainFrame.addWindowListener(new windowListener());

        JLabel[] labels = {vsLbl, orgGameLbl, selectMapLbl, settingsLbl, highScrLbl};
        for(JLabel label : labels)
            label.addMouseListener(new labelListener(label));
    }

    //</editor-fold>

    //<editor-fold desc="- MENU Block -"

    /**
     * Function that makes Menu work by simple enabling and disabling visibility of selected controls.
     * @param Menu_Func Function to be called (Built-in delegate for void function that takes no parameters).
     * @throws IllegalAccessException should be handled once in parent method.
     * @throws InvocationTargetException should be handled once in parent method.
     */
    private void menu(Method Menu_Func) throws IllegalAccessException, InvocationTargetException
    {
        for (JLabel label : activeElements)
            label.setVisible(false);

        activeElements = new ArrayList<>();
        Menu_Func.invoke(this);

        for (JLabel label : activeElements)
            label.setVisible(true);
    }

    private void menu_Start()
    {
        activeElements.add(pressEnterLbl);
        activeElements.add(pacmanLbl);
        activeElements.add(ultimateLbl);
        activeElements.add(copyrightLbl);
    }

    private void menu_MainMenu()
    {
        activeElements.add(selectMapLbl);
        activeElements.add(orgGameLbl);
        activeElements.add(settingsLbl);
        activeElements.add(escLabelLbl);
        activeElements.add(highScrLbl);
        activeElements.add(vsLbl);
    }

    private void menu_SelectMap()
    {
        activeElements.add(errorLdMapLbl);
        activeElements.add(errorInfoLbl);
        activeElements.add(advancedLdButLbl);
        activeElements.add(tryAgainButLbl);
        activeElements.add(escLabelLbl);
    }

    private void menu_HighScore1P() throws IOException
    {
        if (score != 0)
        {
            gameOverLabelLbl.setText("GAME OVER");
            gameOverLabelLbl.setForeground(Color.red);
            gameOverLabelLbl.setLocation(52, 33);
            scoreLabelLbl.setText("Your Score");
            scoreNumLbl.setText(Integer.toString(score));
        }
        else
        {
            scoreLabelLbl.setText("");
            scoreNumLbl.setText("");
            gameOverLabelLbl.setText("");
        }
        if (highScore == -1)
        {
            //In case the HighScore is not loaded yet (value is -1) do so
            highScore = HighScoreClass.loadHighScore();
        }
        highScoreLabelLbl.setText("Highest Score");
        highScoreLabelLbl.setForeground(Color.yellow);
        highScoreNumLbl.setForeground(Color.yellow);
        highScoreNumLbl.setText(Integer.toString(highScore));
    }

    private void menu_HighScore2P()
    {
        //Game selects the winner as the pleyer with highest score
        //In case of tie chooses the winner by remaining pacman lives
        if (score < score2 || (score == score2 && lives <= 0))
        {
            gameOverLabelLbl.setText("GHOSTS WIN");
            gameOverLabelLbl.setForeground(Color.red);
            gameOverLabelLbl.setLocation(36, 33);
            highScoreLabelLbl.setText("2UP");
            highScoreLabelLbl.setForeground(Color.red);
            highScoreNumLbl.setText(Integer.toString(score2));
            highScoreNumLbl.setForeground(Color.red);
            scoreLabelLbl.setText("1UP");
            scoreNumLbl.setText(Integer.toString(score));
        }
        else
        {
            gameOverLabelLbl.setText("PACMAN WINS");
            gameOverLabelLbl.setForeground(Color.yellow);
            gameOverLabelLbl.setLocation(34, 33);
            highScoreLabelLbl.setText("1UP");
            highScoreNumLbl.setText(Integer.toString(score));
            scoreLabelLbl.setText("2UP");
            scoreNumLbl.setText(Integer.toString(score2));
        }
    }

    private void menu_HighScore() throws IOException
    {
        activeElements.add(gameOverLabelLbl);
        activeElements.add(scoreLabelLbl);
        activeElements.add(scoreNumLbl);
        activeElements.add(highScoreLabelLbl);
        activeElements.add(highScoreNumLbl);
        activeElements.add(escLabelLbl);

        //Two branches depending on the mode player has chosen - normal x VS
        if (!player2)
            menu_HighScore1P();
        else
            menu_HighScore2P();

        //It is necessary to set scre and player boolean in order to be able to access default Highscore
        //page later on from menu
        score = 0;
        player2 = false;
    }

    private void menu_Settings()
    {
        activeElements.add(musicButtonLbl);
        activeElements.add(soundsButtonLbl);
        activeElements.add(soundsBtnSelectorLbl);
        activeElements.add(musicBtnSelectorLbl);
        activeElements.add(escLabelLbl);

        // Buttons load with color depending on associated booleans.
        // Music button is by default selected by arrows.
        if (music)
            musicButtonLbl.setBackground(Color.black);
        else
            musicButtonLbl.setBackground(Color.gray);

        if (sound)
            soundsButtonLbl.setBackground(Color.black);
        else
            soundsButtonLbl.setBackground(Color.gray);
    }

    /**
     * Loads predefined original map and calls makeItHappen() to procced to gameplay.
     */
    private void orgGame_Click()
    {
        LoadMap loadMap = new LoadMap(resourcesPath + "\\OriginalMap.txt");
        if (loadMap.Map != null)
            ;//makeItHappen(loadMap);
    }

    /**
     * Opens menu for advanced map loading.
     */
    private void advancedLdBut_Click()
    {
        menuLayer = mn.submenu;
        activeElements.add(typeSymbolsLbl);
        activeElements.add(typedSymbolsLbl);
        typedSymbolsLbl.setText("");
        activeElements.add(typeHintLbl);
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
        if (openFileDialog1.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION)
        {
            menuLayer = mn.submenu;
            menu(this.getClass().getDeclaredMethod("menu_SelectMap"));
            String path = openFileDialog1.getName();
            LoadMap loadMap;
            if (symbols.size() == 0)
                loadMap = new LoadMap(path);
            else
            {
                Character[] chars = symbols.toArray(new Character[0]);
                loadMap = new LoadMap(path, symbols.toArray(chars));
                symbols = new ArrayList<>();
            }
            if (loadMap.Map != null)
                ;//makeItHappen(loadMap);
        }
    }

    /**
     * Enables/Disables Music and Music button in menu - settings.
     */
    private void musicButton_Click()
    {
        music = !music;
        if (music)
        {
            musicButtonLbl.setBackground(Color.black);
            musicButtonLbl.setForeground(Color.white);
        }
        else
        {
            musicButtonLbl.setBackground(Color.gray);
            musicButtonLbl.setForeground(Color.black);
        }
    }

    /**
     * Enables/Disables Sounds and Sounds button in menu - settings.
     */
    private void soundsButton_Click()
    {
        sound = !sound;
        if (sound)
        {
            soundsButtonLbl.setBackground(Color.black);
            soundsButtonLbl.setForeground(Color.white);
        }
        else
        {
            soundsButtonLbl.setBackground(Color.gray);
            soundsButtonLbl.setForeground(Color.black);
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
        player2 = true;
        selectMap_Click();
        if (!gameOn)
            player2 = false;
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
    private String charListToString(ArrayList<String> source)
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
        if (soundsBtnSelectorLbl.getBackground() == Color.yellow)
            musicButton_MouseEnter();
        else
            soundsButton_MouseEnter();
    }

    /**
     * Selects SoundsButton when mouse cursor enters its domain.
     */
    private void soundsButton_MouseEnter()
    {
        soundsBtnSelectorLbl.setBackground(Color.yellow);
        musicBtnSelectorLbl.setBackground(Color.black);
    }

    /**
     * Selects MusicButton when mouse cursor enters its domain.
     */
    private void musicButton_MouseEnter()
    {
        soundsBtnSelectorLbl.setBackground(Color.black);
        musicBtnSelectorLbl.setBackground(Color.yellow);
    }

    /**
     * Function handling key pressing in menu.
     * @param keyCode int code of pressed key.
     * @param keyChar string representing pressed key.
     */
    private void menuKeyDownHandler(int keyCode, String keyChar)
    {
        try {
            if (menuLayer == mn.start) {
                if (keyCode == KeyEvent.VK_ESCAPE)
                    mainFrame.dispose();
                else {
                    menu(this.getClass().getDeclaredMethod("menu_MainMenu"));
                    highlightSelected(menuSelected.item2, orgGameLbl);
                    menuSelected = new Pair<>(mn.game, orgGameLbl);
                    menuLayer = mn.game;
                }
            } else {
                if (keyCode == KeyEvent.VK_ESCAPE) {
                    // Escape returns you to menu form everywhere except from menu itself.
                    if (menuLayer == mn.submenu) {
                        menu(this.getClass().getDeclaredMethod("menu_MainMenu"));
                        highlightSelected(menuSelected.item2, orgGameLbl);
                        menuSelected = new Pair<>(mn.game, orgGameLbl);
                        menuLayer = mn.game;
                    } else {
                        menuLayer = mn.start;
                        menu(this.getClass().getDeclaredMethod("menu_Start"));
                    }
                } else if (typedSymbolsLbl.isVisible()) {
                    // Branch accessible during typing of symbols used on Map to load.
                    if (keyCode == KeyEvent.VK_BACK_SPACE && symbols.size() > 0) {
                        symbols.remove(symbols.size() - 1);
                        typedSymbolsLbl.setText(charListToString(symbols));
                    }
                    if (!symbols.contains(keyChar) && keyCode != KeyEvent.VK_BACK_SPACE) {
                        symbols.add(keyChar);
                        typedSymbolsLbl.setText(charListToString(symbols));
                    }
                    mainFrame.revalidate();
                    final byte symbolsLimit = 5;
                    if (symbols.size() == symbolsLimit)
                        selectMap_Click();
                } else if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
                    if (menuLayer == mn.submenu && menuSelected.item1 == mn.settings)
                        moveInSettings();
                    else
                        moveInMenu(-1);
                } else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
                    if (menuLayer == mn.submenu && menuSelected.item1 == mn.settings)
                        moveInSettings();
                    else
                        moveInMenu(+1);
                } else if (keyCode == KeyEvent.VK_ENTER)
                    if (menuLayer == mn.game) {
                        Method action = enumToAction(menuSelected.item1);
                        if (action != null)
                            action.invoke(this);
                    } else if (menuSelected.item1 == mn.settings) {
                        if (musicBtnSelectorLbl.getBackground() == Color.yellow)
                            musicButton_Click();
                        else
                            soundsButton_Click();
                    }
            }
        }
        catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException ignore){
            String exp = "exp";
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
            prevLabel.setFont(new Font(prevLabel.getFont().getFontName(), Font.BOLD, 21));
        }
        if(newLabel != null) {
            newLabel.setForeground(Color.yellow);
            newLabel.setFont(new Font(newLabel.getFont().getFontName(), Font.BOLD, 23));
        }
    }

    //</editor-fold>

    //<editor-fold desc="- KEY BINDINGS Block -">

    private void addKeyBindings(){
        InputMap iMap = mainPanel.getInputMap();
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "w_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "d_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "s_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "a_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "esc_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "back_key");
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter_key");

        ActionMap aMap = mainPanel.getActionMap();
        aMap.put("up_key", key_pressed_UP);
        aMap.put("right_key", key_pressed_RIGHT);
        aMap.put("down_key", key_pressed_DOWN);
        aMap.put("left_key", key_pressed_LEFT);
        aMap.put("w_key", key_pressed_W);
        aMap.put("d_key", key_pressed_D);
        aMap.put("s_key", key_pressed_S);
        aMap.put("a_key", key_pressed_A);
        aMap.put("esc_key", key_pressed_ESC);
        aMap.put("back_key", key_pressed_BACK);
        aMap.put("enter_key", key_pressed_ENTER);
    }

    private Action key_pressed_UP = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_UP, "up"); }
    };
    private Action key_pressed_RIGHT = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_RIGHT, "right"); }
    };
    private Action key_pressed_DOWN = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_DOWN, "down"); }
    };
    private Action key_pressed_LEFT = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_LEFT, "left"); }
    };
    private Action key_pressed_W = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_W, "W"); }
    };
    private Action key_pressed_D = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_D, "D"); }
    };
    private Action key_pressed_S = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_S, "S"); }
    };
    private Action key_pressed_A = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_A, "A"); }
    };
    private Action key_pressed_ENTER = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_ENTER, "enter"); }
    };
    private Action key_pressed_BACK = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_BACK_SPACE, "back"); }
    };
    private Action key_pressed_ESC = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) { keyDownHandler(KeyEvent.VK_ESCAPE, "esc"); }
    };

    private void keyDownHandler(int key, String str){
        if(!gameOn)
            menuKeyDownHandler(key, str);
        else
            ;//gameKeyDownHandler(key, str);

        mainFrame.revalidate();
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
            case "orgGame":
                return mn.game;
            case "selectMap":
                return mn.selectmap;
            case "vs":
                return mn.vs;
            case "highScr":
                return mn.highscore;
            case "settings":
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
                return this.orgGameLbl;
            case selectmap:
                return this.selectMapLbl;
            case vs:
                return this.vsLbl;
            case highscore:
                return this.highScrLbl;
            case settings:
                return this.settingsLbl;
            default:
                return this.orgGameLbl;
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
        public void windowOpened(WindowEvent e){
            menuLayer = mn.start;
            menuSelected = new Pair<>(mn.game, orgGameLbl);
            try {
                menu(MenuController.class.getDeclaredMethod("menu_Start"));
            }
            catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException ex){
                windowClosing(e);
            }
        }

        /**
         * Method handling score saving and form disposing in case of application termination from outside the code.
         * @param e WindowEvent
         */
        @Override
        public void windowClosing(WindowEvent e){
            try{
                if(!player2 && score > 0)
                    HighScoreClass.saveHighScore(score);
            }
            catch(IOException ignore){}

            mainFrame.dispose();
        }

        /**
         * Method handling score saving in case of from code exit (pressing esc/exception)
         * @param e WindowEvent
         */
        @Override
        public void windowClosed(WindowEvent e){
            try{
                if(!player2 && score > 0)
                    HighScoreClass.saveHighScore(score);
            }
            catch(IOException ignore){}
        }

        /**
         * Method handling timers and music pausing on window minimisation.
         * @param e WindowEvent
         */
        @Override
        public void windowIconified(WindowEvent e) {
            String pauseGame = "to do";
        }

        /**
         * Resumes timers and music and redraws game graphics after window minimisation.
         * @param e WindowEvent
         */
        @Override
        public void windowDeiconified(WindowEvent e) {
            String resumeGame = "to do";

            if(gameOn)
                ;//bg.Render(g);
        }

        @Override
        public void windowActivated(WindowEvent e) { }

        @Override
        public void windowDeactivated(WindowEvent e) { }
    }

    private class labelListener extends MouseAdapter {
        private JLabel label;

        public labelListener(JLabel label){
            this.label = label;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            ;
        }

        /**
         * Function that provides color change of labels in menu on cursor entry of label's domain.
         * @param e MouseEvent
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            highlightSelected(menuSelected.item2, label);
            menuSelected = new Pair<>(labelToEnum(label), label);
        }

        /**
         * Function that provides color change of labels in menu on cursor exit of label's domain.
         * @param e MouseEvent
         */
        @Override
        public void mouseExited(MouseEvent e) {
            highlightSelected(menuSelected.item2, null);
            menuSelected = new Pair<>(mn.none, null);
        }
    }

    //</editor-fold>
}
