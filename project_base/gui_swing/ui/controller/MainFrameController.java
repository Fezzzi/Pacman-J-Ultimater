package pacman_ultimater.project_base.gui_swing.ui.controller;

import pacman_ultimater.project_base.gui_swing.ui.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrameController {

    private MainFrame mainFrame;
    private JButton testBtn;
    private JTextArea testTA;

    public MainFrameController(){
        initComponents();
        initListeners();
    }

    private void initComponents() {

        mainFrame = new MainFrame();
        testBtn = mainFrame.getTestBtn();
        testTA = mainFrame.getTestTA();
    }

    public void showMainFrameWindow(){
        mainFrame.setVisible(true);
    }

    private void initListeners() {
        testBtn.addActionListener(new TestBtnListener());
    }

    private class TestBtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            testTA.append("Welcome");
        }
    }
}
