package pacman_ultimater.project_base.gui_swing.ui.controller;

import pacman_ultimater.project_base.core.*;
import pacman_ultimater.project_base.custom_utils.IntPair;
import pacman_ultimater.project_base.custom_utils.Quintet;
import pacman_ultimater.project_base.gui_swing.model.GameModel;
import pacman_ultimater.project_base.gui_swing.model.MainFrameModel;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static pacman_ultimater.project_base.gui_swing.model.GameConsts.*;

class GameLoadController {

    private MainFrameModel model;
    private GameModel vars;

    //<editor-fold desc="- STARTGAME Block -">

    GameLoadController(MainFrameModel model, GameModel vars) throws LineUnavailableException
    {
        this.model = model;
        this.vars = vars;
        vars.musicPlayer = AudioSystem.getClip();
    }

    /**
     * Function that handles loading, setting and placing of all the map tiles in game control.
     * @param tiles game map in 2D tile array
     * @param color tiles color
     */
    private void renderMap(Tile[][] tiles, Color color)
    {
        vars.gameMap = new JLabel();
        vars.gameMap.setSize(vars.size);
        Image bufferImage = model.mainFrame.createImage(vars.size.width, vars.size.height);
        Graphics bufferGraphics = bufferImage.getGraphics();
        Graphics2D bg2D = (Graphics2D)bufferGraphics;
        bg2D.setStroke(new BasicStroke(2));
        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.fillRect(0,0, vars.size.width, vars.size.height);

        if (color == LoadMap.TRANSPARENT)
            color = chooseRandomColor();
        if(color != Color.white)
            vars.mapColor = color;
        for (int i = 0; i < LoadMap.MAPHEIGHTINTILES; i++)
            for (int j = 0; j < LoadMap.MAPWIDTHINTILES; j++)
                tiles[i][j].DrawTile(bufferGraphics, new Point(j * LoadMap.TILESIZEINPXS, (i + 3) * LoadMap.TILESIZEINPXS), color);

        vars.gameMap.setIcon(new ImageIcon(bufferImage));
        model.mainPanel.add(vars.gameMap);
        vars.gameMap.setVisible(true);
    }

