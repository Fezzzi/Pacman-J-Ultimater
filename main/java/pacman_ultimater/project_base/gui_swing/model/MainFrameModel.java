package pacman_ultimater.project_base.gui_swing.model;

import pacman_ultimater.project_base.core.HighScoreClass;
import pacman_ultimater.project_base.gui_swing.ui.view.MainFrame;

import javax.swing.*;
import java.io.IOException;

public class MainFrameModel {

    public String resourcesPath;

    public MainFrame mainFrame;
    public JPanel mainPanel;
    public JFileChooser openFileDialog1;
    public Timer pacUpdater;
    public Timer ghostUpdater;
    public Timer pacSmoothTimer;
    public Timer ghostSmoothTimer;
    public JLabel pacmanLbl;
    public JLabel ultimateLbl;
    public JLabel copyrightLbl;
    public JLabel pressEnterLbl;
    public JLabel selectMapLbl;
    public JLabel orgGameLbl;
    public JLabel settingsLbl;
    public JLabel escLabelLbl;
    public JLabel highScrLbl;
    public JLabel vsLbl;
    public JLabel highScoreLabelLbl;
    public JLabel scoreLabelLbl;
    public JLabel highScoreNumLbl;
    public JLabel scoreNumLbl;
    public JLabel musicButtonLbl;
    public JLabel soundsButtonLbl;
    public JLabel musicBtnSelectorLbl;
    public JLabel soundsBtnSelectorLbl;
    public JLabel gameOverLabelLbl;
    public JLabel errorLdMapLbl;
    public JLabel errorInfoLbl;
    public JLabel tryAgainButLbl;
    public JLabel advancedLdButLbl;
    public JLabel typeSymbolsLbl;
    public JLabel typedSymbolsLbl;
    public JLabel typeHintLbl;

    public MainFrameModel(MainFrame view){
        mainFrame = view;
        mainPanel = view.getMainPanel();
        resourcesPath = view.getResourcesPath();
        openFileDialog1 = view.getOpenFileDialog1();
        ghostUpdater = view.getGhostUpdater();
        ghostSmoothTimer = view.getGhostSmoothTimer();
        pacUpdater = view.getPacUpdater();
        pacSmoothTimer = view.getPacSmoothTimer();

        pacmanLbl = view.getPacmanLbl();
        ultimateLbl = view.getUltimateLbl();
        copyrightLbl = view.getCopyrightLbl();
        pressEnterLbl = view.getPressEnterLbl();
        selectMapLbl = view.getSelectMapLbl();
        orgGameLbl = view.getOrgGameLbl();
        settingsLbl = view.getSettingsLbl();
        escLabelLbl = view.getEscLabelLbl();
        highScrLbl = view.getHighScrLbl();
        vsLbl = view.getVsLbl();
        highScoreLabelLbl = view.getHighScoreLabelLbl();
        scoreLabelLbl = view.getScoreLabelLbl();
        highScoreNumLbl = view.getHighScoreNumLbl();
        scoreNumLbl = view.getScoreNumLbl();
        musicButtonLbl = view.getMusicButtonLbl();
        soundsButtonLbl = view.getSoundsButtonLbl();
        musicBtnSelectorLbl = view.getMusicBtnSelectorLbl();
        soundsBtnSelectorLbl = view.getSoundsBtnSelectorLbl();
        gameOverLabelLbl = view.getGameOverLabelLbl();
        errorLdMapLbl = view.getErrorLdMapLbl();
        errorInfoLbl = view.getErrorInfoLbl();
        tryAgainButLbl = view.getTryAgainButLbl();
        advancedLdButLbl = view.getAdvancedLdButLbl();
        typeSymbolsLbl = view.getTypeSymbolsLbl();
        typedSymbolsLbl = view.getTypedSymbolsLbl();
        typeHintLbl = view.getTypeHintLbl();
    }

    /**
     * Handles occurred exception by displaying apology and exception message
     * @param message Exception message.
     */
    public void handleExceptions(String message){
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
