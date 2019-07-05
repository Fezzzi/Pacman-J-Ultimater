package pacman_ultimater.project_base.gui_swing.model;

import pacman_ultimater.project_base.custom_utils.Pair;
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
    private final MainFrame mainFrame;
    public final JPanel mainPanel;
    public final JFileChooser openFileDialog1;
    public Timer pacUpdater;
    public Timer ghostUpdater;
    public Timer pacSmoothTimer;
    public Timer ghostSmoothTimer;
    public final JLabel pacmanLbl;
    public final JLabel ultimateLbl;
    public final JLabel copyrightLbl;
    public final JLabel pressEnterLbl;
    public final JLabel selectMapLbl;
    public final JLabel orgGameLbl;
    public final JLabel settingsLbl;
    public final JLabel escLabelLbl;
    public final JLabel highScrLbl;
    public final JLabel multiplayerLbl;
    public final JLabel highScoreLabelLbl;
    public final JLabel scoreLabelLbl;
    public final JLabel highScoreNumLbl;
    public final JLabel scoreNumLbl;
    public final JLabel musicButtonLbl;
    public final JLabel soundsButtonLbl;
    public final JLabel musicBtnSelectorLbl;
    public final JLabel soundsBtnSelectorLbl;
    public final JLabel gameOverLabelLbl;
    public final JLabel errorLdMapLbl;
    public final JLabel errorInfoLbl;
    public final JLabel tryAgainButLbl;
    public final JLabel advancedLdButLbl;
    public final JLabel typeSymbolsLbl;
    public final JLabel typedSymbolsLbl;
    public final JLabel typeHintLbl;
    public final JLabel editorLbl;
    public final JLabel aboutLbl;
    public final JLabel editExistingButLbl;
    public final JLabel editBtnSelectorLbl;
    public final JLabel createNewButLbl;
    public final JLabel createBtnSelectorLbl;
    public final JLabel P2ButLbl;
    public final JLabel P2ButSelectorLbl;
    public final JLabel P3ButLbl;
    public final JLabel P3ButSelectorLbl;
    public final JLabel P4ButLbl;
    public final JLabel P4ButSelectorLbl;
    public final JLabel aboutHeadlineLbl;
    public final JLabel[] aboutCharacterLbls;
    public final JLabel[] aboutPelletLbls;
    public final JLabel aboutCopyLbl;


    public final JLabel[] labels;
    public final Map<String, Pair<Rectangle, Integer>> defLabelData;

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
        aboutLbl = view.getAboutLbl();
        editExistingButLbl = view.getEditExistingButLbl();
        editBtnSelectorLbl = view.getEditBtnSelectorLbl();
        createNewButLbl = view.getCreateNewButLbl();
        createBtnSelectorLbl = view.getCreateBtnSelectorLbl();
        P2ButLbl = view.getP2ButLbl();
        P2ButSelectorLbl = view.getP2ButSelectorLbl();
        P3ButLbl = view.getP3ButLbl();
        P3ButSelectorLbl = view.getP3ButSelectorLbl();
        P4ButLbl = view.getP4ButLbl();
        P4ButSelectorLbl = view.getP4ButSelectorLbl();
        aboutHeadlineLbl = view.getAboutHeadlineLbl();
        aboutCharacterLbls = view.getAboutCharacterLbls();
        aboutPelletLbls = view.getAboutPelletLbls();
        aboutCopyLbl = view.getAboutCopyLbl();

        labels = new JLabel[]{
            pacmanLbl, ultimateLbl, copyrightLbl, pressEnterLbl, selectMapLbl, orgGameLbl, settingsLbl, escLabelLbl,
            highScrLbl, multiplayerLbl, highScoreLabelLbl, scoreLabelLbl, highScoreNumLbl, scoreNumLbl,
            musicButtonLbl, soundsButtonLbl, musicBtnSelectorLbl, soundsBtnSelectorLbl,
            gameOverLabelLbl, errorLdMapLbl, errorInfoLbl, tryAgainButLbl,
            advancedLdButLbl, typeSymbolsLbl, typedSymbolsLbl, typeHintLbl, editorLbl, aboutLbl,
            editExistingButLbl, editBtnSelectorLbl, createNewButLbl, createBtnSelectorLbl, P2ButLbl, P2ButSelectorLbl,
            P3ButLbl, P3ButSelectorLbl, P4ButLbl, P4ButSelectorLbl, aboutHeadlineLbl, aboutCopyLbl,
            aboutCharacterLbls[0], aboutCharacterLbls[1], aboutCharacterLbls[2], aboutCharacterLbls[3], aboutPelletLbls[0], aboutPelletLbls[1]
        };

        defLabelData = new HashMap<>();
        for (JLabel label : labels) {
            defLabelData.put(label.getName(), new Pair<>(label.getBounds(), label.getFont().getSize()));
        }
    }

    /**
     * Disposes mainFrame resulting in whole app closeup.
     */
    public void disposeMainFrame()
    {
        mainFrame.dispose();
    }
}
