package pacman_ultimater.project_base.gui_swing.ui.view;

import pacman_ultimater.project_base.core.ClasspathFileReader;
import pacman_ultimater.project_base.core.LoadMap;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Application's view.
 * Creates application's window and fills it with labels used for menu orientation.
 */
public class MainFrame extends JFrame{

    //<editor-fold desc="- FRAME COMPONENTS Block -">

    private JPanel mainPanel;
    private JFileChooser openFileDialog1;

    private Timer pacUpdater;
    private Timer ghostUpdater;
    private Timer pacSmoothTimer;
    private Timer ghostSmoothTimer;

    private JLabel pacmanLbl;
    private JLabel ultimateLbl;
    private JLabel copyrightLbl;
    private JLabel pressEnterLbl;
    private JLabel selectMapLbl;
    private JLabel orgGameLbl;
    private JLabel settingsLbl;
    private JLabel escLabelLbl;
    private JLabel highScrLbl;
    private JLabel multiplayerLbl;
    private JLabel highScoreLabelLbl;
    private JLabel scoreLabelLbl;
    private JLabel highScoreNumLbl;
    private JLabel scoreNumLbl;
    private JLabel musicButtonLbl, musicBtnSelectorLbl;
    private JLabel soundsButtonLbl, soundsBtnSelectorLbl;
    private JLabel gameOverLabelLbl;
    private JLabel errorLdMapLbl;
    private JLabel errorInfoLbl;
    private JLabel tryAgainButLbl;
    private JLabel advancedLdButLbl;
    private JLabel typeSymbolsLbl;
    private JLabel typedSymbolsLbl;
    private JLabel typeHintLbl;
    private JLabel editorLbl;
    private JLabel aboutLbl;
    private JLabel editExistingButLbl, editBtnSelectorLbl;
    private JLabel createNewButLbl, createBtnSelectorLbl;
    private JLabel P2ButLbl, P2ButSelectorLbl;
    private JLabel P3ButLbl, P3ButSelectorLbl;
    private JLabel P4ButLbl, P4ButSelectorLbl;
    private JLabel aboutHeadlineLbl;
    private JLabel[] aboutCharacterLbls;
    private JLabel[] aboutPelletLbls;
    private JLabel aboutCopyLbl;

    //</editor-fold>

    //<editor-fold desc="- INITIALIZATION Block -">

