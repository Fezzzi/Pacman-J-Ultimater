package pacman_ultimater.project_base.gui_swing.ui.view;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class MainFrame extends JFrame{

    //<editor-fold desc="- Frame Components -">

    private String resourcesPath;
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

    public MainFrame(){
        resourcesPath = new File("src/main/resources").getAbsolutePath();

        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(resourcesPath + "\\PacManJUltimater.png").getImage());
        setMinimumSize(new Dimension(490, 529));
        setName("PacManJUltimater");
        setTitle("Pac-Man J-Ultimater");
        setResizable(false);

        initComponents();
        addComponents();

        setContentPane(mainPanel);
        //Might want to set margin to (3, 2, 3, 2);

        mainPanel.revalidate();
    }

    private void initComponents(){
        instantiateComponents();
        //
        // Main Frame
        //
        this.setFocusable(false);
        //
        // Main Panel
        //
        mainPanel.setLayout(null);
        mainPanel.setBackground(SystemColor.desktop);
        mainPanel.setFocusable(true);
        //
        // Open File Dialog
        //
        openFileDialog1.setDialogTitle("Choose Map File");
        openFileDialog1.setFileFilter(new FileFilter() {
            public boolean accept(File dir) {
                if(dir.isFile())
                    return dir.getName().toLowerCase().endsWith(".txt");
                return false;
            }

            public String getDescription(){
                return "";
            }
        });
        openFileDialog1.setFocusable(false);
        //
        // Pacman
        //
        pacmanLbl.setFont(new Font("Ravie", Font.BOLD, 54));
        pacmanLbl.setForeground(Color.YELLOW);
        pacmanLbl.setBounds(70, 50,380,75);
        pacmanLbl.setName("pacman");
        pacmanLbl.setText("PAC-MAN");
        pacmanLbl.setVisible(false);
        pacmanLbl.setFocusable(false);
        //
        // J-Ultimater
        //
        ultimateLbl.setFont(new Font("Ravie", Font.BOLD, 32));
        ultimateLbl.setForeground(Color.red);
        ultimateLbl.setBounds(58, 115,380,50);
        ultimateLbl.setName("jultimater");
        ultimateLbl.setText("- J-ULTIMATER -");
        ultimateLbl.setVisible(false);
        ultimateLbl.setFocusable(false);
        //
        // copyright
        //
        copyrightLbl.setForeground(Color.yellow);
        copyrightLbl.setBounds(292, 460, 179,17);
        copyrightLbl.setName("copyright");
        copyrightLbl.setText("© Copyright Filip Horký 2018");
        copyrightLbl.setVisible(false);
        copyrightLbl.setFocusable(false);
        //
        // PressEnter
        //
        pressEnterLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 34));
        pressEnterLbl.setForeground(Color.white);
        pressEnterLbl.setBounds(110, 180,400,200);
        pressEnterLbl.setName("pressEnter");
        pressEnterLbl.setText("<html>&nbsp;&nbsp;INSERT COIN<br/><br/>- press any key -</html>");
        pressEnterLbl.setVisible(false);
        pressEnterLbl.setFocusable(false);
        //
        // OrgGame
        //
        orgGameLbl.setFont( new Font("Microsoft Sans Serif", Font.PLAIN, 34));
        orgGameLbl.setForeground(Color.white);
        orgGameLbl.setBounds(117,51,241,41);
        orgGameLbl.setName("OrgGame");
        orgGameLbl.setText("Original Game");;
        orgGameLbl.setVisible(false);
        orgGameLbl.setFocusable(false);
        //
        // selectMap
        //
        selectMapLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 34));
        selectMapLbl.setForeground(Color.white);
        selectMapLbl.setBounds(145,118,190,39);
        selectMapLbl.setName("selectMap");
        selectMapLbl.setText("Select Map");
        selectMapLbl.setVisible(false);
        selectMapLbl.setFocusable(false);
        //
        // Settings
        //
        settingsLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 34));
        settingsLbl.setForeground(Color.white);
        settingsLbl.setBounds(168,186,143,39);
        settingsLbl.setName("Settings");
        settingsLbl.setText("Settings");
        settingsLbl.setVisible(false);
        settingsLbl.setFocusable(false);
        //
        // VS
        //
        vsLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 34));
        vsLbl.setForeground(Color.white);
        vsLbl.setBounds(211,275,63,39);
        vsLbl.setName("VS");
        vsLbl.setText("VS");
        vsLbl.setVisible(false);
        vsLbl.setFocusable(false);
        //
        // HighScr
        //
        highScrLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 34));
        highScrLbl.setForeground(Color.white);
        highScrLbl.setBounds(130,328,235,38);
        highScrLbl.setName("HihScr");
        highScrLbl.setText("Highest Score");
        highScrLbl.setVisible(false);
        highScrLbl.setFocusable(false);
        //
        // EscLabel
        //
        escLabelLbl.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 20));
        escLabelLbl.setBounds(152, 427, 185, 22);
        escLabelLbl.setForeground(Color.yellow);
        escLabelLbl.setName("EscLabel");
        escLabelLbl.setText("Press ESC to return");
        escLabelLbl.setVisible(false);
        escLabelLbl.setFocusable(false);
    }

    private void instantiateComponents(){
        mainPanel = new JPanel();
        openFileDialog1 = new JFileChooser(resourcesPath);

        pacmanLbl = new JLabel();
        ultimateLbl = new JLabel();
        copyrightLbl = new JLabel();
        pressEnterLbl = new JLabel();
        selectMapLbl = new JLabel();
        orgGameLbl = new JLabel();
        settingsLbl = new JLabel();
//        pacUpdater = new Timer(1000,);
//        ghostUpdater = new Timer(1000,);
//        pacSmoothTimer = new Timer(1000,);
//        ghostSmoothTimer = new Timer(1000,);
        escLabelLbl = new JLabel();
        highScrLbl = new JLabel();
        vsLbl = new JLabel();
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
    }

    private void addComponents(){
        add(typeSymbolsLbl);
        add(typedSymbolsLbl);
        add(typeHintLbl);
        add(advancedLdButLbl);
        add(tryAgainButLbl);
        add(errorLdMapLbl);
        add(gameOverLabelLbl);
        add(soundsButtonLbl);
        add(soundsBtnSelectorLbl);
        add(musicBtnSelectorLbl);
        add(musicButtonLbl);
        add(scoreNumLbl);
        add(highScoreNumLbl);
        add(scoreLabelLbl);
        add(highScoreLabelLbl);
        mainPanel.add(vsLbl);
        mainPanel.add(highScrLbl);
        mainPanel.add(escLabelLbl);
        mainPanel.add(settingsLbl);
        mainPanel.add(orgGameLbl);
        mainPanel.add(selectMapLbl);
        mainPanel.add(pressEnterLbl);
        mainPanel.add(copyrightLbl);
        mainPanel.add(ultimateLbl);
        mainPanel.add(pacmanLbl);
        add(errorInfoLbl);
        mainPanel.add(openFileDialog1);
    }

    //<editor-fold desc="- Getters -"

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
    public JLabel getVsLbl() {
        return vsLbl;
    }
    public JFileChooser getOpenFileDialog1() { return openFileDialog1; }

    public String getResourcesPath() { return resourcesPath; }

    //</editor-fold>
}
