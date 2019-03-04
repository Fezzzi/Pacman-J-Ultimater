package pacman_ultimater.project_base.gui_swing.ui.controller;

import pacman_ultimater.project_base.core.*;
import pacman_ultimater.project_base.custom_utils.IntPair;
import pacman_ultimater.project_base.custom_utils.Quintet;
import pacman_ultimater.project_base.custom_utils.TimersListeners;
import pacman_ultimater.project_base.gui_swing.model.GameConsts;
import pacman_ultimater.project_base.gui_swing.model.GameModel;
import pacman_ultimater.project_base.gui_swing.model.MainFrameModel;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static pacman_ultimater.project_base.gui_swing.model.GameConsts.*;

class GameLoadController {

    private MainFrameModel model;
    private GameModel vars;
    private JLabel loading, levelLabel, ready;
    private int[] animLabelsIds = new int[5];

    //<editor-fold desc="- STARTGAME Block -">

    GameLoadController(MainFrameModel model, GameModel vars) throws LineUnavailableException
    {
        this.model = model;
        this.vars = vars;
        vars.musicPlayer = AudioSystem.getClip();
    }

    /**
     * Function that handles loading, setting and placing of all the map tiles in game control.
     *
     * @param tiles game map in 2D tile array
     * @param color tiles color
     */
    void renderMap(Tile[][] tiles, Color color)
    {
        Image bufferImage = model.mainPanel.createImage(vars.size.width, vars.size.height);
        vars.bufferGraphics = bufferImage.getGraphics();
        Graphics2D bg2D = (Graphics2D)vars.bufferGraphics;
        bg2D.setStroke(new BasicStroke(3));
        vars.bufferGraphics.setColor(Color.BLACK);
        vars.bufferGraphics.fillRect(0,0, vars.size.width, vars.size.height);

        if (color == LoadMap.TRANSPARENT)
            color = chooseRandomColor();
        if(color != Color.white)
            vars.mapColor = color;
        for (int i = 0; i < LoadMap.MAPHEIGHTINTILES; i++)
            for (int j = 0; j < LoadMap.MAPWIDTHINTILES; j++)
                tiles[i][j].DrawTile(vars.bufferGraphics, new Point(j * LoadMap.TILESIZEINPXS, (i + 3) * LoadMap.TILESIZEINPXS), color);

        vars.gameMap.setIcon(new ImageIcon(bufferImage));
    }

    /**
     * Creates deep copy of map array so the game can modify actual map but does not lose information about original map.
     *
     * @param source Original Map.
     * @return Tile[][]
     */
    private Tile[][] deepCopy(Tile[][] source)
    {
        Tile[][] destination = new Tile[source.length][];
        for (int i = 0; i < source.length; i++)
        {
            destination[i] = new Tile[source[i].length];
            for (int j = 0; j < source[i].length; j++)
                destination[i][j] = new Tile(source[i][j].tile);
        }
        return destination;
    }

    /**
     * Physically places picture's control in the form.
     *
     * @param pic Picture's control.
     * @param image Image to be assigned to picture's control.
     * @param point Picture's location.
     * @param size Picture's image size.
     */
    private void placePicture(JLabel pic, ImageIcon image, Point point, Dimension size)
    {
        pic.setIcon(image);
        pic.setLocation(point);
        pic.setSize(size);
        model.mainPanel.add(pic);
    }

    /**
     * Physically places label's control in the form.
     *
     * @param label Label's control.
     * @param text Text to be assigned to label's control.
     * @param color Assigned text's color.
     * @param point Label's location.
     * @param font Text's font.
     */
    private void placeLabel(JLabel label, String text, Color color, Point point, Font font)
    {
        label.setText(text);
        label.setForeground(color);
        label.setLocation(point);
        label.setFont(font);
        label.setFocusable(false);
        model.mainPanel.add(label);
    }