    /**
     * @throws IOException To be handled by caller.
     */
    public MainFrame()
            throws IOException
    {
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(ClasspathFileReader.getICON().readAllBytes()).getImage());
        setMinimumSize(new Dimension(LoadMap.DEFAULTWIDTH, LoadMap.DEFAULTHEIGHT));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2 - this.getSize().width/2, dim.height/2 - this.getSize().height/2);
        setName("PacManJUltimater");
        setTitle("Pac-Man J-Ultimater");

        UIManager.put("Panel.background", Color.white);
        UIManager.put("OptionPane.background", Color.black);
        UIManager.put("OptionPane.messageForeground", Color.white);

        initComponents();
        addComponents();

        setContentPane(mainPanel);
        mainPanel.revalidate();
    }

    private void initComponents()
    {
        instantiateComponents();
        //
        // Constants
        //
        int menuLabelsX = (LoadMap.DEFAULTWIDTH - 300) / 2;
        int prefaceLabelsX = (LoadMap.DEFAULTWIDTH - 400) / 2;
        //
        // Main Frame
        //
        this.setFocusable(false);
        //
        // Main Panel
        //
        mainPanel.setLayout(null);
        mainPanel.setBackground(SystemColor.BLACK);
        mainPanel.setFocusable(true);
        //
        // Open File Dialog
        //
        openFileDialog1.setDialogTitle("Choose Map File");
        openFileDialog1.setFileFilter(new FileFilter() {
            public boolean accept(File dir) {
                if(dir.isFile())
                    return dir.getName().toLowerCase().endsWith(".txt");
                return true;
            }
            public String getDescription(){
                return "";
            }
        });
        openFileDialog1.setFocusable(false);
        //
        // Pacman
        //
        pacmanLbl.setFont(ClasspathFileReader.getFONT().deriveFont(Font.BOLD, 54));
        pacmanLbl.setForeground(Color.YELLOW);
        pacmanLbl.setBounds(prefaceLabelsX, 59,400,75);
        pacmanLbl.setName("pacman");
        pacmanLbl.setText("PAC-MAN");
        pacmanLbl.setVisible(false);
        pacmanLbl.setFocusable(false);
        pacmanLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // J-Ultimater
        //
        ultimateLbl.setFont(ClasspathFileReader.getFONT().deriveFont(Font.BOLD, 32));
        ultimateLbl.setForeground(Color.red);
        ultimateLbl.setBounds(prefaceLabelsX, 136,400,50);
        ultimateLbl.setName("jultimater");
        ultimateLbl.setText("- J-ULTIMATER -");
        ultimateLbl.setVisible(false);
        ultimateLbl.setFocusable(false);
        ultimateLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // copyright
        //
        copyrightLbl.setForeground(Color.yellow);
        copyrightLbl.setBounds(prefaceLabelsX, 519, 400,17);
        copyrightLbl.setName("copyright");
        copyrightLbl.setText("By Filip Horký, 2019");
        copyrightLbl.setVisible(false);
        copyrightLbl.setFocusable(false);
        copyrightLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        //
        // PressEnter
        //
        pressEnterLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 34));
        pressEnterLbl.setForeground(Color.white);
        pressEnterLbl.setBounds(prefaceLabelsX, 212,400,200);
        pressEnterLbl.setName("pressEnter");
        pressEnterLbl.setText("<html>&nbsp;&nbsp;INSERT COIN<br/><br/>- press any key -</html>");
        pressEnterLbl.setVisible(false);
        pressEnterLbl.setFocusable(false);
        pressEnterLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // OrgGame
        //
        orgGameLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 32));
        orgGameLbl.setForeground(Color.white);
        orgGameLbl.setBounds(menuLabelsX,35,300,45);
        orgGameLbl.setName("OrgGame");
        orgGameLbl.setText("Original Game");
        orgGameLbl.setVisible(false);
        orgGameLbl.setFocusable(false);
        orgGameLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // selectMap
        //
        selectMapLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 32));
        selectMapLbl.setForeground(Color.white);
        selectMapLbl.setBounds(menuLabelsX,97,300,45);
        selectMapLbl.setName("SelectMap");
        selectMapLbl.setText("Select Map");
        selectMapLbl.setVisible(false);
        selectMapLbl.setFocusable(false);
        selectMapLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // Multiplayer
        //
        multiplayerLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 32));
        multiplayerLbl.setForeground(Color.white);
        multiplayerLbl.setBounds(menuLabelsX,160,300,45);
        multiplayerLbl.setName("Multiplayer");
        multiplayerLbl.setText("Multiplayer");
        multiplayerLbl.setVisible(false);
        multiplayerLbl.setFocusable(false);
        multiplayerLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // Settings
        //
        settingsLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 32));
        settingsLbl.setForeground(Color.white);
        settingsLbl.setBounds(menuLabelsX,224,300,45);
        settingsLbl.setName("Settings");
        settingsLbl.setText("Settings");
        settingsLbl.setVisible(false);
        settingsLbl.setFocusable(false);
        settingsLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // Editor
        //
        editorLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 32));
        editorLbl.setForeground(Color.white);
        editorLbl.setBounds(menuLabelsX,287,300,45);
        editorLbl.setName("Editor");
        editorLbl.setText("Editor");
        editorLbl.setVisible(false);
        editorLbl.setFocusable(false);
        editorLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // How To
        //
        aboutLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 32));
        aboutLbl.setForeground(Color.white);
        aboutLbl.setBounds(menuLabelsX,349,300,45);
        aboutLbl.setName("About");
        aboutLbl.setText("About");
        aboutLbl.setVisible(false);
        aboutLbl.setFocusable(false);
        aboutLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // HighScr
        //
        highScrLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 32));
        highScrLbl.setForeground(Color.white);
        highScrLbl.setBounds(menuLabelsX,413,300,45);
        highScrLbl.setName("HighScr");
        highScrLbl.setText("Highest Score");
        highScrLbl.setVisible(false);
        highScrLbl.setFocusable(false);
        highScrLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // EscLabel
        //
        escLabelLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 20));
        escLabelLbl.setBounds(menuLabelsX, 503, 300, 22);
        escLabelLbl.setForeground(Color.yellow);
        escLabelLbl.setName("EscLabel");
        escLabelLbl.setText("Press ESC to return");
        escLabelLbl.setVisible(false);
        escLabelLbl.setFocusable(false);
        escLabelLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // MusicButton
        //
        musicButtonLbl.setBackground(Color.black);
        musicButtonLbl.setBorder(BorderFactory.createLineBorder(Color.white));
        musicButtonLbl.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 34));
        musicButtonLbl.setForeground(Color.white);
        musicButtonLbl.setBounds(LoadMap.DEFAULTWIDTH/2 - 70,120,140,44);
        musicButtonLbl.setName("MusicButton");
        musicButtonLbl.setText("MUSIC");
        musicButtonLbl.setVisible(false);
        musicButtonLbl.setFocusable(false);
        musicButtonLbl.setOpaque(true);
        musicButtonLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // MusicBtnSelector
        //
        musicBtnSelectorLbl.setBackground(Color.yellow);
        musicBtnSelectorLbl.setBounds(LoadMap.DEFAULTWIDTH/2 - 76,120,24,44);
        musicBtnSelectorLbl.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 39));
        musicBtnSelectorLbl.setName("MusicBtnSelector");
        musicBtnSelectorLbl.setText("<html><div style='background-color: yellow; width: 8px;'>&nbsp;</div><html>");
        musicBtnSelectorLbl.setVisible(false);
        musicBtnSelectorLbl.setFocusable(false);
        //
        // SoundsButton
        //
        soundsButtonLbl.setBackground(Color.black);
        soundsButtonLbl.setBorder(BorderFactory.createLineBorder(Color.white));
        soundsButtonLbl.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 34));
        soundsButtonLbl.setForeground(Color.white);
        soundsButtonLbl.setBounds(LoadMap.DEFAULTWIDTH/2 - 75,233,150,44);
        soundsButtonLbl.setName("SoundsButton");
        soundsButtonLbl.setText("SOUND");
        soundsButtonLbl.setVisible(false);
        soundsButtonLbl.setFocusable(false);
        soundsButtonLbl.setOpaque(true);
        soundsButtonLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // SoundsBtnSelector
        //
        soundsBtnSelectorLbl.setBackground(Color.yellow);
        soundsBtnSelectorLbl.setBounds(LoadMap.DEFAULTWIDTH/2 - 81,233,24,44);
        soundsBtnSelectorLbl.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 39));
        soundsBtnSelectorLbl.setName("SoundsBtnSelector");
        soundsBtnSelectorLbl.setText("<html><div style='background-color: yellow; width: 8px;'>&nbsp;</div><html>");
        soundsBtnSelectorLbl.setVisible(false);
        soundsBtnSelectorLbl.setFocusable(false);
        //
        // HighScoreLabel
        //
        highScoreLabelLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 34));
        highScoreLabelLbl.setForeground(Color.yellow);
        highScoreLabelLbl.setBounds(71,113,300,39);
        highScoreLabelLbl.setName("HighScoreLabel");
        highScoreLabelLbl.setText("Highest Score");
        highScoreLabelLbl.setVisible(false);
        highScoreLabelLbl.setFocusable(false);
        //
        // ScoreLabel
        //
        scoreLabelLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 28));
        scoreLabelLbl.setForeground(Color.white);
        scoreLabelLbl.setBounds(71,273,300,36);
        scoreLabelLbl.setName("ScoreLabel");
        scoreLabelLbl.setText("Your Score");
        scoreLabelLbl.setVisible(false);
        scoreLabelLbl.setFocusable(false);
        //
        // HighScoreNum
        //
        highScoreNumLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 38));
        highScoreNumLbl.setForeground(Color.yellow);
        highScoreNumLbl.setBounds(181,171,200,58);
        highScoreNumLbl.setName("HighScoreNum");
        highScoreNumLbl.setVisible(false);
        highScoreNumLbl.setFocusable(false);
        highScoreNumLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        //
        // ScoreNum
        //
        scoreNumLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 33));
        scoreNumLbl.setForeground(Color.white);
        scoreNumLbl.setBounds(181,310,200,54);
        scoreNumLbl.setName("ScoreNum");
        scoreNumLbl.setVisible(false);
        scoreNumLbl.setFocusable(false);
        scoreNumLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        //
        // GameOverLabel
        //
        gameOverLabelLbl.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 48));
        gameOverLabelLbl.setForeground(Color.red);
        gameOverLabelLbl.setName("GameOverLabel");
        gameOverLabelLbl.setBounds(prefaceLabelsX,33,400,52);
        gameOverLabelLbl.setText("GAME OVER");
        gameOverLabelLbl.setVisible(false);
        gameOverLabelLbl.setFocusable(false);
        gameOverLabelLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // ErrorLdMap
        //
        errorLdMapLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 34));
        errorLdMapLbl.setForeground(Color.white);
        errorLdMapLbl.setBounds(menuLabelsX,42,300,39);
        errorLdMapLbl.setName("ErrorLdMap");
        errorLdMapLbl.setText("Error Loading Map");
        errorLdMapLbl.setVisible(false);
        errorLdMapLbl.setFocusable(false);
        errorLdMapLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // ErrorInfo
        //
        errorInfoLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 34));
        errorInfoLbl.setForeground(Color.white);
        errorInfoLbl.setBounds(0,50, 450, 450);
        errorInfoLbl.setName("ErrorInfo");
        errorInfoLbl.setText("");
        errorInfoLbl.setVisible(false);
        errorInfoLbl.setFocusable(false);
        //
        // TryAgainBut
        //
        tryAgainButLbl.setBackground(Color.white);
        tryAgainButLbl.setOpaque(true);
        tryAgainButLbl.setBorder(BorderFactory.createLineBorder(Color.white));
        tryAgainButLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 25));
        tryAgainButLbl.setForeground(Color.black);
        tryAgainButLbl.setBounds(LoadMap.DEFAULTWIDTH/2 - 210,370,170,31);
        tryAgainButLbl.setName("TryAgainBut");
        tryAgainButLbl.setText("TRY AGAIN");
        tryAgainButLbl.setVisible(false);
        tryAgainButLbl.setFocusable(false);
        tryAgainButLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // AdvancedLdBut
        //
        advancedLdButLbl.setBackground(Color.white);
        advancedLdButLbl.setOpaque(true);
        advancedLdButLbl.setBorder(BorderFactory.createLineBorder(Color.white));
        advancedLdButLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 25));
        advancedLdButLbl.setForeground(Color.black);
        advancedLdButLbl.setBounds(LoadMap.DEFAULTWIDTH/2 - 30,370,230,31);
        advancedLdButLbl.setName("AdvancedLdBut");
        advancedLdButLbl.setText("ADVANCED LOAD");
        advancedLdButLbl.setVisible(false);
        advancedLdButLbl.setFocusable(false);
        advancedLdButLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // EditExistingBtn
        //
        editExistingButLbl.setBackground(Color.black);
        editExistingButLbl.setBorder(BorderFactory.createLineBorder(Color.white));
        editExistingButLbl.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 34));
        editExistingButLbl.setForeground(Color.white);
        editExistingButLbl.setBounds(LoadMap.DEFAULTWIDTH/2 - 52,120,104,44);
        editExistingButLbl.setName("EditExistingBut");
        editExistingButLbl.setText("EDIT");
        editExistingButLbl.setVisible(false);
        editExistingButLbl.setFocusable(false);
        editExistingButLbl.setOpaque(true);
        editExistingButLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // EditBtnSeletor
        //
        editBtnSelectorLbl.setBackground(Color.yellow);
        editBtnSelectorLbl.setBounds(LoadMap.DEFAULTWIDTH/2 - 66,120,24,44);
        editBtnSelectorLbl.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 39));
        editBtnSelectorLbl.setName("editBtnSelector");
        editBtnSelectorLbl.setText("<html><div style='background-color: yellow; width: 8px;'>&nbsp;</div><html>");
        editBtnSelectorLbl.setVisible(false);
        editBtnSelectorLbl.setFocusable(false);
        //
        // CreateNewBtn
        //
        createNewButLbl.setBackground(Color.black);
        createNewButLbl.setBorder(BorderFactory.createLineBorder(Color.white));
        createNewButLbl.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 34));
        createNewButLbl.setForeground(Color.white);
        createNewButLbl.setBounds(LoadMap.DEFAULTWIDTH/2 - 84,233,168,44);
        createNewButLbl.setName("CreateNewBut");
        createNewButLbl.setText("CREATE");
        createNewButLbl.setVisible(false);
        createNewButLbl.setFocusable(false);
        createNewButLbl.setOpaque(true);
        createNewButLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // CreateBtnSeletor
        //
        createBtnSelectorLbl.setBackground(Color.yellow);
        createBtnSelectorLbl.setBounds(LoadMap.DEFAULTWIDTH/2 - 98,233,24,44);
        createBtnSelectorLbl.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 39));
        createBtnSelectorLbl.setName("createBtnSelector");
        createBtnSelectorLbl.setText("<html><div style='background-color: yellow; width: 8px;'>&nbsp;</div><html>");
        createBtnSelectorLbl.setVisible(false);
        createBtnSelectorLbl.setFocusable(false);
        //
        // P2ButLbl
        //
        P2ButLbl.setBackground(Color.black);
        P2ButLbl.setBorder(BorderFactory.createLineBorder(Color.white));
        P2ButLbl.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 34));
        P2ButLbl.setForeground(Color.white);
        P2ButLbl.setBounds(LoadMap.DEFAULTWIDTH/2 - 52,120,104,44);
        P2ButLbl.setName("p2But");
        P2ButLbl.setText("1 v 1");
        P2ButLbl.setVisible(false);
        P2ButLbl.setFocusable(false);
        P2ButLbl.setOpaque(true);
        P2ButLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // P2ButSelectorLbl
        //
        P2ButSelectorLbl.setBackground(Color.yellow);
        P2ButSelectorLbl.setBounds(LoadMap.DEFAULTWIDTH/2 - 66,120,24,44);
        P2ButSelectorLbl.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 39));
        P2ButSelectorLbl.setName("p2BtnSelector");
        P2ButSelectorLbl.setText("<html><div style='background-color: yellow; width: 8px;'>&nbsp;</div><html>");
        P2ButSelectorLbl.setVisible(false);
        P2ButSelectorLbl.setFocusable(false);
        //
        // P3ButLbl
        //
        P3ButLbl.setBackground(Color.black);
        P3ButLbl.setBorder(BorderFactory.createLineBorder(Color.white));
        P3ButLbl.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 34));
        P3ButLbl.setForeground(Color.white);
        P3ButLbl.setBounds(LoadMap.DEFAULTWIDTH/2 - 52,233,104,44);
        P3ButLbl.setName("p3But");
        P3ButLbl.setText("1 v 2");
        P3ButLbl.setVisible(false);
        P3ButLbl.setFocusable(false);
        P3ButLbl.setOpaque(true);
        P3ButLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // P3ButSelectorLbl
        //
        P3ButSelectorLbl.setBackground(Color.yellow);
        P3ButSelectorLbl.setBounds(LoadMap.DEFAULTWIDTH/2 - 66,233,24,44);
        P3ButSelectorLbl.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 39));
        P3ButSelectorLbl.setName("p3BtnSelector");
        P3ButSelectorLbl.setText("<html><div style='background-color: yellow; width: 8px;'>&nbsp;</div><html>");
        P3ButSelectorLbl.setVisible(false);
        P3ButSelectorLbl.setFocusable(false);
        //
        // P4ButLbl
        //
        P4ButLbl.setBackground(Color.black);
        P4ButLbl.setBorder(BorderFactory.createLineBorder(Color.white));
        P4ButLbl.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 34));
        P4ButLbl.setForeground(Color.white);
        P4ButLbl.setBounds(LoadMap.DEFAULTWIDTH/2 - 52,346,104,44);
        P4ButLbl.setName("p4But");
        P4ButLbl.setText("1 v 3");
        P4ButLbl.setVisible(false);
        P4ButLbl.setFocusable(false);
        P4ButLbl.setOpaque(true);
        P4ButLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // P4ButSelectorLbl
        //
        P4ButSelectorLbl.setBackground(Color.yellow);
        P4ButSelectorLbl.setBounds(LoadMap.DEFAULTWIDTH/2 - 66,346,24,44);
        P4ButSelectorLbl.setFont(new Font("Microsoft Sans Serif", Font.BOLD, 39));
        P4ButSelectorLbl.setName("p4BtnSelector");
        P4ButSelectorLbl.setText("<html><div style='background-color: yellow; width: 8px;'>&nbsp;</div><html>");
        P4ButSelectorLbl.setVisible(false);
        P4ButSelectorLbl.setFocusable(false);
        //
        // AboutHeadlineLbl
        //
        aboutHeadlineLbl.setBackground(Color.BLACK);
        aboutHeadlineLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 28));
        aboutHeadlineLbl.setForeground(Color.white);
        aboutHeadlineLbl.setSize(350,50);
        aboutHeadlineLbl.setText("CHARACTER / NICKNAME");
        aboutHeadlineLbl.setName("aboutHeadline");
        aboutHeadlineLbl.setLocation((LoadMap.DEFAULTWIDTH - 350) / 2, 50);
        aboutHeadlineLbl.setHorizontalAlignment(SwingConstants.CENTER);
        aboutHeadlineLbl.setVisible(false);
        //
        // AboutCharacterLbls
        //
        for (int i = 0; i < 4; ++i){
            aboutCharacterLbls[i].setBackground(Color.BLACK);
            aboutCharacterLbls[i].setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 28));
            aboutCharacterLbls[i].setForeground(i == 0 ? new Color(255,0,0)
                                            : i == 1 ? new Color(255, 170, 227)
                                            : i == 2 ? new Color(81,255,255) : new Color(255, 180, 95));
            aboutCharacterLbls[i].setText(i == 0 ? "  - SHADOW    \"BLINKY\""
                    : i == 1 ? "  - SPEEDY      \"PINKY\""
                    : i == 2 ? "  - BASHFUL    \"INKY\"" : "  - POKEY        \"CLYDE\"");
            aboutCharacterLbls[i].setName("aboutCharater" + Integer.toString(i + 2));
            aboutCharacterLbls[i].setSize(350, 50);
            aboutCharacterLbls[i].setLocation((LoadMap.DEFAULTWIDTH - 350) / 2, 120 + 55 * i);
            aboutCharacterLbls[i].setHorizontalAlignment(SwingConstants.LEFT);
            aboutCharacterLbls[i].setVisible(false);
        }
        //
        // AboutPelletLbls
        //
        for (int i = 0; i < 2; ++i){
            aboutPelletLbls[i].setBackground(Color.BLACK);
            aboutPelletLbls[i].setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 20));
            aboutPelletLbls[i].setForeground(Color.white);
            aboutPelletLbls[i].setText(i == 0 ? "  10 PTS" : "  50 PTS");
            aboutPelletLbls[i].setName("aboutPeller"+ Integer.toString(i + 2));
            aboutPelletLbls[i].setSize(100, 50);
            aboutPelletLbls[i].setLocation((LoadMap.DEFAULTWIDTH - 100) / 2, 340 + 30*i);
            aboutPelletLbls[i].setHorizontalAlignment(SwingConstants.CENTER);
            aboutPelletLbls[i].setVisible(false);
        }
        //
        // AboutCopyLbl
        //
        aboutCopyLbl.setBackground(Color.BLACK);
        aboutCopyLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 28));
        aboutCopyLbl.setForeground(new Color(217, 200, 217));
        aboutCopyLbl.setText("2019 FILIP HORKY");
        aboutCopyLbl.setName("aboutCopy");
        aboutCopyLbl.setSize(300, 50);
        aboutCopyLbl.setLocation(menuLabelsX, 420);
        aboutCopyLbl.setHorizontalAlignment(SwingConstants.CENTER);
        aboutCopyLbl.setVisible(false);
        //
        // TypeSymbols
        //
        typeSymbolsLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 34));
        typeSymbolsLbl.setForeground(Color.white);
        typeSymbolsLbl.setBounds(menuLabelsX,163,300,39);
        typeSymbolsLbl.setName("TypeSymbols");
        typeSymbolsLbl.setText("Type 5 symbols:");
        typeSymbolsLbl.setVisible(false);
        typeSymbolsLbl.setFocusable(false);
        typeSymbolsLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // TypedSymbols
        //
        typedSymbolsLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 36));
        typedSymbolsLbl.setForeground(Color.yellow);
        typedSymbolsLbl.setBounds(menuLabelsX,260,300,48);
        typedSymbolsLbl.setName("TypedSymbols");
        typedSymbolsLbl.setText("");
        typedSymbolsLbl.setVisible(false);
        typedSymbolsLbl.setFocusable(true);
        typedSymbolsLbl.setHorizontalAlignment(SwingConstants.CENTER);
        //
        // TypeHint
        //
        typeHintLbl.setBorder(BorderFactory.createLineBorder(Color.white));
        typeHintLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 22));
        typeHintLbl.setForeground(Color.white);
        typeHintLbl.setBounds(prefaceLabelsX,141,400,180);
        typeHintLbl.setName("TypeHint");
        typeHintLbl.setText("<html><div style='padding-left: 10px; padding-top: 10px;'>[Free ; Pellet ; P. pellet ; Wall ; Gate]</div></html>");
        typeHintLbl.setVisible(false);
        typeHintLbl.setFocusable(false);
        typeHintLbl.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void instantiateComponents(){
        mainPanel = new JPanel();
        openFileDialog1 = new JFileChooser();

        // Timers will be set up properly lately
        pacUpdater = pacSmoothTimer = ghostUpdater = ghostSmoothTimer = null;

        pacmanLbl = new JLabel();
        ultimateLbl = new JLabel();
        copyrightLbl = new JLabel();
        pressEnterLbl = new JLabel();
        selectMapLbl = new JLabel();
        orgGameLbl = new JLabel();
        settingsLbl = new JLabel();
        escLabelLbl = new JLabel();
        highScrLbl = new JLabel();
        multiplayerLbl = new JLabel();
        highScoreLabelLbl = new JLabel();
        scoreLabelLbl = new JLabel();
        highScoreNumLbl = new JLabel();
        scoreNumLbl = new JLabel();
        musicButtonLbl = new JLabel();
        musicBtnSelectorLbl = new JLabel();
        soundsButtonLbl = new JLabel();
        soundsBtnSelectorLbl = new JLabel();
        gameOverLabelLbl = new JLabel();
        errorLdMapLbl = new JLabel();
        errorInfoLbl = new JLabel();
        tryAgainButLbl = new JLabel();
        advancedLdButLbl = new JLabel();
        typeSymbolsLbl = new JLabel();
        typedSymbolsLbl = new JLabel();
        typeHintLbl = new JLabel();
        editorLbl = new JLabel();
        aboutLbl = new JLabel();
        editExistingButLbl = new JLabel();
        editBtnSelectorLbl = new JLabel();
        createNewButLbl = new JLabel();
        createBtnSelectorLbl = new JLabel();
        P2ButLbl = new JLabel();
        P2ButSelectorLbl = new JLabel();
        P3ButLbl = new JLabel();
        P3ButSelectorLbl = new JLabel();
        P4ButLbl = new JLabel();
        P4ButSelectorLbl = new JLabel();
        aboutHeadlineLbl = new JLabel();
        aboutCharacterLbls = new JLabel[]{new JLabel(), new JLabel(), new JLabel(), new JLabel()};
        aboutPelletLbls = new JLabel[]{new JLabel(), new JLabel()};
        aboutCopyLbl = new JLabel();
    }

    private void addComponents()
    {
        mainPanel.add(typeSymbolsLbl);
        mainPanel.add(typedSymbolsLbl);
        mainPanel.add(typeHintLbl);
        mainPanel.add(advancedLdButLbl);
        mainPanel.add(tryAgainButLbl);
        mainPanel.add(errorLdMapLbl);
        mainPanel.add(gameOverLabelLbl);
        mainPanel.add(soundsButtonLbl);
        mainPanel.add(soundsBtnSelectorLbl);
        mainPanel.add(musicButtonLbl);
        mainPanel.add(musicBtnSelectorLbl);
        mainPanel.add(scoreNumLbl);
        mainPanel.add(highScoreNumLbl);
        mainPanel.add(scoreLabelLbl);
        mainPanel.add(highScoreLabelLbl);
        mainPanel.add(multiplayerLbl);
        mainPanel.add(highScrLbl);
        mainPanel.add(escLabelLbl);
        mainPanel.add(settingsLbl);
        mainPanel.add(orgGameLbl);
        mainPanel.add(selectMapLbl);
        mainPanel.add(pressEnterLbl);
        mainPanel.add(copyrightLbl);
        mainPanel.add(ultimateLbl);
        mainPanel.add(pacmanLbl);
        mainPanel.add(errorInfoLbl);
        mainPanel.add(editorLbl);
        mainPanel.add(aboutLbl);
        mainPanel.add(openFileDialog1);
        mainPanel.add(editExistingButLbl);
        mainPanel.add(editBtnSelectorLbl);
        mainPanel.add(createNewButLbl);
        mainPanel.add(createBtnSelectorLbl);
        mainPanel.add(P2ButLbl);
        mainPanel.add(P2ButSelectorLbl);
        mainPanel.add(P3ButLbl);
        mainPanel.add(P3ButSelectorLbl);
        mainPanel.add(P4ButLbl);
        mainPanel.add(P4ButSelectorLbl);
        mainPanel.add(aboutHeadlineLbl);
        mainPanel.add(aboutCharacterLbls[0]);
        mainPanel.add(aboutCharacterLbls[1]);
        mainPanel.add(aboutCharacterLbls[2]);
        mainPanel.add(aboutCharacterLbls[3]);
        mainPanel.add(aboutPelletLbls[0]);
        mainPanel.add(aboutPelletLbls[1]);
        mainPanel.add(aboutCopyLbl);
    }

    //</editor-fold>>

    //<editor-fold desc="- GETTERS Block -"

    public JPanel getMainPanel() {
        return mainPanel;
    }
    public JLabel getPacmanLbl(){
        return pacmanLbl;
    }
    public JLabel getUltimateLbl() {
        return ultimateLbl;
    }
    public JLabel getCopyrightLbl() {
        return copyrightLbl;
    }
    public JLabel getPressEnterLbl() {
        return pressEnterLbl;
    }
    public JLabel getSelectMapLbl() {
        return selectMapLbl;
    }
    public JLabel getOrgGameLbl() {
        return orgGameLbl;
    }
    public JLabel getSettingsLbl() {
        return settingsLbl;
    }
    public JLabel getEscLabelLbl() {
        return escLabelLbl;
    }
    public JLabel getHighScrLbl() {
        return highScrLbl;
    }
    public JLabel getMultiplayerLbl() {
        return multiplayerLbl;
    }
    public JLabel getMusicButtonLbl() {
        return musicButtonLbl;
    }
    public JLabel getSoundsButtonLbl() {
        return soundsButtonLbl;
    }
    public JLabel getMusicBtnSelectorLbl() {
        return musicBtnSelectorLbl;
    }
    public JLabel getSoundsBtnSelectorLbl() {
        return soundsBtnSelectorLbl;
    }
    public JLabel getHighScoreLabelLbl() {
        return highScoreLabelLbl;
    }
    public JLabel getScoreLabelLbl() {
        return scoreLabelLbl;
    }
    public JLabel getHighScoreNumLbl() {
        return highScoreNumLbl;
    }
    public JLabel getScoreNumLbl() {
        return scoreNumLbl;
    }
    public JLabel getGameOverLabelLbl(){
        return gameOverLabelLbl;
    }
    public JLabel getErrorLdMapLbl() {
        return errorLdMapLbl;
    }
    public JLabel getTryAgainButLbl() {
        return tryAgainButLbl;
    }
    public JLabel getAdvancedLdButLbl() {
        return advancedLdButLbl;
    }
    public JLabel getTypeSymbolsLbl() {
        return typeSymbolsLbl;
    }
    public JLabel getTypedSymbolsLbl() {
        return typedSymbolsLbl;
    }
    public JLabel getTypeHintLbl() {
        return typeHintLbl;
    }
    public JLabel getErrorInfoLbl() {
        return errorInfoLbl;
    }
    public JLabel getEditorLbl()
    {
        return editorLbl;
    }
    public JLabel getAboutLbl ()
    {
        return aboutLbl;
    }
    public JLabel getEditExistingButLbl ()
    {
        return editExistingButLbl;
    }
    public JLabel getEditBtnSelectorLbl ()
    {
        return editBtnSelectorLbl;
    }
    public JLabel getCreateNewButLbl ()
    {
        return createNewButLbl;
    }
    public JLabel getCreateBtnSelectorLbl ()
    {
        return createBtnSelectorLbl;
    }
    public JLabel getP2ButLbl()
    {
        return P2ButLbl;
    }
    public JLabel getP2ButSelectorLbl()
    {
        return P2ButSelectorLbl;
    }
    public JLabel getP3ButLbl()
    {
        return P3ButLbl;
    }
    public JLabel getP3ButSelectorLbl()
    {
        return P3ButSelectorLbl;
    }
    public JLabel getP4ButLbl()
    {
        return P4ButLbl;
    }
    public JLabel getP4ButSelectorLbl()
    {
        return P4ButSelectorLbl;
    }
    public JLabel getAboutHeadlineLbl()
    {
        return aboutHeadlineLbl;
    }
    public JLabel[] getAboutCharacterLbls()
    {
        return aboutCharacterLbls;
    }
    public JLabel[] getAboutPelletLbls()
    {
        return aboutPelletLbls;
    }
    public JLabel getAboutCopyLbl()
    {
        return aboutCopyLbl;
    }

    public Timer getPacUpdater() {
        return pacUpdater;
    }
    public Timer getGhostUpdater() {
        return ghostUpdater;
    }
    public Timer getPacSmoothTimer() {
        return pacSmoothTimer;
    }
    public Timer getGhostSmoothTimer() {
        return ghostSmoothTimer;
    }

    public JFileChooser getOpenFileDialog1() {
        return openFileDialog1;
    }

    //</editor-fold>
}