    /**
     * Creates deep copy of map array so the game can modify actual map but does not lose information about original map.
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
     * Physicaly places picture's control in the form.
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
     * Function that loads all the game entities and presets all their default settings such as position, direction, etc...
     */
    private void loadEntities()
    {
        //Entitie's Data strucute consists of:
        //  - Two numbers - x and y position on the map in Tiles.
        //  - Picturebox containing entity's image and its physical location.
        //  - Direction used later for entity's movement and selecting the right image.
        //  - Type of entity such as Player1, Player2, or all the other kinds of enemy's AI.
        vars.defaultAIs = new DefaultAI[]
        {
            new DefaultAI(DefaultAI.nType.HOSTILEATTACK, LoadMap.MAPWIDTHINTILES, LoadMap.MAPHEIGHTINTILES),
            new DefaultAI(DefaultAI.nType.HOSTILEATTACK, LoadMap.MAPWIDTHINTILES, LoadMap.MAPHEIGHTINTILES),
            new DefaultAI(DefaultAI.nType.HOSTILEATTACK, LoadMap.MAPWIDTHINTILES, LoadMap.MAPHEIGHTINTILES),
            new DefaultAI(DefaultAI.nType.HOSTILEATTACK, LoadMap.MAPWIDTHINTILES, LoadMap.MAPHEIGHTINTILES)
        };

        vars.entities = new ArrayList<>();
        vars.entities.add(new Quintet<>(
                LoadMap.PACMANINITIALX, LoadMap.PACMANINITIALY, new JLabel(), Direction.nType.LEFT,
                new DefaultAI(DefaultAI.nType.PLAYER1, LoadMap.MAPWIDTHINTILES, LoadMap.MAPHEIGHTINTILES)));
        vars.entities.add(new Quintet<>(
                vars.topGhostInTiles.item1, vars.topGhostInTiles.item2, new JLabel(), Direction.nType.LEFT,
                vars.player2 ? new DefaultAI(DefaultAI.nType.PLAYER2, LoadMap.MAPWIDTHINTILES, LoadMap.MAPHEIGHTINTILES)
                            : vars.defaultAIs[0]));
        vars.entities.add(new Quintet<>(
                vars.topGhostInTiles.item1 - 2, vars.topGhostInTiles.item2 + 3, new JLabel(),Direction.nType.DIRECTION, vars.defaultAIs[1]));
        vars.entities.add(new Quintet<>(
                vars.topGhostInTiles.item1, vars.topGhostInTiles.item2 + 3, new JLabel(),Direction.nType.DIRECTION, vars.defaultAIs[2]));
        vars.entities.add(new Quintet<>(
                vars.topGhostInTiles.item1 + 2, vars.topGhostInTiles.item2 + 3, new JLabel(),Direction.nType.DIRECTION, vars.defaultAIs[3]));

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
     * @param hp Number of lives to be displayed.
     */
    private void loadHud(int hp)
    {
        final Font hudTextFont = new Font("Arial", Font.BOLD, 18);
        final int HEARTHSIZEINPX = 32;
        int lives = 0;
        vars.up1 = new JLabel();
        vars.up1.setSize(50,30);
        vars.scoreBox.setSize(120,30);
        placeLabel(vars.up1, "1UP", Color.white, new Point(3 * LoadMap.TILESIZEINPXS, 0), hudTextFont);
        placeLabel(vars.scoreBox, vars.score > 0 ? Integer.toString(vars.score) : "00", Color.white,
                new Point(4 * LoadMap.TILESIZEINPXS, 20), hudTextFont);
        vars.up1.setVisible(true);
        vars.scoreBox.setVisible(true);

        //Selects labels depending on game mode
        if (!vars.player2)
        {
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

        // Sets visibility of lives depending on number of player's lives.
        for (int i = MAXLIVES - 1; i > hp && i >= 0; i--)
           vars.pacLives[i].setVisible(false);
    }

    /**
     * Procedure serving simply for initialization of variables at the map load up and displaying loading screen.
     * @throws IOException Propagation from highScore loading.
     * @throws LineUnavailableException Propagation from sound players creation.
     */
    private void loadingAndInit() throws IOException, LineUnavailableException
    {
        if (mfc.level == 0)
        {
            loading = new JLabel();
            levelLabel = new JLabel();
            defSize = mfc.mainFrame.getMinimumSize();
            size = new Dimension((LoadMap.MAPWIDTHINTILES) * LoadMap.TILESIZEINPXS,
                                (LoadMap.MAPHEIGHTINTILES + 8) * LoadMap.TILESIZEINPXS);
            mfc.mainFrame.setMinimumSize(size);
            mfc.mainPanel.setSize(size);

            mfc.extraLifeGiven = false;
            mfc.score = 0;
            mfc.score2 = 0;
            mfc.soundTick = 0; //used for sound players to take turns
            mfc.soundPlayers = new Clip[MainFrameController.SOUNDPLAYERSCOUNT];
            for (int i = 0; i < MainFrameController.SOUNDPLAYERSCOUNT; i++) {
                mfc.soundPlayers[i] = AudioSystem.getClip();
            }

            mfc.collectedDots = 0;
            mfc.lives = 3;
            ++mfc.level;
        }

        mfc.mainPanel.removeAll();
        loading.setSize(350,60);
        placeLabel(loading, "Loading...", Color.yellow, new Point(75, 103),
                    new Font("Ravie", Font.BOLD, 48));
        levelLabel.setSize(300,60);
        placeLabel(levelLabel, "- Level " + Integer.toString(mfc.level) + " -", Color.red, new Point(118, 200),
                    new Font("Ravie", Font.BOLD,30));
        loading.setVisible(true);
        levelLabel.setVisible(true);
        mfc.mainPanel.revalidate();

        mfc.keyPressed1 = false;
        mfc.keyPressed2 = false;
        mfc.ghostsEaten = 0;
        mfc.keyCountdown1 = 0;
        mfc.keyCountdown2 = 0;
        mfc.killed = false;
        mfc.ticks = 0; // Counts tick to enable power pellets flashing and ghost flashing at the end of pac's excitement.
        mfc.freeGhosts = 1;
        mfc.ghostRelease = mfc.player2 ? 130 / 3 : (260 - mfc.level) / 3;
        mfc.eatEmTimer = 0; //timer for pacman's excitement
        pacLives = new JLabel[MAXLIVES];
        for (int i = 0; i < MAXLIVES; i++)
            pacLives[i] = new JLabel();

        // Yet empty fields of the array would redraw over top right corner of the map.
        // This way it draws empty tile on pacman's initial position tile which is empty by definition.
        for (int i = 0; i < LoadMap.RDPSIZE; i++)
            mfc.redrawPellets[i] = new Point(LoadMap.PACMANINITIALY, LoadMap.PACMANINITIALX);

        if (mfc.highScore == -1)
            mfc.highScore = HighScoreClass.loadHighScore(mfc.resourcesPath);
    }

    /**
     * Returns random color by fixing one channel to max, another to min and the last one chooses randomly.
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
     * @param restart Indicates whether is method triggered by lavel's restart or finish.
     * @throws IOException Propagation from highScore loading.
     * @throws LineUnavailableException Propagation from music playing.
     * @throws ExecutionException Propagation from Threading.
     * @throws InterruptedException Propagation from Threading.
     * @throws UnsupportedAudioFileException Propagation form music playing.
     */
    void playGame(boolean restart)
            throws IOException, LineUnavailableException, ExecutionException, InterruptedException, UnsupportedAudioFileException
    {
        PlayGameTask pgTask = new PlayGameTask(restart);
        pgTask.execute();
    }

    enum playGamePhase {PHASE1, PHASE2, PHASE3, INTERRUPTEDEXCEPTION, EXECUTIONEXCEPTION};

    class PlayGameTask extends SwingWorker<Void, playGamePhase> {
        boolean restart;

        PlayGameTask(boolean restart){
            this.restart = restart;
        }

        @Override
        public Void doInBackground(){
            try {
                publish(playGamePhase.PHASE1);
                AnimationTask animTask = new AnimationTask();
                animTask.execute();
                animTask.get();
                mfc.musicPlayer.stop();

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
        protected void process(List<playGamePhase> phases) {
            try {
                for (playGamePhase phase : phases) {
                    switch (phase) {
                        case PHASE1:
                            loadingAndInit();

                            if (!this.restart) {
                                if (mfc.music) {
                                    AudioInputStream sound = AudioSystem.getAudioInputStream(new File(mfc.resourcesPath + "/sounds/pacman_intermission.wav"));
                                    byte[] buffer1 = new byte[65536];
                                    sound.read(buffer1, 0, 65536);

                                    mfc.musicPlayer.open(sound.getFormat(), buffer1, 0, 65536);
                                    mfc.musicPlayer.start();
                                }
                            }
                            break;
                        case PHASE2:
                            if(mfc.music)
                                mfc.musicPlayer.close();

                            loadHud(mfc.lives - 2);
                            topGhostInTiles = new IntPair(mfc.map.item3.item1 - 1, mfc.map.item3.item2 - 1);
                            loadEntities();
                            ready = new JLabel();
                            ready.setSize(150, 30);
                            placeLabel(ready, "READY!", Color.yellow,
                                    new Point(((topGhostInTiles.item1 - 3) * LoadMap.TILESIZEINPXS) + 6,
                                            (topGhostInTiles.item2 + 6) * LoadMap.TILESIZEINPXS + 46),
                                    new Font("Ravie", Font.BOLD, 22));
                            ready.setVisible(true);

                            if (!restart)
                            {
                                mfc.collectedDots = 0;
                                mfc.mapFresh = deepCopy(mfc.map.item1);
                                if (mfc.level <= 13 && mfc.level > 1)
                                    mfc.ghostUpdater.setDelay(mfc.ghostUpdater.getDelay() - 5);
                            }
                            mfc.mainPanel.remove(loading);
                            mfc.mainPanel.remove(levelLabel);
                            mfc.mainFrame.repaint();
                            mfc.mainFrame.revalidate();
                            renderMap(mfc.mapFresh, mfc.map.item4);

                            if (mfc.music){
                                AudioInputStream sound = AudioSystem.getAudioInputStream(new File(mfc.resourcesPath + "/sounds/pacman_beginning.wav"));
                                byte[] buffer1 = new byte[65536];
                                sound.read(buffer1, 0, 65536);

                                mfc.musicPlayer.open(sound.getFormat(), buffer1, 0, 65536);
                                mfc.musicPlayer.start();
                            }
                            break;
                        case PHASE3:
                            if(mfc.music)
                                mfc.musicPlayer.close();

                            // It's possible that the player has pressed escape during the opening theme.
                            if (mfc.gameOn) {
                                ready.setVisible(false);
                                mfc.mainPanel.remove(ready);
                                mfc.mainPanel.revalidate();
                                // Corrects start positions of pacman and first ghost as they are located between the tiles at first.
                                entities.get(0).item3.setLocation(new Point(entities.get(0).item3.getLocation().x - 9,
                                        entities.get(0).item3.getLocation().y));
                                entities.get(1).item3.setLocation(new Point(entities.get(1).item3.getLocation().x - 9,
                                        entities.get(1).item3.getLocation().y));
                                if (mfc.music) {
                                    AudioInputStream sound = AudioSystem.getAudioInputStream(new File(mfc.resourcesPath + "/sounds/pacman_siren.wav"));
                                    byte[] buffer1 = new byte[65536];
                                    sound.read(buffer1, 0, 65536);

                                    mfc.musicPlayer.open(sound.getFormat(), buffer1, 0, 65536);
                                    mfc.musicPlayer.setLoopPoints(0, 9100);
                                    mfc.musicPlayer.loop(Clip.LOOP_CONTINUOUSLY);
                                    mfc.musicPlayer.start();
                                }
                                // Starts updaters that provides effect of main game cycle.
                                mfc.pacUpdater.setDelay(PACTIMER);
                                mfc.pacUpdater.setRepeats(true);
                                mfc.ghostUpdater.setDelay(!mfc.player2 ? (PACTIMER + 40 - (mfc.level > 13 ? 65
                                                                                                        : mfc.level * 5))
                                                                        : mfc.pacUpdater.getDelay() + 10);
                                mfc.ghostUpdater.setRepeats(true);
                                mfc.pacSmoothTimer.setDelay(mfc.pacUpdater.getDelay() / ((LoadMap.TILESIZEINPXS / 2) + 1));
                                mfc.pacSmoothTimer.setRepeats(true);
                                mfc.ghostSmoothTimer.setDelay(mfc.ghostUpdater.getDelay() / ((LoadMap.TILESIZEINPXS / 2) + 1));
                                mfc.ghostSmoothTimer.setRepeats(true);
//                                mfc.pacUpdater.start();
//                                mfc.ghostUpdater.start();
                            }
                            break;
                        case EXECUTIONEXCEPTION:
                            mfc.tryToSaveScore();
                            mfc.handleExceptions("java.util.concurrent.ExecutionException");
                            break;
                        case INTERRUPTEDEXCEPTION:
                            mfc.tryToSaveScore();
                            mfc.handleExceptions("java.lang.InterruptedException");
                            break;
                    }
                }
            }
            catch (LineUnavailableException | IOException | UnsupportedAudioFileException exception) {
                mfc.tryToSaveScore();
                mfc.handleExceptions(exception.toString());
            }
        }
    }

    /**
     * Represents single frame of loading animation
     */
    class AnimationFrame{
        boolean initialisation;
        boolean finished;
        ImageIcon pacmanImage;
        JLabel[] entities;
        Point[] locations;

        AnimationFrame(boolean initialisation, boolean finished, ImageIcon pacmanImage, JLabel[] entities, Point[] locations){
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
    class AnimationTask extends SwingWorker<Void, AnimationFrame>{
        @Override
        public Void doInBackground() throws InterruptedException{
            Random rndm = new Random();
            int elemCount = rndm.nextInt(5) + 1;
            int pacCount = 0;
            JLabel[] elements = new JLabel[elemCount];
            int startX = LoadMap.MAPWIDTHINTILES * LoadMap.TILESIZEINPXS;

            // FIRST ANIMATION: From right to left --------------------------------------------------------------------
            for (int i = 1; i <= elemCount; i++)
            {
                elements[i-1] = new JLabel();
                elements[i-1].setIcon(new ImageIcon(mfc.resourcesPath + "/textures/Entity" + Integer.toString(i) + "Left.png"));
                elements[i-1].setLocation(new Point(startX + (i == 0 ? 0 : (i + 4) * (2 * LoadMap.TILESIZEINPXS)),
                                                    (LoadMap.MAPHEIGHTINTILES / 2 + 6) * LoadMap.TILESIZEINPXS));
                elements[i-1].setSize(new Dimension(ENTITIESSIZEINPXS, ENTITIESSIZEINPXS));
            }
            publish(new AnimationFrame(true, false, null, elements, null));
            Thread.sleep(24);

            Point[] locations = new Point[elemCount];
            ImageIcon pacmanImage = new ImageIcon(mfc.resourcesPath + "/Textures/PacStart.png");
            for (int j = startX; j > -300; j -= 4)
            {
                ++pacCount;
                for (int i = 0; i < elemCount; i++)
                {
                    locations[i] = new Point(j + (i == 0 ? 0 : (i + 4) * (2 * LoadMap.TILESIZEINPXS)),
                                            (LoadMap.MAPHEIGHTINTILES / 2 + 6) * LoadMap.TILESIZEINPXS);
                    if (i == 0 && pacCount % 4 == 0)
                        if (pacCount % 8 == 0)
                            pacmanImage = new ImageIcon(mfc.resourcesPath + "/Textures/PacStart.png");
                        else
                            pacmanImage = new ImageIcon(mfc.resourcesPath + "/textures/Entity1Left.png");
                }

                publish(new AnimationFrame(false, false, pacmanImage, elements, locations));
                Thread.sleep(12);
            }

            // SECOND ANIMATION: From left to right -------------------------------------------------------------------
            if(mfc.level % 5 == 0){
                elements[0].setSize(new Dimension(ENTITIESSIZEINPXS * 2, ENTITIESSIZEINPXS * 2));
                elements[0].setLocation(new Point(elements[0].getLocation().x, elements[0].getLocation().y - ENTITIESSIZEINPXS));
                elements[0].setIcon(new ImageIcon(mfc.resourcesPath + "/textures/Entity1RightBig.png"));
                locations[0] = elements[0].getLocation();
                for (int i = 2; i <= elemCount; i++)
                    elements[i-1].setIcon(new ImageIcon(mfc.resourcesPath + "/textures/Entity" + Integer.toString(i) + "Right.png"));

                Thread.sleep(250);
                pacCount = 0;
                for (int j = 0; j < 260; ++j)
                {
                    ++pacCount;
                    for (int i = 0; i < elemCount; i++)
                    {
                        locations[i] = new Point(locations[i].x + 4, locations[i].y);
                        if (i == 0 && pacCount % 4 == 0)
                            if (pacCount % 8 == 0)
                                pacmanImage = new ImageIcon(mfc.resourcesPath + "/Textures/PacStartBig.png");
                            else
                                pacmanImage = new ImageIcon(mfc.resourcesPath + "/textures/Entity1RightBig.png");
                    }

                    publish(new AnimationFrame(false, false, pacmanImage, elements, locations));
                    Thread.sleep(12);
                }
            }

            publish(new AnimationFrame(false, true, pacmanImage, elements, locations));
            return null;
        }

        @Override
        protected void process(List<AnimationFrame> frames) {
            for (AnimationFrame frame : frames) {
                if (frame.initialisation) {
                    for (int i = 0; i < frame.entities.length; ++i) {
                        animLabelsIds[i] = mfc.mainPanel.getComponentCount();
                        mfc.mainPanel.add(frame.entities[i]);
                        mfc.mainPanel.getComponent(animLabelsIds[i]).setVisible(true);
                    }
                } else if (frame.finished) {
                    for (int i = frame.entities.length - 1; i >= 0; --i) {
                        mfc.mainPanel.remove(animLabelsIds[i]);
                    }
                } else {
                    for (int i = 0; i < frame.entities.length; ++i) {
                        mfc.mainPanel.getComponent(animLabelsIds[i]).setLocation(frame.locations[i].x, frame.locations[i].y);
                    }
                    frame.entities[0].setIcon(frame.pacmanImage);
                }
                mfc.mainPanel.revalidate();
                mfc.mainPanel.repaint();
            }
        }
    }

    //</editor-fold>
}