    /**
     * Resets entities to their original positions and states.
     */
    private void resetEntities(){
        vars.entities.get(0).item1 = LoadMap.PACMANINITIALX;
        vars.entities.get(0).item2 = LoadMap.PACMANINITIALY;
        vars.entities.get(0).item3.setIcon(new ImageIcon(model.resourcesPath + "/Textures/PacStart.png"));
        vars.entities.get(1).item1 = vars.topGhostInTiles.item1;
        vars.entities.get(1).item2 = vars.topGhostInTiles.item2;
        vars.entities.get(1).item3.setIcon(new ImageIcon(model.resourcesPath + "/Textures/Entity2Left.png"));
        vars.entities.get(2).item1 = vars.topGhostInTiles.item1 - 2;
        vars.entities.get(2).item2 = vars.topGhostInTiles.item2 + 3;
        vars.entities.get(3).item1 = vars.topGhostInTiles.item1;
        vars.entities.get(3).item2 = vars.topGhostInTiles.item2 + 3;
        vars.entities.get(4).item1 = vars.topGhostInTiles.item1 + 2;
        vars.entities.get(4).item2 = vars.topGhostInTiles.item2 + 3;

        for(int i = 0; i < GameConsts.ENTITYCOUNT; ++i) {
            vars.entities.get(i).item3.setLocation(
                    new Point(vars.entities.get(i).item1 * LoadMap.TILESIZEINPXS + 3,
                            vars.entities.get(i).item2 * LoadMap.TILESIZEINPXS + 42));

            if(i < 2)
                vars.entities.get(i).item4 = Direction.directionType.LEFT;
            else {
                vars.entities.get(i).item4 = Direction.directionType.DIRECTION;
                vars.entities.get(i).item3.setIcon(
                    new ImageIcon(model.resourcesPath + "/Textures/Entity"
                            + Integer.toString(i + 1) + (i % 2 == 0 ? "Up.png" : "Down.png")));
            }
        }
    }

    /**
     * Function that loads all the game entities and presets all their default settings such as position, direction, etc...
     */
    private void loadEntities()
    {
        //Entity's Data structure consists of:
        //  - Two numbers - x and y position on the map in Tiles.
        //  - Picture box containing entity's image and its physical location.
        //  - Direction used later for entity's movement and selecting the right image.
        //  - Type of entity such as Player1, Player2, or all the other kinds of enemy's AI.
        vars.defaultAIs = new DefaultAI[]
        {
            new DefaultAI(DefaultAI.nType.HOSTILEATTACK),
            new DefaultAI(DefaultAI.nType.HOSTILEATTACK),
            new DefaultAI(DefaultAI.nType.HOSTILEATTACK),
            new DefaultAI(DefaultAI.nType.HOSTILEATTACK)
        };

        vars.entities = new ArrayList<>();
        vars.entities.add(new Quintet<>(LoadMap.PACMANINITIALX, LoadMap.PACMANINITIALY,
                new JLabel(), Direction.directionType.LEFT, new DefaultAI(DefaultAI.nType.PLAYER1)));
        vars.entities.add(new Quintet<>(vars.topGhostInTiles.item1, vars.topGhostInTiles.item2,
                new JLabel(), Direction.directionType.LEFT, vars.player2 ? new DefaultAI(DefaultAI.nType.PLAYER2)
                                                                        : vars.defaultAIs[0]));
        vars.entities.add(new Quintet<>(vars.topGhostInTiles.item1 - 2, vars.topGhostInTiles.item2 + 3,
                new JLabel(),Direction.directionType.DIRECTION, vars.defaultAIs[1]));
        vars.entities.add(new Quintet<>(vars.topGhostInTiles.item1, vars.topGhostInTiles.item2 + 3,
                new JLabel(),Direction.directionType.DIRECTION, vars.defaultAIs[2]));
        vars.entities.add(new Quintet<>(vars.topGhostInTiles.item1 + 2, vars.topGhostInTiles.item2 + 3,
                new JLabel(),Direction.directionType.DIRECTION, vars.defaultAIs[3]));

        //Setting entities names for easy later manipulation and automatic image selection
        for (int i = 1; i <= ENTITYCOUNT; i++)
            vars.entities.get(i - 1).item3.setName("Entity" + Integer.toString(i));

        // All those magic numbers are X and Y axes correction for entities' pictures to be correctly placed.
        placePicture(vars.entities.get(0).item3,
            new ImageIcon(model.resourcesPath + "/Textures/PacStart.png"),
            new Point(vars.entities.get(0).item1 * LoadMap.TILESIZEINPXS + 3, vars.entities.get(0).item2 * LoadMap.TILESIZEINPXS + 42),
            new Dimension(ENTITIESSIZEINPXS, ENTITIESSIZEINPXS));

        placePicture(vars.entities.get(1).item3,
            new ImageIcon(model.resourcesPath + "/Textures/Entity2Left.png"),
            new Point(vars.entities.get(1).item1 * LoadMap.TILESIZEINPXS + 3, vars.entities.get(1).item2 * LoadMap.TILESIZEINPXS + 42),
            new Dimension(ENTITIESSIZEINPXS, ENTITIESSIZEINPXS));

        for (int i = 2; i < ENTITYCOUNT; i++)
            placePicture(vars.entities.get(i).item3,
                new ImageIcon(model.resourcesPath + "/Textures/Entity" + Integer.toString(i + 1) + (i % 2 == 0 ? "Up.png" : "Down.png")),
                new Point(vars.entities.get(i).item1 * LoadMap.TILESIZEINPXS + 3, vars.entities.get(i).item2 * LoadMap.TILESIZEINPXS + 42),
                new Dimension(ENTITIESSIZEINPXS, ENTITIESSIZEINPXS));
    }

