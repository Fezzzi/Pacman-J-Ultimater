package pacman_ultimater.project_base.gui_swing.ui.view;

import javax.swing.*;

public class MainFrame extends JFrame {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;

    private JPanel mainPanel;
    private JButton testBtn;
    private JTextArea testTA;

    public MainFrame(){
        setSize(WIDTH, HEIGHT);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JButton getTestBtn() {
        return testBtn;
    }

    public JTextArea getTestTA(){
        return testTA;
    }
}
