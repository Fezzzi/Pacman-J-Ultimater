package pacman_ultimater.project_base.gui_swing.ui.controller;

import pacman_ultimater.project_base.core.HighScoreClass;
import pacman_ultimater.project_base.core.LoadMap;
import pacman_ultimater.project_base.core.Tile;
import pacman_ultimater.project_base.custom_utils.IntPair;
import pacman_ultimater.project_base.custom_utils.Quintet;
import pacman_ultimater.project_base.gui_swing.ui.view.MainFrame;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainFrameController {

    //<editor-fold desc="- VARIABLES Block -">

    int score, score2, lives, highScore, soundTick, collectedDots, ghostsEaten, ghostRelease,
        level, keyCountdown1, keyCountdown2, freeGhosts, ticks, eatEmTimer;
    JLabel scoreBox = new JLabel(), highScoreBox = new JLabel(), score2Box = new JLabel();
    boolean gameOn, player2, sound, music, extraLifeGiven, keyPressed1, keyPressed2, killed;
    String resourcesPath;
    Clip musicPlayer;
    Clip[] soundPlayers;
    Quintet<Tile[][], Integer, IntPair, Color, ArrayList<Point>> map;
    Tile[][] mapFresh;
    Point[] redrawPellets = new Point[LoadMap.RDPSIZE];
    LoadMap loadedMap;

    MainFrame mainFrame;
    JPanel mainPanel;
    JFileChooser openFileDialog1;
    Timer pacUpdater;
    Timer ghostUpdater;
    Timer pacSmoothTimer;
    Timer ghostSmoothTimer;
    JLabel pacmanLbl;
    JLabel ultimateLbl;
    JLabel copyrightLbl;
    JLabel pressEnterLbl;
    JLabel selectMapLbl;
    JLabel orgGameLbl;
    JLabel settingsLbl;
    JLabel escLabelLbl;
    JLabel highScrLbl;
    JLabel vsLbl;
    JLabel highScoreLabelLbl;
    JLabel scoreLabelLbl;
    JLabel highScoreNumLbl;
    JLabel scoreNumLbl;
    JLabel musicButtonLbl;
    JLabel soundsButtonLbl;
    JLabel musicBtnSelectorLbl;
    JLabel soundsBtnSelectorLbl;
    JLabel gameOverLabelLbl;
    JLabel errorLdMapLbl;
    JLabel errorInfoLbl;
    JLabel tryAgainButLbl;
    JLabel advancedLdButLbl;
    JLabel typeSymbolsLbl;
    JLabel typedSymbolsLbl;
    JLabel typeHintLbl;

    static final int SOUNDPLAYERSCOUNT = 6;
    static final int GHOSTFLASHINGSTART = 30;
    static final int MAXLEVEL = 256;

    //</editor-fold>

    //<editor-fold desc="- INITIALIZATION Block -">

    public MainFrameController(){
        initComponents();
        setInitialState();
    }

    /**
     * Sets initial state by setting default values to all variables.
     */
    private void setInitialState(){
        score = 0;
        score2 = 0;
        lives = 0;
        highScore = -1;
        gameOn = false;
        player2 = false;
        sound = true;
        music = true;
    }

    /**
     * Initializes components from form for further usage.
     */
    private void initComponents(){
        mainFrame = new MainFrame();
        mainPanel = mainFrame.getMainPanel();
        resourcesPath = mainFrame.getResourcesPath();
        openFileDialog1 = mainFrame.getOpenFileDialog1();
        ghostUpdater = mainFrame.getGhostUpdater();
        ghostSmoothTimer = mainFrame.getGhostSmoothTimer();
        pacUpdater = mainFrame.getPacUpdater();
        pacSmoothTimer = mainFrame.getPacSmoothTimer();

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
        highScoreLabelLbl = mainFrame.getHighScoreLabelLbl();
        scoreLabelLbl = mainFrame.getScoreLabelLbl();
        highScoreNumLbl = mainFrame.getHighScoreNumLbl();
        scoreNumLbl = mainFrame.getScoreNumLbl();
        musicButtonLbl = mainFrame.getMusicButtonLbl();
        soundsButtonLbl = mainFrame.getSoundsButtonLbl();
        musicBtnSelectorLbl = mainFrame.getMusicBtnSelectorLbl();
        soundsBtnSelectorLbl = mainFrame.getSoundsBtnSelectorLbl();
        gameOverLabelLbl = mainFrame.getGameOverLabelLbl();
        errorLdMapLbl = mainFrame.getErrorLdMapLbl();
        errorInfoLbl = mainFrame.getErrorInfoLbl();
        tryAgainButLbl = mainFrame.getTryAgainButLbl();
        advancedLdButLbl = mainFrame.getAdvancedLdButLbl();
        typeSymbolsLbl = mainFrame.getTypeSymbolsLbl();
        typedSymbolsLbl = mainFrame.getTypedSymbolsLbl();
        typeHintLbl = mainFrame.getTypeHintLbl();
    }

    //</editor-fold>

    /**
     * Creates new instance of menu - resulting in menu showing up
     */
    public void OpenMenu(){
        MenuController mc = new MenuController(this);
        mainFrame.setVisible(true);
        mainPanel.grabFocus();
    }

    /**
     * Function that provides bridge between menu and the game.
     * Switches input (KeyDown) to game mode by turning gameOn.
     * Initializes Map and calls function that makes the game start.
     */
    void makeItHappen()
    {
        try {
            level = 0;
            gameOn = true;
            map = loadedMap.Map;
            loadGame();
        }
        catch(IOException | LineUnavailableException | ExecutionException
                | UnsupportedAudioFileException | InterruptedException e)
        {
            handleExceptions(e.getMessage());
            mainFrame.dispose();
        }
    }

    private void loadGame()
            throws LineUnavailableException, IOException, ExecutionException, UnsupportedAudioFileException,
                    InterruptedException
    {
        GameLoadController glc = new GameLoadController(this);
        glc.playGame(false);
    }

    /**
     * Attempts to save player's score to file.
     */
    void tryToSaveScore(){
        try{
            if(!player2 && score > 0)
                HighScoreClass.saveHighScore(score, resourcesPath);
        }
        catch(IOException exception){
            handleExceptions(exception.getMessage());
        }
    }


    /**
     * Handles occured eception by displaying apology and exception message
     * @param message Exception message.
     */
    void handleExceptions(String message){
        mainPanel.removeAll();
        errorInfoLbl.setText("<html><div style='width: 100%; text-align: center; display: block;'>" +
                "<b>WE ARE SORRY</b><br />" +
                "<h1>something broke</h1><br /><h2 style='color: red'>"
                + message + "<br /><br /><br />" +
                "</h2><h1>The game will save current score and close.</h1></div></html>");
        errorInfoLbl.setVisible(true);
        mainPanel.add(errorInfoLbl);
        mainPanel.repaint();
        mainPanel.revalidate();
    }
}
