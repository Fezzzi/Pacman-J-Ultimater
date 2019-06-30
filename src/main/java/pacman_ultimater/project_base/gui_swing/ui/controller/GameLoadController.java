package pacman_ultimater.project_base.gui_swing.ui.controller;

import pacman_ultimater.project_base.core.*;
import pacman_ultimater.project_base.custom_utils.IntPair;
import pacman_ultimater.project_base.custom_utils.Quintet;
import pacman_ultimater.project_base.custom_utils.TimersListeners;
import pacman_ultimater.project_base.gui_swing.model.GameConsts;
import pacman_ultimater.project_base.gui_swing.model.GameModel;
import pacman_ultimater.project_base.gui_swing.model.MainFrameModel;
import pacman_ultimater.project_base.core.ClasspathFileReader;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static pacman_ultimater.project_base.gui_swing.model.GameConsts.*;

/**
 * Controlls game loading.
 * Destinguishes between first load, restart level load and next level load.
 */
class GameLoadController
{
    private MainFrameModel model;
    private GameModel vars;
    private JLabel loading, levelLabel, ready;
    private int[] animLabelsIds = new int[5];

    GameLoadController(MainFrameModel model, GameModel vars) throws LineUnavailableException
    {
        this.model = model;
        this.vars = vars;
        vars.musicPlayer = AudioSystem.getClip();
        vars.topGhostInTiles = new IntPair(vars.map.item3.item1 - 1, vars.map.item3.item2 - 1);

        initLabels();
    }

    //<editor-fold desc="- HELPER FUNCTIONS Block -">

