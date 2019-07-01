package pacman_ultimater.project_base.gui_swing.model;

import pacman_ultimater.project_base.gui_swing.ui.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class acting as application's model.
 * Holds together all of application's original labels.
 * Provides several methods for controllers' usage.
 */
public class MainFrameModel
{
    private MainFrame mainFrame;
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
    public JLabel multiplayerLbl;
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
    public JLabel editorLbl;
    public JLabel howToLbl;
    public JLabel editExistingButLbl;
    public JLabel editBtnSelectorLbl;
    public JLabel createNewButLbl;
    public JLabel createBtnSelectorLbl;

    public JLabel[] labels;
    public Map<String, Rectangle> defPositions;

    public MainFrameModel(MainFrame view)
    {
        mainFrame = view;
        mainPanel = view.getMainPanel();
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
        multiplayerLbl = view.getMultiplayerLbl();
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
        editorLbl = view.getEditorLbl();
        howToLbl = view.getHowToLbl();
        editExistingButLbl = view.getEditExistingButLbl();
        editBtnSelectorLbl = view.getEditBtnSelectorLbl();
        createNewButLbl = view.getCreateNewButLbl();
        createBtnSelectorLbl = view.getCreateBtnSelectorLbl();

        labels = new JLabel[]{
            pacmanLbl, ultimateLbl, copyrightLbl, pressEnterLbl, selectMapLbl, orgGameLbl, settingsLbl, escLabelLbl,
            highScrLbl, multiplayerLbl, highScoreLabelLbl, scoreLabelLbl, highScoreNumLbl, scoreNumLbl,
            musicButtonLbl, soundsButtonLbl, musicBtnSelectorLbl, soundsBtnSelectorLbl,
            gameOverLabelLbl, errorLdMapLbl, errorInfoLbl, tryAgainButLbl,
            advancedLdButLbl, typeSymbolsLbl, typedSymbolsLbl, typeHintLbl, editorLbl, howToLbl,
            editExistingButLbl, editBtnSelectorLbl, createNewButLbl, createBtnSelectorLbl,
        };

        defPositions = new HashMap<>();
        for (JLabel label : labels) {
            defPositions.put(label.getName(), label.getBounds());
        }
    }

    /**
     * Centers main Frame on the screenm
     *
     * @param size Current window size
     */
    public void recenterMainFrame(Dimension size)
    {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation(dim.width/2 - size.width/2, dim.height/2 - size.height/2);
    }

    /**
     * Disposes mainFrame resulting in whole app closeup.
     */
    public void disposeMainFrame()
    {
        mainFrame.dispose();
    }

    /**
     * Changes window's size.
     *
     * @param newSize Dimension to be set
     */
    public void setMainFrameSize(Dimension newSize)
    {
        if (newSize.width >= mainFrame.getMinimumSize().width && newSize.height >= mainFrame.getMinimumSize().height) {
            mainFrame.setSize(newSize);
            mainFrame.setMaximumSize(newSize);
        }
    }
}