    /**
     * Function that loads score labels and pacman lives.
     */
    private void loadHud()
    {
        final Font hudTextFont = new Font("Arial", Font.BOLD, 18);
        final int HEARTHSIZEINPX = 32;
        int lives = 0;
        vars.up1 = new JLabel();
        vars.up1.setSize(50,30);
        vars.scoreBox = new JLabel();
        vars.scoreBox.setSize(120,30);
        placeLabel(vars.up1, "1UP", Color.white, new Point(3 * LoadMap.TILESIZEINPXS, 0), hudTextFont);
        placeLabel(vars.scoreBox, vars.score > 0 ? Integer.toString(vars.score) : "00", Color.white,
                new Point(4 * LoadMap.TILESIZEINPXS, 20), hudTextFont);
        vars.up1.setVisible(true);
        vars.scoreBox.setVisible(true);

        //Selects labels depending on game mode
        if (!vars.player2)
        {
            vars.highScoreBox = new JLabel();
            vars.highScoreBox.setSize(200,30);
            placeLabel(vars.highScoreBox, vars.highScore > 0 ? Integer.toString(vars.highScore) : "00", Color.white,
                    new Point(21 * LoadMap.TILESIZEINPXS, 20), hudTextFont);
            JLabel highScoreText = new JLabel();
            highScoreText.setSize(200,30);
            placeLabel(highScoreText, "HIGH SCORE", Color.white, new Point(17 * LoadMap.TILESIZEINPXS, 0), hudTextFont);
            vars.highScoreBox.setVisible(true);
            highScoreText.setVisible(true);
        }
        else
        {
            vars.up2 = new JLabel();
            vars.up2.setSize(50,30);
            vars.score2Box = new JLabel();
            vars.score2Box.setSize(200,30);
            placeLabel(vars.up2, "2UP", Color.white, new Point(21 * LoadMap.TILESIZEINPXS, 0), hudTextFont);
            placeLabel(vars.score2Box, vars.score2 > 0 ? Integer.toString(vars.score2) : "00", Color.white,
                    new Point(22 * LoadMap.TILESIZEINPXS, 20), hudTextFont);
            vars.up2.setVisible(true);
            vars.score2Box.setVisible(true);
        }

        // Places all lives on their supposed place.
        for (JLabel item : vars.pacLives)
        {
            item.setSize(50,50);
            placePicture(item, new ImageIcon(model.resourcesPath + "/textures/Life.png"),
                    new Point(lives * HEARTHSIZEINPX + LoadMap.TILESIZEINPXS, ((LoadMap.MAPHEIGHTINTILES + 3) * LoadMap.TILESIZEINPXS) + 4),
                    new Dimension(HEARTHSIZEINPX, HEARTHSIZEINPX));
            lives++;
        }
    }

