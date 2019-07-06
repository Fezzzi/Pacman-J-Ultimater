package pacman_ultimater.project_base.gui_swing.ui.controller;

import pacman_ultimater.project_base.core.ClasspathFileReader;
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

    private final MainFrameModel model;
    private final MainFrame mainFrame;
    private MenuController mc;
    private final GameModel vars;
    private final resizeWorker resizer;

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
        // Resizing menu labels
        for (JLabel label : model.labels) {
            String name = label.getName();
            Pair<Rectangle, Integer> dld = model.defLabelData.get(name);
            Rectangle defBound = dld.item1;
            Integer defFontSize = dld.item2;
            int newX,
                newY = (int)((defBound.y * vMult) + ((defBound.height * vMult) - defBound.height) / 2),
                newHeight, newWidth;

            // Fonts sizing
            if (name != null && name.equals("pacman") || name.equals("jultimater")) {
                label.setFont(ClasspathFileReader.getFONT().deriveFont(Font.BOLD, defFontSize * Math.min(vMult, hMult)));
            } else {
                label.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, (int)(defFontSize * Math.min(vMult, hMult))));
            }
            newWidth = (int)(defBound.width * Math.min(vMult, hMult));
            newHeight = (int)(defBound.height * Math.min(vMult, hMult));

            // Positioning
            switch (name){
                case "copyright":
                    newX = (int)((defBound.x * hMult) + (defBound.width * hMult) - defBound.width) - (newWidth - defBound.width) / 2;
                    newY = (int)((defBound.y * vMult) + (defBound.height * vMult) - defBound.height);
                    break;
                case "TryAgainBut":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - (newWidth + 40));
                    break;
                case "AdvancedLdBut":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - 30);
                    break;
                case "SoundsButton":
                case "MusicButton":
                case "EditExistingBut":
                case "CreateNewBut":
                case "p2But":
                case "p3But":
                case "p4But":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - newWidth/2);
                    break;
                case "SoundsBtnSelector":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - (166 * Math.min(vMult, hMult)) / 2) - 8;
                    break;
                case "MusicBtnSelector":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - (156 * Math.min(vMult, hMult)) / 2) - 8;
                    break;
                case "editBtnSelector":
                case "p2BtnSelector":
                case "p3BtnSelector":
                case "p4BtnSelector":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - (120 * Math.min(vMult, hMult)) / 2) - 8;
                    break;
                case "createBtnSelector":
                    newX = (int)(((LoadMap.DEFAULTWIDTH * hMult) / 2) - (184 * Math.min(vMult, hMult)) / 2) - 8;
                    break;
                default:
                    newX = (int)((defBound.x * hMult) + ((defBound.width * hMult) - defBound.width) / 2) - (newWidth - defBound.width) / 2;
            }

            if (name.equals("TypeHint")) {
                newHeight = (int)(defBound.height * vMult);
                newY -= (newHeight - defBound.height) / 2;
            }

            label.setBounds(newX, newY, newWidth, newHeight);
        }

        // Update editor layout
        if (vars.editor) {
            mc.resizeEditor();
        }

        // Update loaded game layout
        if (vars.gameOn) {
            mc.resizeGamePlay();
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
    static void fatalErrorMessage(String content)
    {
        UIManager.put("Panel.background", Color.black);
        JOptionPane.showMessageDialog(null,
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
                        HighScoreClass.tryToSaveScore(vars.player2 || vars.player3 || vars.player4, vars.score);
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
                for (Clip soundplayer: vars.soundPlayers) {
                    if (soundplayer != null)
                        soundplayer.close();
                }

            if (vars.score > vars.highScore) {
                try {
                    HighScoreClass.tryToSaveScore(vars.player2 || vars.player3 || vars.player4, vars.score);
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