    /**
     * Initializes loading, level and ready labels.
     */
    private void initLabels()
    {
        ready = new JLabel();
        ready.setSize(150, 30);
        ready.setVisible(false);
        placeLabel(ready, "READY!", Color.yellow,
                new Point(((vars.topGhostInTiles.item1 - 3) * LoadMap.TILESIZEINPXS) + 6,
                        (vars.topGhostInTiles.item2 + 6) * LoadMap.TILESIZEINPXS + 46),
                ClasspathFileReader.getFONT().deriveFont(Font.BOLD, 22));

        loading = new JLabel();
        loading.setSize(350, 60);
        loading.setVisible(false);
        placeLabel(loading, "Loading...", Color.yellow, new Point(75, 103),
                ClasspathFileReader.getFONT().deriveFont(Font.BOLD, 48));

        levelLabel = new JLabel();
        levelLabel.setSize(300, 60);
        levelLabel.setVisible(false);
        placeLabel(levelLabel, "", Color.red, new Point(118, 200),
                ClasspathFileReader.getFONT().deriveFont(Font.BOLD, 30));

        vars.pacLives = new JLabel[MAXLIVES];
        for (int i = 0; i < MAXLIVES; i++)
            vars.pacLives[i] = new JLabel();
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
        bg2D.setStroke(new BasicStroke(2));
        vars.bufferGraphics.setColor(Color.BLACK);
        vars.bufferGraphics.fillRect(0,0, vars.size.width, vars.size.height);

        if (color == LoadMap.TRANSPARENT)
            color = LoadMap.chooseRandomColor();
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
     *
     * @throws IOException To be handled by caller.
     */
    private void resetEntities()
        throws IOException
    {
        vars.entities.get(0).item1 = LoadMap.PACMANINITIALX;
        vars.entities.get(0).item2 = LoadMap.PACMANINITIALY;
        vars.entities.get(0).item3.setIcon(new ImageIcon(ClasspathFileReader.getPACSTART().readAllBytes()));
        vars.entities.get(1).item1 = vars.topGhostInTiles.item1;
        vars.entities.get(1).item2 = vars.topGhostInTiles.item2;
        vars.entities.get(1).item3.setIcon(new ImageIcon(ClasspathFileReader.getENTITY2LEFT().readAllBytes()));
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
                vars.entities.get(i).item3.setIcon(new ImageIcon(
                    ClasspathFileReader.getEntityFile("Entity" + Integer.toString(i + 1),
                                                        i % 2 == 0 ? "UP" : "DOWN").readAllBytes()));
            }
        }
    }

    /**
     * Function that loads all the game entities and presets all their default settings such as position, direction, etc...
     *
     * @throws IOException To be handled by caller.
     */
    private void loadEntities()
        throws IOException
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
            new ImageIcon(ClasspathFileReader.getPACSTART().readAllBytes()),
            new Point(vars.entities.get(0).item1 * LoadMap.TILESIZEINPXS + 3, vars.entities.get(0).item2 * LoadMap.TILESIZEINPXS + 42),
            new Dimension(ENTITIESSIZEINPXS, ENTITIESSIZEINPXS));

        placePicture(vars.entities.get(1).item3,
            new ImageIcon(ClasspathFileReader.getENTITY2LEFT().readAllBytes()),
            new Point(vars.entities.get(1).item1 * LoadMap.TILESIZEINPXS + 3, vars.entities.get(1).item2 * LoadMap.TILESIZEINPXS + 42),
            new Dimension(ENTITIESSIZEINPXS, ENTITIESSIZEINPXS));

        for (int i = 2; i < ENTITYCOUNT; i++)
            placePicture(vars.entities.get(i).item3,
                new ImageIcon(ClasspathFileReader.getEntityFile("Entity" + Integer.toString(i + 1),
                                                                i % 2 == 0 ? "UP" : "DOWN").readAllBytes()),
                new Point(vars.entities.get(i).item1 * LoadMap.TILESIZEINPXS + 3, vars.entities.get(i).item2 * LoadMap.TILESIZEINPXS + 42),
                new Dimension(ENTITIESSIZEINPXS, ENTITIESSIZEINPXS));
    }

    /**
     * Function that loads score labels and pacman lives.
     *
     * @throws IOException To be handled by caller.
     */
    private void loadHud()
        throws IOException
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
            placePicture(item, new ImageIcon(ClasspathFileReader.getLIFE().readAllBytes()),
                    new Point(lives * HEARTHSIZEINPX + LoadMap.TILESIZEINPXS, ((LoadMap.MAPHEIGHTINTILES + 3) * LoadMap.TILESIZEINPXS) + 4),
                    new Dimension(HEARTHSIZEINPX, HEARTHSIZEINPX));
            lives++;
        }
    }

    /**
     * Initializes pacman's and ghosts' timers.
     *
     * @param timers Structure holding listeners for each initialized timer.
     */
    private void initTimers(TimersListeners timers)
    {
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
    }

    /**
     * Procedure serving for initialization of variables at the game load up.
     *
     * @throws IOException Propagation from highScore loading.
     * @throws LineUnavailableException Propagation from sound players creation.
     */
    private void varsInit()
        throws IOException, LineUnavailableException, UnsupportedAudioFileException
    {
        vars.defSize = model.getMainFrameMinimumSize();
        vars.size = new Dimension((LoadMap.MAPWIDTHINTILES + 1) * LoadMap.TILESIZEINPXS,
                                (LoadMap.MAPHEIGHTINTILES + 8) * LoadMap.TILESIZEINPXS);
        model.setMainFrameSize(vars.size);
        model.mainPanel.setSize(vars.size);
        model.recenterMainFrame(vars.size);

        vars.gameMap = new JLabel();
        vars.gameMap.setSize(vars.size);
        vars.extraLifeGiven = false;
        vars.score = 0;
        vars.score2 = 0;
        vars.initSoundPlayers();
        vars.lives = 3;

        if (vars.highScore == -1)
            vars.highScore = HighScoreClass.loadHighScore();

        // Yet empty fields of the array would redraw over top right corner of the map.
        // This way it draws empty tile on pacman's initial position tile which is empty by definition.
        for (int i = 0; i < LoadMap.RDPSIZE; i++)
            vars.redrawPellets[i] = new Point(LoadMap.PACMANINITIALY, LoadMap.PACMANINITIALX);
    }

    /**
     * Procedure serving for variables resetting at the map load up.
     *
     * @param nextLevel Reset Variables for new level load-up?
     */
    private void varsReset(boolean nextLevel){
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

        if(nextLevel){
            vars.collectedDots = 0;
            vars.ghostRelease = vars.player2 ? 130 / 3 : (260 - vars.level) / 3;
        }
    }

    /**
     * Corrects start positions of pacman and first ghost as they are located between the tiles at first.
     */
    private void correctStartPositions()
    {
        vars.entities.get(0).item3.setLocation(new Point(vars.entities.get(0).item3.getLocation().x - 9,
                vars.entities.get(0).item3.getLocation().y));
        vars.entities.get(1).item3.setLocation(new Point(vars.entities.get(1).item3.getLocation().x - 9,
                vars.entities.get(1).item3.getLocation().y));
    }

    /**
     * Sets visibility for components based on given intro/game boolean.
     *
     * @param gameComponents Indicates whether to set visible components for the intro or for the game.
     */
    private void toggleComponentsVisibility (boolean gameComponents)
    {
        vars.gameMap.setVisible(gameComponents);
        ready.setVisible(gameComponents);
        for(int i = 0; i < vars.entities.size(); ++i)
            vars.entities.get(i).item3.setVisible(gameComponents);

        levelLabel.setVisible(!gameComponents);
        loading.setVisible(!gameComponents);
    }

    //</editor-fold>

    //<editor-fold desc="- PUBLIC API Block -">

    /**
     * Provides next level loading in already loaded game.
     */
    void loadNextLevel()
    {
        LoadNextLevelTask lngTask = new LoadNextLevelTask();
        lngTask.execute();
    }

    /**
     * Provides already loaded level reloading.
     */
    void reloadLevel()
    {
        RestartLevelTask rgTask = new RestartLevelTask();
        rgTask.execute();
    }

    /**
     * Provides loading and general preparing of the game at the level start-up.
     *
     * @param timers Class implementing timers' handling.
     */
    void loadGame(TimersListeners timers)
    {
        PlayGameTask pgTask = new PlayGameTask(timers);
        pgTask.execute();
    }

    //</editor-fold>

    //<editor-fold desc="- LOADING TASKS Block -">

    private enum playGamePhase {PHASE1, PHASE2, PHASE3, INTERRUPTEDEXCEPTION, EXECUTIONEXCEPTION}

    class LoadNextLevelTask extends GameLoadSwingWorker
    {
        LoadNextLevelTask()
        {
            super(true);
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
            catch (InterruptedException exception){
                publish(playGamePhase.INTERRUPTEDEXCEPTION);
            }
            catch (ExecutionException exception){
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
                                varsReset(true);
                                levelLabel.setText("- Level " + Integer.toString(vars.level) + " -");
                                toggleComponentsVisibility(false);
                                if (vars.music)
                                    vars.playWithMusicPLayer(ClasspathFileReader.getPACMAN_INTERMISSION(),
                                            false, 0, 0);
                                break;
                            case PHASE2:
                                if (vars.music)
                                    vars.musicPlayer.stop();

                                for (int i = MAXLIVES - 1; i > vars.lives - 2 && i >= 0; i--)
                                    vars.pacLives[i].setVisible(false);

                                resetEntities();
                                vars.mapFresh = deepCopy(vars.map.item1);
                                model.ghostUpdater.setDelay((GameConsts.PACTIMER + 40 - (vars.level > 13 ? 65 : vars.level * 5)) * 2);
                                model.ghostSmoothTimer.setDelay(model.ghostUpdater.getDelay() / ((LoadMap.TILESIZEINPXS / 2) + 1));

                                renderMap(vars.mapFresh, vars.map.item4);
                                toggleComponentsVisibility(true);

                                if (vars.music)
                                    vars.playWithMusicPLayer(ClasspathFileReader.getPACMAN_BEGINNING(),
                                            false, 0, 0);
                                break;
                            case PHASE3:
                                if (vars.music)
                                    vars.musicPlayer.close();

                                ready.setVisible(false);
                                model.mainPanel.revalidate();
                                correctStartPositions();
                                if (vars.music)
                                    vars.playWithMusicPLayer(ClasspathFileReader.getPACMAN_SIREN(),
                                            true, 0, 9100);

                                model.pacUpdater.start();
                                model.ghostUpdater.start();
                                break;
                            case INTERRUPTEDEXCEPTION:
                                if (vars.score > vars.highScore) {
                                    HighScoreClass.tryToSaveScore(vars.player2, vars.score);
                                }
                                MainFrameController.handleExceptions("java.lang.InterruptedException", model);
                                break;
                        }
                    }
                }
            }
            catch (LineUnavailableException | IOException | UnsupportedAudioFileException exception) {
                if (vars.score > vars.highScore) {
                    try {
                        HighScoreClass.tryToSaveScore(vars.player2, vars.score);
                    } catch (IOException ignore) { /* TODO: Notify user score weren't saved due to exception message */ }
                }
                MainFrameController.handleExceptions(exception.toString(), model);
            }
        }
    }

    class RestartLevelTask extends GameLoadSwingWorker
    {
        RestartLevelTask()
        {
            super(false);
        }

        @Override
        protected void process(List<playGamePhase> phases)
        {
            try {
                for (playGamePhase phase : phases) {
                    if (vars.gameOn) {
                        switch (phase) {
                            case PHASE2:
                                if (vars.music) {
                                    vars.musicPlayer.stop();
                                    vars.playWithMusicPLayer(ClasspathFileReader.getPACMAN_BEGINNING(),
                                            false, 0, 0);
                                }
                                varsReset(false);
                                for (int i = MAXLIVES - 1; i > vars.lives - 2 && i >= 0; i--)
                                    vars.pacLives[i].setVisible(false);

                                if (vars.player2)
                                   vars.score2Box.setText(Integer.toString(vars.score2));

                                resetEntities();
                                ready.setVisible(true);
                                renderMap(vars.mapFresh, vars.map.item4);
                                break;
                            case PHASE3:
                                if (vars.music)
                                    vars.musicPlayer.close();

                                ready.setVisible(false);
                                correctStartPositions();
                                if (vars.music)
                                    vars.playWithMusicPLayer(ClasspathFileReader.getPACMAN_SIREN(),
                                            true, 0, 9100);

                                model.pacUpdater.start();
                                model.ghostUpdater.start();
                                model.mainPanel.revalidate();
                                break;
                            case INTERRUPTEDEXCEPTION:
                                if (vars.score > vars.highScore) {
                                    HighScoreClass.tryToSaveScore(vars.player2, vars.score);
                                }
                                MainFrameController.handleExceptions("java.lang.InterruptedException", model);
                                break;
                        }
                    }
                }
            }
            catch (LineUnavailableException | IOException | UnsupportedAudioFileException exception) {
                if (vars.score > vars.highScore) {
                    try {
                        HighScoreClass.tryToSaveScore(vars.player2, vars.score);
                    } catch (IOException ignore) { /* TODO: Notify user score weren't saved due to exception message */ }
                }
                MainFrameController.handleExceptions(exception.toString(), model);
            }
        }
    }

    class PlayGameTask extends GameLoadSwingWorker
    {
        TimersListeners timers;

        PlayGameTask(TimersListeners timers)
        {
            super(true);
            this.timers = timers;
        }

        @Override
        protected void process(List<playGamePhase> phases)
        {
            try {
                for (playGamePhase phase : phases) {
                    if (vars.gameOn) {
                        switch (phase) {
                            case PHASE1:
                                varsInit();
                                varsReset(true);
                                initTimers(timers);
                                levelLabel.setText("- Level " + Integer.toString(vars.level) + " -");
                                loading.setVisible(true);
                                levelLabel.setVisible(true);
                                if (vars.music)
                                    vars.playWithMusicPLayer(ClasspathFileReader.getPACMAN_INTERMISSION(),
                                            false, 0, 0);
                                break;

                            case PHASE2:
                                if (vars.music)
                                    vars.musicPlayer.close();

                                loadHud();
                                for (int i = MAXLIVES - 1; i > vars.lives - 2 && i >= 0; i--)
                                    vars.pacLives[i].setVisible(false);

                                loadEntities();
                                if (vars.level <= 13 && vars.level > 1)
                                    model.ghostUpdater.setDelay(model.ghostUpdater.getDelay() - 5);

                                vars.mapFresh = deepCopy(vars.map.item1);
                                model.mainPanel.add(vars.gameMap);
                                toggleComponentsVisibility(true);
                                renderMap(vars.mapFresh, vars.map.item4);

                                if (vars.music)
                                    vars.playWithMusicPLayer(ClasspathFileReader.getPACMAN_BEGINNING(),
                                            false, 0 , 0);
                                break;
                            case PHASE3:
                                if (vars.music)
                                    vars.musicPlayer.close();

                                ready.setVisible(false);
                                correctStartPositions();
                                if (vars.music)
                                    vars.playWithMusicPLayer(ClasspathFileReader.getPACMAN_SIREN(),
                                            true, 0, 9100);

                                model.pacUpdater.start();
                                model.ghostUpdater.start();
                                model.mainPanel.revalidate();
                                break;
                            case EXECUTIONEXCEPTION:
                                if (vars.score > vars.highScore) {
                                    HighScoreClass.tryToSaveScore(vars.player2, vars.score);
                                }
                                MainFrameController.handleExceptions("java.util.concurrent.ExecutionException", model);
                                break;
                            case INTERRUPTEDEXCEPTION:
                                if (vars.score > vars.highScore) {
                                    HighScoreClass.tryToSaveScore(vars.player2, vars.score);
                                }
                                MainFrameController.handleExceptions("java.lang.InterruptedException", model);
                                break;
                        }
                    }
                }
            }
            catch (LineUnavailableException | IOException | UnsupportedAudioFileException exception) {
                if (vars.score > vars.highScore) {
                    try {
                        HighScoreClass.tryToSaveScore(vars.player2, vars.score);
                    } catch (IOException ignore) { /* TODO: Notify user score weren't saved due to exception message */ }
                }
                MainFrameController.handleExceptions(exception.toString(), model);
            }
        }
    }

    abstract class GameLoadSwingWorker extends SwingWorker<Void, playGamePhase>
    {
        boolean withAnimation;

        GameLoadSwingWorker(boolean withAnimation){
            this.withAnimation = withAnimation;
        }

        @Override
        public Void doInBackground()
        {
            try {
                if(withAnimation) {
                    publish(playGamePhase.PHASE1);
                    AnimationTask animTask = new AnimationTask();
                    animTask.execute();
                    animTask.get();
                }

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
    }

    //</editor-fold>

    //<editor-fold desc="- ANIMATION TASKS Block -">

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
                throws InterruptedException, IOException
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
                elements[i-1].setIcon(new ImageIcon(
                        ClasspathFileReader.getEntityFile("Entity" + Integer.toString(i),"LEFT").readAllBytes()));
                elements[i-1].setLocation(new Point(startX + (i == 0 ? 0 : (i + 4) * (2 * LoadMap.TILESIZEINPXS)),
                                                    (LoadMap.MAPHEIGHTINTILES / 2 + 6) * LoadMap.TILESIZEINPXS));
                elements[i-1].setSize(new Dimension(ENTITIESSIZEINPXS, ENTITIESSIZEINPXS));
            }
            publish(new AnimationFrame(true, false, null, elements, null));
            Thread.sleep(24);

            Point[] locations = new Point[elemCount];
            ImageIcon pacmanImage = new ImageIcon(ClasspathFileReader.getPACSTART().readAllBytes());
            for (int j = startX; j > -300 && vars.gameOn; j -= 4)
            {
                ++pacCount;
                for (int i = 0; i < elemCount; i++)
                {
                    locations[i] = new Point(j + (i == 0 ? 0 : (i + 4) * (2 * LoadMap.TILESIZEINPXS)),
                                            (LoadMap.MAPHEIGHTINTILES / 2 + 6) * LoadMap.TILESIZEINPXS);
                    if (i == 0 && pacCount % 4 == 0)
                        if (pacCount % 8 == 0)
                            pacmanImage = new ImageIcon(ClasspathFileReader.getPACSTART().readAllBytes());
                        else
                            pacmanImage = new ImageIcon(ClasspathFileReader.getENTITY1LEFT().readAllBytes());
                }

                publish(new AnimationFrame(false, false, pacmanImage, elements, locations));
                Thread.sleep(12);
            }

            // SECOND ANIMATION: From left to right -------------------------------------------------------------------
            if(vars.level % 5 == 0 && vars.gameOn){
                elements[0].setSize(new Dimension(ENTITIESSIZEINPXS * 2, ENTITIESSIZEINPXS * 2));
                elements[0].setLocation(new Point(elements[0].getLocation().x, elements[0].getLocation().y - ENTITIESSIZEINPXS));
                elements[0].setIcon(new ImageIcon(ClasspathFileReader.getENTITY1RIGHTBIG().readAllBytes()));
                locations[0] = elements[0].getLocation();
                for (int i = 2; i <= elemCount; i++)
                    elements[i-1].setIcon(new ImageIcon(
                            ClasspathFileReader.getEntityFile("Entity" + Integer.toString(i),"RIGHT").readAllBytes()));

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
                                pacmanImage = new ImageIcon(ClasspathFileReader.getPACSTARTBIG().readAllBytes());
                            else
                                pacmanImage = new ImageIcon(ClasspathFileReader.getENTITY1RIGHTBIG().readAllBytes());
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