    /**
     * Procedure serving simply for initialization of variables at the map load up and displaying loading screen.
     */
    private void restartInit(){
        vars.soundTick = 0;
        vars.keyPressed1 = false;
        vars.keyPressed2 = false;
        vars.ghostsEaten = 0;
        vars.keyCountdown1 = 0;
        vars.keyCountdown2 = 0;
        vars.killed = false;
        vars.ticks = 0;
        vars.freeGhosts = 1;
        vars.eatEmTimer = 0;
        vars.ghostRelease = vars.player2 ? 130 / 3 : (260 - vars.level) / 3;
    }

    /**
     * Procedure serving simply for initialization of variables at the map load up and displaying loading screen.
     *
     * @throws IOException Propagation from highScore loading.
     * @throws LineUnavailableException Propagation from sound players creation.
     */
    private void loadingAndInit()
        throws IOException, LineUnavailableException, UnsupportedAudioFileException
    {
        if (vars.level == 0) {
            loading = new JLabel();
            levelLabel = new JLabel();
            vars.defSize = model.getMainFrameMinimumSize();
            vars.size = new Dimension((LoadMap.MAPWIDTHINTILES + 1) * LoadMap.TILESIZEINPXS,
                    (LoadMap.MAPHEIGHTINTILES + 8) * LoadMap.TILESIZEINPXS);
            model.setMainFrameSize(vars.size);
            model.mainPanel.setSize(vars.size);
            model.recenterMainFrame(vars.size);

            vars.gameMap = new JLabel();
            vars.gameMap.setSize(vars.size);
            vars.gameMap.setVisible(true);
            vars.extraLifeGiven = false;
            vars.score = 0;
            vars.score2 = 0;
            vars.soundTick = 0; // used for sound players to take turns
            vars.initSoundPlayers(model.resourcesPath);

            vars.collectedDots = 0;
            vars.lives = 3;
            ++vars.level;

            loading.setSize(350, 60);
            placeLabel(loading, "Loading...", Color.yellow, new Point(75, 103),
                new Font("Ravie", Font.BOLD, 48));
            levelLabel.setSize(300, 60);
            placeLabel(levelLabel, "- Level " + Integer.toString(vars.level) + " -", Color.red, new Point(118, 200),
                new Font("Ravie", Font.BOLD, 30));

            vars.pacLives = new JLabel[MAXLIVES];
            for (int i = 0; i < MAXLIVES; i++)
                vars.pacLives[i] = new JLabel();
        }
        loading.setVisible(true);
        levelLabel.setVisible(true);
        model.mainPanel.revalidate();
        vars.keyPressed1 = false;
        vars.keyPressed2 = false;
        vars.ghostsEaten = 0;
        vars.keyCountdown1 = 0;
        vars.keyCountdown2 = 0;
        vars.killed = false;
        vars.ticks = 0; // Counts tick to enable power pellets flashing and ghost flashing at the end of pac's excitement.
        vars.freeGhosts = 1;
        vars.ghostRelease = vars.player2 ? 130 / 3 : (260 - vars.level) / 3;
        vars.eatEmTimer = 0;

        // Yet empty fields of the array would redraw over top right corner of the map.
        // This way it draws empty tile on pacman's initial position tile which is empty by definition.
        for (int i = 0; i < LoadMap.RDPSIZE; i++)
            vars.redrawPellets[i] = new Point(LoadMap.PACMANINITIALY, LoadMap.PACMANINITIALX);

        if (vars.highScore == -1)
            vars.highScore = HighScoreClass.loadHighScore(model.resourcesPath);
    }

