package pacman_ultimater.project_base.gui_swing.ui.controller;

import pacman_ultimater.project_base.core.HighScoreClass;
import pacman_ultimater.project_base.gui_swing.model.GameModel;
import pacman_ultimater.project_base.gui_swing.model.MainFrameModel;
import pacman_ultimater.project_base.gui_swing.ui.view.MainFrame;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Controlls lowest level of program such as window closing or iconifying.
 * Provides handling of all exceptions that weren't individually handled by upper methods.
 */
public class MainFrameController {

    private MainFrameModel model;
    private MainFrame mainFrame;
    private GameModel vars;

    public MainFrameController(MainFrameModel model, MainFrame mainFrame)
    {
        this.model = model;
        this.mainFrame = mainFrame;
        this.vars = new GameModel();
    }

    /**
     * Creates new instance of menu - resulting in menu showing up
     */
    public void OpenMenu()
    {
        mainFrame.addWindowListener(new windowListener());
        mainFrame.setVisible(true);
        model.mainPanel.grabFocus();
    }

    /**
     * Handles events like window minimisation, window closing, atc...
     */
    private class windowListener implements WindowListener {

        /**
         * Method handling form's load request.
         * @param e WindowEvent
         */
        @Override
        public void windowOpened(WindowEvent e)
        {
            MenuController mc = new MenuController(model, vars);
            try {
                mc.openMenu();
            }
            catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException exception){
                if (vars.score > vars.highScore) {
                    try {
                        HighScoreClass.tryToSaveScore(vars.player2, vars.score);
                    } catch (IOException ignore) { /* TODO: Notify user score weren't saved due to exception message */ }
                }
                MainFrameController.handleExceptions(exception.getMessage(), model);
            }
        }

        /**
         * Method handling score saving and form disposing in case of application termination from outside the code.
         * @param e WindowEvent
         */
        @Override
        public void windowClosing(WindowEvent e)
        {
            vars.gameOn = false;
            for (Timer timer: new Timer[]{model.pacUpdater, model.ghostUpdater, model.pacSmoothTimer, model.ghostSmoothTimer})
                if (timer != null)
                    timer.stop();

            if(vars.musicPlayer != null)
                vars.musicPlayer.close();

            if(vars.soundPlayers != null)
                for (Clip soundplayer: vars.soundPlayers)
                    soundplayer.close();

            if (vars.score > vars.highScore) {
                try {
                    HighScoreClass.tryToSaveScore(vars.player2, vars.score);
                } catch (IOException ignore) { /* TODO: Notify user score weren't saved due to exception message */ }
            }
            model.disposeMainFrame();
        }

        /**
         * @param e WindowEvent
         */
        @Override
        public void windowClosed(WindowEvent e) {}

        /**
         * Method handling timers and music pausing on window minimisation.
         * @param e WindowEvent
         */
        @Override
        public void windowIconified(WindowEvent e)
        {
            model.pacUpdater.stop();
            model.pacSmoothTimer.stop();
            model.ghostUpdater.stop();
            model.ghostSmoothTimer.stop();
        }

        /**
         * Resumes timers and music and redraws game graphics after window minimisation.
         * @param e WindowEvent
         */
        @Override
        public void windowDeiconified(WindowEvent e)
        {
            if(vars.gameOn){
                model.pacUpdater.start();
                model.pacSmoothTimer.start();
                model.ghostUpdater.start();
                model.ghostSmoothTimer.start();
            }
        }

        @Override
        public void windowActivated(WindowEvent e) { }

        @Override
        public void windowDeactivated(WindowEvent e) { }
    }

    /**
     * Handles occurred exception by displaying apology and exception message
     * @param message Exception message.
     * @param model game's model.
     */
    static void handleExceptions(String message, MainFrameModel model)
    {
        model.mainPanel.removeAll();
        model.errorInfoLbl.setText("<html><div style='width: 100%; text-align: center; display: block;'>" +
                "<b>WE ARE SORRY</b><br />" +
                "<h1>something broke</h1><br /><h2 style='color: red'>"
                + message.replaceAll("(.{20,35}\\.)","$1<br>") + "<br /><br /><br />" +
                "</h2><h1>The game will try to save current score and close.</h1></div></html>");
        model.errorInfoLbl.setVisible(true);
        model.mainPanel.add(model.errorInfoLbl);
        model.mainPanel.repaint();
        model.mainPanel.revalidate();
    }
}
