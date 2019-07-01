package pacman_ultimater.project_base.gui_swing.ui.controller;

import pacman_ultimater.project_base.core.HighScoreClass;
import pacman_ultimater.project_base.core.LoadMap;
import pacman_ultimater.project_base.custom_utils.Pair;
import pacman_ultimater.project_base.gui_swing.model.GameModel;
import pacman_ultimater.project_base.gui_swing.model.MainFrameModel;
import pacman_ultimater.project_base.gui_swing.ui.view.MainFrame;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Controlls lowest level of program such as window closing or iconifying.
 * Provides handling of all exceptions that weren't individually handled by upper methods.
 */
public class MainFrameController {

    private MainFrameModel model;
    private MainFrame mainFrame;
    private MenuController mc;
    private GameModel vars;
    private resizeWorker resizer;

    public MainFrameController(MainFrameModel model, MainFrame mainFrame)
    {
        this.model = model;
        this.mainFrame = mainFrame;
        this.vars = new GameModel();
        resizer = new resizeWorker();
    }

    /**
     * Resizes and repositions all components based on vertical and horizontal multiplicators
     */
    private void resize(float vMult, float hMult)
    {
        // Reposition menu labels
        for (JLabel label : model.labels) {
            String name = label.getName();
            Rectangle defBound = model.defPositions.get(name);
            int newX,
                newY = (int)((defBound.y * vMult) + ((defBound.height * vMult) - defBound.height) / 2),
                newHeight = defBound.height,
                newWidth = defBound.width;

            switch (name){
                case "copyright":
                    newX = (int)((defBound.x * hMult) + (defBound.width * hMult) - defBound.width);
                    newY = (int)((defBound.y * vMult) + (defBound.height * vMult) - defBound.height);
                    break;
                case "TryAgainBut":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - 210);
                    break;
                case "AdvancedLdBut":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - 30);
                    break;
                case "SoundsButton":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - 75);
                    break;
                case "SoundsBtnSelector":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - 83);
                    break;
                case "MusicButton":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - 70);
                    break;
                case "MusicBtnSelector":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - 78);
                    break;
                case "EditExistingBut":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - 52);
                    break;
                case "editBtnSelector":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - 60);
                    break;
                case "CreateNewBut":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - 84);
                    break;
                case "createBtnSelector":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - 92);
                    break;
                default:
                    newX = (int)((defBound.x * hMult) + ((defBound.width * hMult) - defBound.width) / 2);
            }

            if (name.equals("TypeHint")) {
                newHeight = (int)(defBound.height * vMult);
                newY -= (newHeight - defBound.height) / 2;
            }

            label.setBounds(newX, newY, newWidth, newHeight);
        }

        // Update editor layout
        if (vars.editor) {
            for (JLabel component: vars.addedComponents) {
                for (MouseListener listener: component.getMouseListeners()){
                    component.removeMouseListener(listener);
                }
                model.mainPanel.remove(component);
            }
            mc.editor.show();
        }

        if (vars.gameOn) {

        }
    }

    /**
     * Creates new instance of menu - resulting in menu showing up
     */
    public void OpenMenu()
    {
        mainFrame.addWindowListener(new windowListener());
        mainFrame.setVisible(true);
        model.mainPanel.grabFocus();

        try {
            resizer.doInBackground();
        } catch (Exception ignore) {
            UIManager.put("Panel.background", Color.black);
            JOptionPane.showMessageDialog(
                    model.mainPanel, "Error occured, resizing disabled!",
                    "Pac-Man J-Ultimater: Error message", JOptionPane.PLAIN_MESSAGE);
            UIManager.put("Panel.background", Color.white);
            mainFrame.setResizable(false);
        }
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

    /**
     * Displays fatal error message
     *
     * @param content String
     */
    private void fatalErrorMessage(String content)
    {
        UIManager.put("Panel.background", Color.black);
        JOptionPane.showMessageDialog(model.mainPanel,
                "Fatal Error, " + content,
                "Pac-Man J-Ultimater: Error message",
                JOptionPane.PLAIN_MESSAGE);
        UIManager.put("Panel.background", Color.white);
    }

    /**
     * Handles events like window minimisation, window closing, atc...
     */
    private class windowListener implements WindowListener
    {
        /**
         * Method handling form's load request.
         * @param e WindowEvent
         */
        @Override
        public void windowOpened(WindowEvent e)
        {
            mc = new MenuController(model, vars);
            try {
                mc.openMenu();
            }
            catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException exception){
                if (vars.score > vars.highScore) {
                    try {
                        HighScoreClass.tryToSaveScore(vars.player2, vars.score);
                    } catch (IOException ignore) {
                        fatalErrorMessage("score was not saved!");
                    }
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
            resizer.closing = true;
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
                } catch (IOException ignore) {
                    fatalErrorMessage("score was not saved!");
                }
            }
            model.disposeMainFrame();
        }

        /**
         * {@inheritDoc}
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
            if (vars.gameOn) {
                model.pacUpdater.stop();
                model.pacSmoothTimer.stop();
                model.ghostUpdater.stop();
                model.ghostSmoothTimer.stop();
            }
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
     * Background worker responsible for periodical notifying in case window was resized
     */
    private class resizeWorker extends SwingWorker<Void, Pair<Float, Float>>
    {
        boolean closing = false;
        private float vMult = 1, hMult = 1;

        @Override
        protected Void doInBackground() throws Exception
        {
            while (!closing && !model.errorInfoLbl.isVisible()) {
                Rectangle r = mainFrame.getBounds();
                vars.vMult = (float)r.height / vars.defSize.height;
                vars.hMult = (float)r.width / vars.defSize.width;

                if (vars.vMult < 1 || vars.hMult < 1
                || Math.abs(vMult - vars.vMult) > 0.05 || Math.abs(hMult - vars.hMult) > 0.05) {
                    vMult = vars.vMult;
                    hMult = vars.hMult;

                    publish(new Pair<>(vMult, hMult));
                }

                Thread.sleep(500);
            }
            return null;
        }

        @Override
        protected void process(java.util.List<Pair<Float,Float>> chunks)
        {
            Pair<Float,Float> last = chunks.get(chunks.size() - 1);
            if (last.item1 >= 1 && last.item2 >=1) {
                resize(last.item1, last.item2);
            } else {
                // Fix for Windows 10 display scale ability
                mainFrame.setSize(vars.defSize.width, vars.defSize.height);
                resize(1, 1);
            }
        }
    }
}