    /**
     * Returns random color by fixing one channel to max, another to min and the last one chooses randomly.
     *
     * @return Color
     */
    private static Color chooseRandomColor()
    {
        Random rndm = new Random();
        switch (rndm.nextInt(6))
        {
            case 0:
                return new Color(0, 255, rndm.nextInt(256));
            case 1:
                return new Color(0, rndm.nextInt(256), 255);
            case 2:
                return new Color(255, 0, rndm.nextInt(256));
            case 3:
                return new Color(255, rndm.nextInt(256), 0);
            case 4:
                return new Color(rndm.nextInt(256), 0, 255);
            default:
                return new Color(rndm.nextInt(256), 255, 0);
        }
    }

    /**
     * Provides loading and general preparing of the game at the level start-up.
     *
     * @param restart Indicates whether is method triggered by level's restart or finish.
     * @param timers Class implementing timers' handling.
     */
    void playGame(boolean restart, TimersListeners timers)
    {
        if(restart)
        {
            RestartGameTask rgTask = new RestartGameTask();
            rgTask.execute();
        }
        else {
            PlayGameTask pgTask = new PlayGameTask(timers);
            pgTask.execute();
        }
    }

    enum playGamePhase {PHASE1, PHASE2, PHASE3, INTERRUPTEDEXCEPTION, EXECUTIONEXCEPTION}

    class RestartGameTask extends SwingWorker<Void, playGamePhase>
    {
        @Override
        public Void doInBackground()
        {
            try {
                publish(playGamePhase.PHASE2);
                Thread.sleep(OPENINGTHEMELENGTH);

                publish(playGamePhase.PHASE3);
            }
            catch (InterruptedException exception){
                publish(playGamePhase.INTERRUPTEDEXCEPTION);
            }
            return null;
        }

        @Override
        protected void process(List<playGamePhase> phases)
        {
            try {
                for (playGamePhase phase : phases) {
                    if (vars.gameOn) {
                        switch (phase) {
                            case PHASE2:
                                System.out.println("Time gate 1: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
                                if (vars.music){
                                    vars.musicPlayer.close();
                                    vars.playWithMusicPLayer(model.resourcesPath + "/sounds/pacman_beginning.wav",
                                            false, 0 , 0);
                                }
                                restartInit();
                                for (int i = MAXLIVES - 1; i > vars.lives - 2 && i >= 0; i--)
                                    vars.pacLives[i].setVisible(false);

                                resetEntities();
                                ready.setVisible(true);
                                System.out.println("Time gate 2: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
                                renderMap(vars.mapFresh, vars.map.item4);
                                break;
                            case PHASE3:
                                if (vars.music)
                                    vars.musicPlayer.close();

                                ready.setVisible(false);
                                model.mainPanel.revalidate();
                                // Corrects start positions of pacman and first ghost as they are located between the tiles at first.
                                vars.entities.get(0).item3.setLocation(new Point(vars.entities.get(0).item3.getLocation().x - 9,
                                        vars.entities.get(0).item3.getLocation().y));
                                vars.entities.get(1).item3.setLocation(new Point(vars.entities.get(1).item3.getLocation().x - 9,
                                        vars.entities.get(1).item3.getLocation().y));
                                if (vars.music)
                                    vars.playWithMusicPLayer(model.resourcesPath + "/sounds/pacman_siren.wav",
                                            true, 0, 9100);

                                model.pacUpdater.start();
                                model.ghostUpdater.start();
                                break;
                            case INTERRUPTEDEXCEPTION:
                                HighScoreClass.tryToSaveScore(vars.player2, vars.score, model.resourcesPath);
                                MainFrameController.handleExceptions("java.lang.InterruptedException", model);
                                break;
                        }
                    }
                }
            }
            catch (LineUnavailableException | IOException | UnsupportedAudioFileException exception) {
                try {
                    HighScoreClass.tryToSaveScore(vars.player2, vars.score, model.resourcesPath);
                }
                catch(IOException ignore) { /* TODO: Notify user score weren't saved due to exception message */ }
                MainFrameController.handleExceptions(exception.toString(), model);
            }
        }
    }

    class PlayGameTask extends SwingWorker<Void, playGamePhase>
    {
        TimersListeners timers;

        PlayGameTask(TimersListeners timers)
        {
            this.timers = timers;
            ready = new JLabel();
        }

        @Override
        public Void doInBackground()
        {
            try {
                publish(playGamePhase.PHASE1);
                AnimationTask animTask = new AnimationTask();
                animTask.execute();
                animTask.get();
                vars.musicPlayer.stop();

                publish(playGamePhase.PHASE2);
                Thread.sleep(OPENINGTHEMELENGTH);

                publish(playGamePhase.PHASE3);
            }
            catch(InterruptedException exception){
                publish(playGamePhase.INTERRUPTEDEXCEPTION);
            }
            catch(ExecutionException exception){
                publish(playGamePhase.EXECUTIONEXCEPTION);
            }
            return null;
        }

        @Override
        protected void process(List<playGamePhase> phases)
        {
            try {
                for (playGamePhase phase : phases) {
                    if (vars.gameOn) {
                        switch (phase) {
                            case PHASE1:
                                loadingAndInit();
                                if (vars.music)
                                    vars.playWithMusicPLayer(model.resourcesPath + "/sounds/pacman_intermission.wav", false, 0, 0);
                                break;
                            case PHASE2:
                                if (vars.music)
                                    vars.musicPlayer.close();

                                loadHud();
                                for (int i = MAXLIVES - 1; i > vars.lives - 2 && i >= 0; i--)
                                    vars.pacLives[i].setVisible(false);

                                vars.topGhostInTiles = new IntPair(vars.map.item3.item1 - 1, vars.map.item3.item2 - 1);
                                loadEntities();
                                ready.setSize(150, 30);
                                placeLabel(ready, "READY!", Color.yellow,
                                    new Point(((vars.topGhostInTiles.item1 - 3) * LoadMap.TILESIZEINPXS) + 6,
                                            (vars.topGhostInTiles.item2 + 6) * LoadMap.TILESIZEINPXS + 46),
                                    new Font("Ravie", Font.BOLD, 22));

                                vars.collectedDots = 0;
                                vars.mapFresh = deepCopy(vars.map.item1);
                                if (vars.level <= 13 && vars.level > 1)
                                    model.ghostUpdater.setDelay(model.ghostUpdater.getDelay() - 5);

                                model.mainPanel.remove(loading);
                                model.mainPanel.remove(levelLabel);

                                ready.setVisible(true);
                                model.mainPanel.add(vars.gameMap);
                                model.mainPanel.repaint();
                                model.mainPanel.revalidate();
                                renderMap(vars.mapFresh, vars.map.item4);

                                if (vars.music)
                                    vars.playWithMusicPLayer(model.resourcesPath + "/sounds/pacman_beginning.wav", false, 0 , 0);
                                break;
                            case PHASE3:
                                if (vars.music)
                                    vars.musicPlayer.close();

                                ready.setVisible(false);
                                model.mainPanel.revalidate();
                                // Corrects start positions of pacman and first ghost as they are located between the tiles at first.
                                vars.entities.get(0).item3.setLocation(new Point(vars.entities.get(0).item3.getLocation().x - 9,
                                        vars.entities.get(0).item3.getLocation().y));
                                vars.entities.get(1).item3.setLocation(new Point(vars.entities.get(1).item3.getLocation().x - 9,
                                        vars.entities.get(1).item3.getLocation().y));
                                if (vars.music)
                                    vars.playWithMusicPLayer(model.resourcesPath + "/sounds/pacman_siren.wav", true, 0, 9100);

                                // Starts updaters that provides effect of main game cycle.
                                model.pacUpdater = new Timer(PACTIMER, timers.getPacman_timer());
                                model.pacUpdater.setRepeats(true);
                                model.ghostUpdater = new Timer(
                                        !vars.player2 ? (PACTIMER + 40 - (vars.level > 13 ? 65
                                                : vars.level * 5))
                                                : model.pacUpdater.getDelay() + 10,
                                        timers.getGhost_timer());
                                model.ghostUpdater.setRepeats(true);
                                model.pacSmoothTimer = new Timer(
                                        model.pacUpdater.getDelay() / ((LoadMap.TILESIZEINPXS / 2) + 1),
                                        timers.getPacman_smooth_timer());
                                model.pacSmoothTimer.setRepeats(true);
                                model.ghostSmoothTimer = new Timer(
                                        model.ghostUpdater.getDelay() / ((LoadMap.TILESIZEINPXS / 2) + 1),
                                        timers.getGhost_smooth_timer());
                                model.ghostSmoothTimer.setRepeats(true);
                                model.pacUpdater.start();
                                model.ghostUpdater.start();
                                break;
                            case EXECUTIONEXCEPTION:
                                HighScoreClass.tryToSaveScore(vars.player2, vars.score, model.resourcesPath);
                                MainFrameController.handleExceptions("java.util.concurrent.ExecutionException", model);
                                break;
                            case INTERRUPTEDEXCEPTION:
                                HighScoreClass.tryToSaveScore(vars.player2, vars.score, model.resourcesPath);
                                MainFrameController.handleExceptions("java.lang.InterruptedException", model);
                                break;
                        }
                    }
                }
            }
            catch (LineUnavailableException | IOException | UnsupportedAudioFileException exception) {
                try {
                    HighScoreClass.tryToSaveScore(vars.player2, vars.score, model.resourcesPath);
                }
                catch(IOException ignore) { /* TODO: Notify user score weren't saved due to exception message */ }
                MainFrameController.handleExceptions(exception.toString(), model);
            }
        }
    }

    /**
     * Represents single frame of loading animation
     */
    class AnimationFrame
    {
        boolean initialisation;
        boolean finished;
        ImageIcon pacmanImage;
        JLabel[] entities;
        Point[] locations;

        AnimationFrame(boolean initialisation, boolean finished, ImageIcon pacmanImage, JLabel[] entities, Point[] locations)
        {
            this.initialisation = initialisation;
            this.finished = finished;
            this.pacmanImage = pacmanImage;
            this.entities = entities;
            this.locations = locations;
        }
    }

    /**
     * Background worker handling loading animation playing. Works on publish/process base.
     */
    class AnimationTask extends SwingWorker<Void, AnimationFrame>
    {
        @Override
        public Void doInBackground()
                throws InterruptedException
        {
            Random rndm = new Random();
            int elemCount = rndm.nextInt(5) + 1;
            int pacCount = 0;
            JLabel[] elements = new JLabel[elemCount];
            int startX = LoadMap.MAPWIDTHINTILES * LoadMap.TILESIZEINPXS;

            // FIRST ANIMATION: From right to left --------------------------------------------------------------------
            for (int i = 1; i <= elemCount && vars.gameOn; i++)
            {
                elements[i-1] = new JLabel();
                elements[i-1].setIcon(new ImageIcon(model.resourcesPath + "/textures/Entity" + Integer.toString(i) + "Left.png"));
                elements[i-1].setLocation(new Point(startX + (i == 0 ? 0 : (i + 4) * (2 * LoadMap.TILESIZEINPXS)),
                                                    (LoadMap.MAPHEIGHTINTILES / 2 + 6) * LoadMap.TILESIZEINPXS));
                elements[i-1].setSize(new Dimension(ENTITIESSIZEINPXS, ENTITIESSIZEINPXS));
            }
            publish(new AnimationFrame(true, false, null, elements, null));
            Thread.sleep(24);

            Point[] locations = new Point[elemCount];
            ImageIcon pacmanImage = new ImageIcon(model.resourcesPath + "/Textures/PacStart.png");
            for (int j = startX; j > -300 && vars.gameOn; j -= 4)
            {
                ++pacCount;
                for (int i = 0; i < elemCount; i++)
                {
                    locations[i] = new Point(j + (i == 0 ? 0 : (i + 4) * (2 * LoadMap.TILESIZEINPXS)),
                                            (LoadMap.MAPHEIGHTINTILES / 2 + 6) * LoadMap.TILESIZEINPXS);
                    if (i == 0 && pacCount % 4 == 0)
                        if (pacCount % 8 == 0)
                            pacmanImage = new ImageIcon(model.resourcesPath + "/Textures/PacStart.png");
                        else
                            pacmanImage = new ImageIcon(model.resourcesPath + "/textures/Entity1Left.png");
                }

                publish(new AnimationFrame(false, false, pacmanImage, elements, locations));
                Thread.sleep(12);
            }

            // SECOND ANIMATION: From left to right -------------------------------------------------------------------
            if(vars.level % 5 == 0 && vars.gameOn){
                elements[0].setSize(new Dimension(ENTITIESSIZEINPXS * 2, ENTITIESSIZEINPXS * 2));
                elements[0].setLocation(new Point(elements[0].getLocation().x, elements[0].getLocation().y - ENTITIESSIZEINPXS));
                elements[0].setIcon(new ImageIcon(model.resourcesPath + "/textures/Entity1RightBig.png"));
                locations[0] = elements[0].getLocation();
                for (int i = 2; i <= elemCount; i++)
                    elements[i-1].setIcon(new ImageIcon(model.resourcesPath + "/textures/Entity" + Integer.toString(i) + "Right.png"));

                Thread.sleep(250);
                pacCount = 0;
                for (int j = 0; j < 260 && vars.gameOn; ++j)
                {
                    ++pacCount;
                    for (int i = 0; i < elemCount; i++)
                    {
                        locations[i] = new Point(locations[i].x + 4, locations[i].y);
                        if (i == 0 && pacCount % 4 == 0)
                            if (pacCount % 8 == 0)
                                pacmanImage = new ImageIcon(model.resourcesPath + "/Textures/PacStartBig.png");
                            else
                                pacmanImage = new ImageIcon(model.resourcesPath + "/textures/Entity1RightBig.png");
                    }

                    publish(new AnimationFrame(false, false, pacmanImage, elements, locations));
                    Thread.sleep(12);
                }
            }

            publish(new AnimationFrame(false, true, pacmanImage, elements, locations));
            return null;
        }

        @Override
        protected void process(List<AnimationFrame> frames)
        {
            for (AnimationFrame frame : frames) {
                if(vars.gameOn) {
                    if (frame.initialisation) {
                        for (int i = 0; i < frame.entities.length; ++i) {
                            animLabelsIds[i] = model.mainPanel.getComponentCount();
                            model.mainPanel.add(frame.entities[i]);
                            model.mainPanel.getComponent(animLabelsIds[i]).setVisible(true);
                        }
                    } else if (frame.finished) {
                        for (int i = frame.entities.length - 1; i >= 0; --i) {
                            model.mainPanel.remove(animLabelsIds[i]);
                        }
                    } else {
                        for (int i = 0; i < frame.entities.length; ++i) {
                            model.mainPanel.getComponent(animLabelsIds[i]).setLocation(frame.locations[i].x, frame.locations[i].y);
                        }
                        frame.entities[0].setIcon(frame.pacmanImage);
                    }
                    model.mainPanel.revalidate();
                    model.mainPanel.repaint();
                }
            }
        }
    }

    //</editor-fold>
}
