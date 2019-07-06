package pacman_ultimater.project_base.gui_swing.ui.controller;

import pacman_ultimater.project_base.ai.DefaultAI;
import pacman_ultimater.project_base.core.*;
import pacman_ultimater.project_base.custom_utils.IntPair;
import pacman_ultimater.project_base.custom_utils.Quintet;
import pacman_ultimater.project_base.custom_utils.TimersListeners;
import pacman_ultimater.project_base.gui_swing.model.GameConsts;
import pacman_ultimater.project_base.gui_swing.model.GameModel;
import pacman_ultimater.project_base.gui_swing.model.MainFrameModel;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

/**
 * Controlls gameplay itself. Handles game loading via GameLoadController.
 */
class GameplayController implements IKeyDownHandler
{
    //<editor-fold desc="- VARIABLES Block -">

    private final static int KEYTICKS = 5;

    private boolean killNextTick = false;
    private final boolean[] teleported = new boolean[GameConsts.ENTITYCOUNT];
    private Direction.directionType newDirection1, newDirection2, newDirection3, newDirection4;
    private final Point[] entitiesPixDeltas = new Point[GameConsts.ENTITYCOUNT];
    private JLabel scoreLabel;
    private int newTileSize;
    private float minMult;
    private int newXPadding;
    private boolean ghostLegs, pacTick, resize;

    private TimersListeners timers;
    private GameLoadController glc;
    private final MainFrameModel model;
    private final GameModel vars;
    private final IGameOverHandler gameOverHandler;

    //</editor-fold>

    //<editor-fold desc="- GAMEPLAY Block -">

    GameplayController(MainFrameModel model, GameModel vars, IGameOverHandler gameOverHandler)
            throws LineUnavailableException
    {
        this.model = model;
        this.vars = vars;
        this.gameOverHandler = gameOverHandler;

        minMult = Math.min(vars.vMult * 1.05f, vars.hMult);
        newTileSize = (int)(LoadMap.TILESIZEINPXS * minMult);
        newXPadding = (int)(((vars.defSize.width * vars.hMult) - (LoadMap.MAPWIDTHINTILES * newTileSize)) /2);
        resize = minMult != 1;
        newDirection1 = newDirection2 = newDirection3 = newDirection4 = Direction.directionType.DIRECTION;
        pacTick = ghostLegs = false;
        glc = new GameLoadController(model, vars);
        timers = new TimersListeners(
                new TimerListener(timer_types.PACMAN), new TimerListener(timer_types.GHOST),
                new TimerListener(timer_types.PACMAN_SMOOTH), new TimerListener(timer_types.GHOST_SMOOTH));

        scoreLabel = new JLabel();
        scoreLabel.setForeground(new Color(139, 255, 255));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, (int)(22 * minMult)));
        scoreLabel.setVisible(false);
        scoreLabel.setSize(150,45);
        model.mainPanel.add(scoreLabel);
    }

    /**
     * Wraps public access around start game request on GameLoadController.
     */
    void loadGame()
    {
        sendGameLoadRequest(loadGameType.START);
    }

    private enum loadGameType {START, RESTART, NEXT}

    /**
     * Handles communication with GameLoadController.
     *
     * @param type Distinguishes between first game loading / restarting level / loading new level
     */
    private void sendGameLoadRequest(loadGameType type)
    {
        for(int i = 0; i < GameConsts.ENTITYCOUNT; ++i)
            entitiesPixDeltas[i] = new Point(0,0);

        switch(type){
            case NEXT:
                glc.loadNextLevel();
                break;
            case START:
                glc.loadGame(timers);
                break;
            case RESTART:
                glc.reloadLevel();
                break;
        }

        newDirection1 = Direction.directionType.DIRECTION;
        newDirection2 = Direction.directionType.DIRECTION;
        newDirection3 = Direction.directionType.DIRECTION;
        newDirection4 = Direction.directionType.DIRECTION;
    }

    /**
     * Function handling key pressing during gameplay.
     *
     * @param keyCode Identifies pressed key.
     */
    public void handleKey(int keyCode)
    {
        //Booleans keyPressedX are used to distinguish which of the players has pressed the key during Multiplayer.
        if (vars.player2 || vars.player3 || vars.player4) {
            if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_W
            || keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_S) {
                vars.keyPressed1 = true;
            } else {
                vars.keyPressed2 = true;
            }
        }

        vars.keyPressed3 = ((vars.player3 || vars.player4) && (keyCode == KeyEvent.VK_U || keyCode == KeyEvent.VK_H
                                            || keyCode == KeyEvent.VK_J || keyCode == KeyEvent.VK_K));
        vars.keyPressed4 = (vars.player4 && (keyCode == KeyEvent.VK_8 || keyCode == KeyEvent.VK_4
                                            || keyCode == KeyEvent.VK_5 || keyCode == KeyEvent.VK_6));

        if (!vars.keyPressed2 && !vars.keyPressed3 && !vars.keyPressed4) {
            vars.keyPressed1 = true;
        }

        if (keyCode == KeyEvent.VK_ESCAPE) {
            endGame();
        } else if (vars.keyPressed4) {
            switch(keyCode) {
                case KeyEvent.VK_4:
                    newDirection4 = Direction.directionType.LEFT;
                    break;
                case KeyEvent.VK_6:
                    newDirection4 = Direction.directionType.RIGHT;
                    break;
                case KeyEvent.VK_8:
                    newDirection4 = Direction.directionType.UP;
                    break;
                case KeyEvent.VK_5:
                    newDirection4 = Direction.directionType.DOWN;
                    break;
                default:
                    vars.keyPressed4 = false;
            }
        } else if (vars.keyPressed3) {
            switch(keyCode) {
                case KeyEvent.VK_H:
                    newDirection3 = Direction.directionType.LEFT;
                    break;
                case KeyEvent.VK_K:
                    newDirection3 = Direction.directionType.RIGHT;
                    break;
                case KeyEvent.VK_U:
                    newDirection3 = Direction.directionType.UP;
                    break;
                case KeyEvent.VK_J:
                    newDirection3 = Direction.directionType.DOWN;
                    break;
                default:
                    vars.keyPressed3 = false;
            }
        } else if (vars.keyPressed2) {
            switch(keyCode){
                case KeyEvent.VK_LEFT:
                    newDirection2 = Direction.directionType.LEFT;
                    break;
                case KeyEvent.VK_RIGHT:
                    newDirection2 = Direction.directionType.RIGHT;
                    break;
                case KeyEvent.VK_UP:
                    newDirection2 = Direction.directionType.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    newDirection2 = Direction.directionType.DOWN;
                    break;
                default:
                    vars.keyPressed2 = false;
            }
        } else {
            if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT)
                newDirection1 = Direction.directionType.LEFT;
            else if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP)
                newDirection1 = Direction.directionType.UP;
            else if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT)
                newDirection1 = Direction.directionType.RIGHT;
            else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN)
                newDirection1 = Direction.directionType.DOWN;
            else
                vars.keyPressed1 = false;
        }
    }

    /**
     * Function readrawing editor on window resizing
     */
    public void resize()
    {
        // Resize in case the map will be redrawn with different tile size
        if (Tile.tileSize != (int) (LoadMap.TILESIZEINPXS * Math.min(vars.vMult * 1.05, vars.hMult))) {
            resize = true;
        }
        glc.resizeLevel();
    }

    /**
     * Ends game by stopping game loop and enabling menu functionality.
     * Saves new highscore in case of beating the previous one by the player.
     * Generally destroys all of the forms's controls and loads them again with their default settings.
     */
    private void endGame()
    {
        glc = null;
        vars.gameOn = false;
        if(model.pacUpdater != null)
            model.pacUpdater.stop();
        if(model.pacSmoothTimer != null)
            model.pacSmoothTimer.stop();
        if(model.ghostUpdater != null)
            model.ghostUpdater.stop();
        if(model.ghostSmoothTimer != null)
            model.ghostSmoothTimer.stop();

        model.pacUpdater = null;
        model.ghostUpdater = null;
        model.pacSmoothTimer = null;
        model.ghostSmoothTimer = null;
        vars.mapFresh = null;
        if (vars.score >= vars.highScore) {
            try {
                HighScoreClass.tryToSaveScore(vars.player2 || vars.player3 || vars.player4, vars.score);
            }
            catch (IOException ignore){
                MainFrameController.fatalErrorMessage("score was not saved!");
            }
        }
        if(vars.musicPlayer != null) {
            vars.musicPlayer.stop();
            vars.musicPlayer.close();
        }
        model.mainPanel.repaint();
        gameOverHandler.handleGameOverRequest();
    }

    /**
     * Handles the event raised by unexcited pacman's contact with one of the ghosts.
     *
     * @throws LineUnavailableException Exception is to be handled by caller.
     * @throws IOException Exception is to be handled by caller.
     * @throws UnsupportedAudioFileException Exception is to be handled by caller.
     */
    private void killPacman()
        throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        model.pacUpdater.stop();
        model.ghostUpdater.stop();
        model.ghostSmoothTimer.stop();
        model.pacSmoothTimer.stop();
        if (vars.music)
            vars.playWithMusicPLayer(ClasspathFileReader.getPACMAN_DEATH(), false, 0 , 0);

        if (vars.player2 || vars.player3 || vars.player4) {
            vars.score2 += GameConsts.P2SCOREFORKILL;
        }
        SwingWorker<Void, Integer> sw = new SwingWorker<>()
        {
            @Override
            protected Void doInBackground()
            {
                try {
                    for (int i = 0; i < 12; ++i) {
                        publish(i);

                        Thread.sleep(100);
                    }

                    Thread.sleep(500);
                } catch (InterruptedException ignore) {}
                return null;
            }

            @Override
            protected void process(List<Integer> chunks)
            {
                Integer last = chunks.get(chunks.size() - 1);
                if (last < 11) {
                    vars.entities.get(0).item3.setIcon(new ImageIcon(
                            Textures.drawEntity(model.mainPanel, minMult, "Entity1", "LEFT", last)));
                } else {
                    vars.entities.get(0).item3.setIcon(new ImageIcon(
                            Textures.drawEntity(model.mainPanel, minMult, "PacExplode", "", last)));
                }
                model.mainPanel.repaint();
            }

            @Override
            protected void done()
            {
                vars.lives--;

                if (vars.lives > 0)
                    sendGameLoadRequest(loadGameType.RESTART);
                else
                    endGame();
                model.mainPanel.repaint();
                model.mainPanel.revalidate();
            }
        };
        sw.execute();
    }

    /**
     * Updates desired score box (highscore/player).
     *
     * @param score New Value to be set.
     * @param label Label whose value is to be set.
     */
    private void updateHud(int score, JLabel label)
    {
        label.setText(Integer.toString(score));
    }

    /**
     * Checks whether the direction the entity is aiming in is free.
     *
     * @param x X-axis delta.
     * @param y Y-axis delta.
     * @param entity The observed entity.
     * @return boolean value indicating emptiness of the observed tile.
     */
    private boolean isDirectionFree(int x, int y, Quintet<Integer, Integer, JLabel, Direction.directionType, DefaultAI> entity)
    {
        if (entity == null){
            return false;
        }

        int indexX = entity.item1 + x;
        int indexY = entity.item2 + y;

        return (indexX < 0 || indexX >= LoadMap.MAPWIDTHINTILES || indexY < 0 || indexY >= LoadMap.MAPHEIGHTINTILES
                || vars.mapFresh[indexY][indexX].tile == Tile.nType.FREE
                || vars.mapFresh[indexY][indexX].tile == Tile.nType.DOT
                || vars.mapFresh[indexY][indexX].tile == Tile.nType.POWERDOT);
    }

    /**
     * Tests whether the direction entity wants to move in is free and sets its direction and variable for saving direction to default value.
     *
     * @param newDirection Variable with stored target direction (set to default in case of success).
     * @param entity The observed entity.
     */
    private void SetToMove
    (Direction.directionType newDirection, Quintet<Integer, Integer, JLabel, Direction.directionType, DefaultAI> entity)
    {
        IntPair delta = Direction.directionToIntPair(newDirection);
        if (isDirectionFree(delta.item1, delta.item2, entity)) {
            entity.item4 = newDirection;
            newDirection = Direction.directionType.DIRECTION;
        }
    }

    /**
     * Checks whether the entity can continue in the direction it goes otherwise stops it.
     *
     * @param entity The updated entity.
     */
    private void canMove(Quintet<Integer, Integer, JLabel, Direction.directionType, DefaultAI> entity)
    {
        IntPair delta = Direction.directionToIntPair(entity.item4);
        if (!isDirectionFree(delta.item1, delta.item2, entity)) {
            entity.item5.prevDirection = entity.item4;
            entity.item4 = Direction.directionType.DIRECTION;
        }
    }

    /**
     * Combines entity's in-tiles movement with procedure handling its physical movement and right texture loading.
     *
     * @param entX New entity's in-tiles X-axis position.
     * @param entY New entity's in-tiles Y-axis position.
     * @param dX Physical X-axis change.
     * @param dY Physical Y-axis change.
     * @param entNum The updated entity's id (number).
     * @param entity The updated entity.
     */
    private void moveEntity(int entX, int entY, int dX, int dY, byte entNum,
                            Quintet<Integer, Integer, JLabel, Direction.directionType, DefaultAI> entity)
    {
        entity.item1 = entX;
        entity.item2 = entY;
        entitiesPixDeltas[entNum] = new Point(dX, dY);
    }

    /**
     * Moves entity in its saved direction and tells called function true or false in order to stop it from overwriting
     * entity's image in case of teleporting (is reverse move from the program's point of view).
     *
     * @param entity The updated entity.
     * @param entNum The updated entity's id (number).
     */
    private void moveIt(Quintet<Integer, Integer, JLabel, Direction.directionType, DefaultAI> entity, byte entNum)
    {
        switch (entity.item4) {
            case LEFT:
                if (entity.item1 > 0) {
                    teleported[entNum] = false;
                    moveEntity(entity.item1 - 1, entity.item2, - newTileSize, 0, entNum, entity);
                } else {
                    teleported[entNum] = true;
                    moveEntity(LoadMap.MAPWIDTHINTILES - 1, entity.item2,
                            (LoadMap.MAPWIDTHINTILES - 1) * newTileSize, 0, entNum, entity);
                }
                break;
            case RIGHT:
                if (entity.item1 < LoadMap.MAPWIDTHINTILES - 1) {
                    teleported[entNum] = false;
                    moveEntity(entity.item1 + 1, entity.item2, newTileSize, 0, entNum, entity);
                } else {
                    teleported[entNum] = true;
                    moveEntity(0, entity.item2,
                            -(LoadMap.MAPWIDTHINTILES - 1) * newTileSize, 0, entNum, entity);
                }
                break;
            case UP:
                if (entity.item2 > 0) {
                    teleported[entNum] = false;
                    moveEntity(entity.item1, entity.item2 - 1, 0, - newTileSize, entNum, entity);
                } else {
                    teleported[entNum] = true;
                    moveEntity(entity.item1, LoadMap.MAPHEIGHTINTILES - 1,
                            0, (LoadMap.MAPHEIGHTINTILES - 1) * newTileSize, entNum, entity);
                }
                break;
            case DOWN:
                if (entity.item2 < LoadMap.MAPHEIGHTINTILES - 1) {
                    teleported[entNum] = false;
                    moveEntity(entity.item1, entity.item2 + 1, 0, newTileSize, entNum, entity);
                } else {
                    teleported[entNum] = true;
                    moveEntity(entity.item1, 0,
                            0, -(LoadMap.MAPHEIGHTINTILES - 1) * newTileSize, entNum, entity);
                }
                break;
            default:
                break;
        }
    }

    /**
     * Checks whether the position is a crossroad.
     *
     * @param x X-axis position.
     * @param y Y-axis position.
     * @return Boolean indicating crossroad.
     */
    private boolean isAtCrossroad(int x, int y)
    {
        int turns = 0;
        for (int j = 0; j < 2; j++) {
            for (int i = -1; i < 2; i += 2) {
                int indexY = y + (j == 0 ? i : 0);
                int indexX = x + (j == 1 ? i : 0);
                if (indexY < 0 || indexY >= LoadMap.MAPHEIGHTINTILES || indexX < 0 || indexX >= LoadMap.MAPWIDTHINTILES)
                    turns = 1;
                else if (vars.map.item1[indexY][indexX].tile == Tile.nType.FREE ||
                        vars.map.item1[indexY][indexX].tile == Tile.nType.DOT ||
                        vars.map.item1[indexY][indexX].tile == Tile.nType.POWERDOT)
                    turns++;
            }
        }
        return (turns >= 3);
    }

    /**
     * Loads right image depending on the game situation and direction.
     *
     * @param entity The updated entity.
     */
    private void updatePicture(Quintet<Integer, Integer, JLabel, Direction.directionType, DefaultAI> entity)
    {
        if (resize) {
            int newEntitySize = (int) (GameConsts.ENTITIESSIZEINPXS * minMult);
            entity.item3.setSize(newEntitySize, newEntitySize);
            entity.item3.setLocation(
                    (int)(entity.item1 * newTileSize + newXPadding - (14 * minMult)),
                    entity.item2 * newTileSize + (int)(30 * minMult) + 12);
        }

        // Last Line of if statement ensures ghost flashing at the end of pacman excited mode.
        if ((vars.eatEmTimer <= 0 || entity.item5.state != DefaultAI.nType.CANBEEATEN
        || entity.item3.getName().equals("Entity1")) && entity.item5.state != DefaultAI.nType.EATEN)
        {
            if (entity.item3.getName().equals("Entity1")) {
                entity.item3.setIcon(new ImageIcon(Textures.drawEntity(
                        model.mainPanel, minMult, entity.item3.getName(), pacTick ? entity.item4.toString() : "DIRECTION", 0)));
            } else {
                entity.item3.setIcon(new ImageIcon(Textures.drawEntity(
                        model.mainPanel, minMult, entity.item3.getName(), entity.item4.toString(), ghostLegs ? 0 : 1)));
            }
        } else if (entity.item5.state == DefaultAI.nType.EATEN) {
            entity.item3.setIcon(new ImageIcon(Textures.drawEntity(
                    model.mainPanel, minMult, "Eyes", entity.item4.toString(),0)));
        } else {
            if (vars.ticks % 3 == 0 && vars.eatEmTimer < GameConsts.GHOSTFLASHINGSTART) {
                entity.item3.setIcon(new ImageIcon(Textures.drawEntity(
                        model.mainPanel, minMult, "CanBeEaten", "", ghostLegs ? 2 : 3)));
            } else {
                entity.item3.setIcon(new ImageIcon(Textures.drawEntity(
                        model.mainPanel, minMult, "CanBeEaten", "", ghostLegs ? 0 : 1)));
            }
        }
    }

    /**
     * Places picture to the place it should be according to it's tile indexes.
     * Finishes one cycle of smooth move and enables start of another cycle.
     *
     * @param entity Entity whose picture is to be corrected.
     */
    private void correctPicture(Quintet<Integer, Integer, JLabel, Direction.directionType, DefaultAI> entity)
    {
        int newEntitySize = (int)(GameConsts.ENTITIESSIZEINPXS * minMult);
        entity.item3.setSize(newEntitySize, newEntitySize);
        entity.item3.setLocation(
                (int)(entity.item1 * newTileSize + newXPadding - (14 * minMult)),
                entity.item2 * newTileSize + (int)(30 * minMult) + 12);
        if (entity.item4 != Direction.directionType.DIRECTION)
            updatePicture(entity);
    }

    /**
     * Function that moves all of the entities and checks whether the pacman and a ghost have met.
     *
     * @param isPacman Boolean indicating whether to update pacman or ghosts.
     */
    private void updateMove(boolean isPacman)
    {
        // Direction of entities controlled by players are updated via newDirection variables.
        // Direction of UI entities is set through AI algorithms.
        if (isPacman) {
            if (newDirection1 != Direction.directionType.DIRECTION)
                SetToMove(newDirection1, vars.entities.get(0));

            // Places picture to the place it should be according to it's tile indexes.
            // Finishes one cycle of smooth move and enables start of another cycle.
            correctPicture(vars.entities.get(0));

            canMove(vars.entities.get(0));
            moveIt(vars.entities.get(0), (byte)0);
        } else {
            if (newDirection2 != Direction.directionType.DIRECTION) {
                SetToMove(newDirection2, vars.entities.get(1));
            }
            if (newDirection3 != Direction.directionType.DIRECTION) {
                SetToMove(newDirection3, vars.entities.get(2));
            }
            if (newDirection4 != Direction.directionType.DIRECTION) {
                SetToMove(newDirection4, vars.entities.get(3));
            }

            for (byte i = 1; i <= vars.freeGhosts; i++) {
                // Places picture to the place it should be according to it's tile indexes.
                // Finishes one cycle of smooth move and enables start of another cycle.
                Quintet<Integer, Integer, JLabel, Direction.directionType, DefaultAI> entity = vars.entities.get(i);
                correctPicture(entity);

                // if entity is AI, creates new instance with direction selected by AI algorithm.
                if (((!vars.player2 || i > 1) && (!vars.player3 || i > 2) && (!vars.player4 || i > 3))
                && (entity.item4 == Direction.directionType.DIRECTION
                || isAtCrossroad(entity.item1, entity.item2)))
                {
                    entity.item4 = entity.item5.NextStep(
                            new IntPair(entity.item1, entity.item2),
                            entity.item5.state == DefaultAI.nType.EATEN ? vars.topGhostInTiles
                                : new IntPair(vars.entities.get(0).item1, vars.entities.get(0).item2),
                            entity.item4,
                            vars.entities.get(0).item4 == Direction.directionType.DIRECTION
                            ? vars.entities.get(0).item5.prevDirection : vars.entities.get(0).item4,
                            vars.map.item1);
                }

                canMove(entity);
                moveIt(entity, i);

                if (entity.item5.state == DefaultAI.nType.EATEN && entity.item1 == vars.topGhostInTiles.item1
                && entity.item2 == vars.topGhostInTiles.item2) {
                    if (vars.chaseMode > 0) {
                        entity.item5.state = DefaultAI.nType.HOSTILEATTACK;
                    } else if (vars.scatterMode > 0) {
                        entity.item5.state = DefaultAI.nType.HOSTILERETREAT;
                    }
                }
            }
        }
    }

    /**
     * Provides timer for pacman's excitement and changes all of the ghost back to normal at the end.
     *
     * @throws IOException Exception is to be handled by caller.
     * @throws UnsupportedAudioFileException Exception is to be handled by caller.
     * @throws LineUnavailableException Exception is to be handled by caller.
     */
    private void updateEatEmTimer()
        throws IOException, UnsupportedAudioFileException, LineUnavailableException
    {
        if (vars.eatEmTimer > 1)
            --vars.eatEmTimer;
        else if (vars.eatEmTimer == 1) {
            if (vars.music) {
                vars.playWithMusicPLayer(ClasspathFileReader.getPACMAN_SIREN(), true, 0, 9100);
            }

            for (int i = 1; i < 5; i++) {
                if ((vars.player2 && i == 1) || (vars.player3 && i == 2) || (vars.player4 && i == 3)) {
                    vars.entities.get(i).item5.state = DefaultAI.nType.NOAI;
                } else {
                    if (vars.entities.get(i).item5.state != DefaultAI.nType.EATEN) {
                        if (vars.chaseMode > 0) {
                            vars.entities.get(i).item5.state = DefaultAI.nType.HOSTILEATTACK;
                        } else if (vars.scatterMode > 0) {
                            vars.entities.get(i).item5.state = DefaultAI.nType.HOSTILERETREAT;
                        }
                    }
                }
            }
            vars.ghostsEaten = 0;
            model.ghostUpdater.setDelay(!(vars.player2 || vars.player3 || vars.player4) ?
                    (GameConsts.PACTIMER + 40 - (vars.level > 13 ? 65 : vars.level * 5))
                    : model.pacUpdater.getDelay() + (vars.player2 ? 10 : vars.player3 ? 20 : 30));
            model.ghostSmoothTimer.setDelay(model.ghostUpdater.getDelay() / ((newTileSize / 2) + 1));
            vars.eatEmTimer = 0;
        }
    }

    /**
     * Proides functionality for fruits - appearing, disappearing, collecting
     */
    private void updateFruit()
    {
        if (vars.collectedDots >= vars.map.item2/2 && vars.fruitLife < GameConsts.FRUITLIFE)
        {
            if (vars.collectedDots >= vars.map.item2/2 && vars.fruitLife == 0) {
                vars.fruitLabel.setIcon(new ImageIcon(
                        Textures.drawFruit(model.mainPanel, minMult, vars.level)
                ));
                vars.fruitLabel.setVisible(true);
            }

            if (vars.entities.get(0).item1 == 14 && vars.entities.get(0).item2 == 17 && vars.fruitLife > 0){
                vars.fruitLife = GameConsts.FRUITLIFE;
                int score = vars.level > 12 ? 5000 : vars.level > 10 ? 3000 : vars.level > 8 ? 2000
                        : vars.level > 6 ? 1000 : vars.level > 4 ? 700 : vars.level > 2 ? 500 : vars.level > 1 ? 300
                        : 100;

                vars.score += score;
                updateHud(vars.score, vars.scoreBox);
                scoreLabel.setText(Integer.toString(score));
                scoreLabel.setFont(new Font("Arial", Font.BOLD, (int)(22 * minMult)));
                Point location = vars.fruitLabel.getLocation();
                scoreLabel.setLocation(location.x, location.y);

                SwingWorker<Void, Boolean> sw = new SwingWorker<>(){
                    @Override
                    protected void process(List<Boolean> chunks)
                    {
                        Boolean last = chunks.get(chunks.size() - 1);
                        if (last) {
                            scoreLabel.setVisible(false);
                        } else {
                            scoreLabel.setVisible(true);
                        }

                        model.mainPanel.repaint();
                    }

                    @Override
                    protected void done()
                    {
                        model.pacUpdater.start();
                        model.ghostUpdater.start();
                        model.ghostSmoothTimer.start();
                        model.pacSmoothTimer.start();
                    }

                    @Override
                    protected Void doInBackground()
                    {
                        publish(false);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ignore) {}
                        publish(true);
                        return null;
                    }
                };
                sw.execute();

                if (vars.sound) {
                    vars.playWithSoundPlayer(GameConsts.EATGHOSTSOUNDPLAYERID);
                }

                vars.collectedFruits.add(vars.level);
                vars.fruitLabel.setVisible(false);
                vars.fruitHud.setIcon(new ImageIcon(Textures.getFruits(model.mainPanel, minMult, vars.collectedFruits)));
            }

            ++vars.fruitLife;
        }
    }

    /**
     * Checks whether pacman is not on a tile with pellet.
     * if so, plays the sound and increases number of collected pellets and score.
     * Also enables pacman's excitement via EatEmTimer.
     *
     * @throws UnsupportedAudioFileException Exception is to be handled by caller.
     * @throws IOException Exception is to be handled by caller.
     * @throws LineUnavailableException Exception is to be handled by caller.
     */
    private void updateEatPellet()
        throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        if (vars.mapFresh[vars.entities.get(0).item2][vars.entities.get(0).item1].tile == Tile.nType.DOT ||
                vars.mapFresh[vars.entities.get(0).item2][vars.entities.get(0).item1].tile == Tile.nType.POWERDOT) {
            if (vars.sound) {
                vars.playWithSoundPlayer(0);
                vars.soundTick = (vars.soundTick + 1) % GameConsts.SOUNDPLAYERSCOUNT;
            }

            ++vars.collectedDots;
            if (vars.mapFresh[vars.entities.get(0).item2][vars.entities.get(0).item1].tile == Tile.nType.DOT)
                vars.score += GameConsts.PELLETSCORE;
            else {
                vars.score += GameConsts.POWERPELLETSCORE;
                if (vars.music)
                    vars.playWithMusicPLayer(ClasspathFileReader.getPACMAN_POWERSIREN(), true, 0 , 26320);

                //Pacman's excitement lasts shorter each level
                vars.eatEmTimer = (vars.player2 || vars.player3 || vars.player4) ? (3 * GameConsts.BASEEATEMTIMER) / 5
                                               : (vars.level > 12 ? 35 : (GameConsts.BASEEATEMTIMER - vars.level * 5));
                vars.ghostsEaten = 0;
                model.ghostUpdater.setDelay((GameConsts.PACTIMER + 40 - (vars.level > 13 ? 65 : vars.level * 5)) * 2);
                model.ghostSmoothTimer.setDelay(model.ghostUpdater.getDelay() / ((newTileSize / 2) + 1));
                //Return all of the ghost to normal state to be able to be eaten again later
                for (int i = 1; i < 5; i++)
                    vars.entities.get(i).item5.state = DefaultAI.nType.CANBEEATEN;
            }

            //Deletes pellet from the tile
            vars.mapFresh[vars.entities.get(0).item2][vars.entities.get(0).item1].tile = Tile.nType.FREE;
            vars.mapFresh[vars.entities.get(0).item2][vars.entities.get(0).item1].FreeTile(
                    vars.bufferGraphics, new Point(vars.entities.get(0).item1 * newTileSize,
                    (vars.entities.get(0).item2 + 3) * newTileSize), Color.BLACK);

            if (vars.score > vars.highScore) {
                vars.highScore = vars.score;
                updateHud(vars.highScore, vars.highScoreBox);
            }
            updateHud(vars.score, vars.scoreBox);
        }

        blink();
    }

    /**
     * Places ghost specified by number out of ghost house and sets its start direction.
     * Also resets timer for ghost releasing.
     */
    private void setGhostFree()
    {
        Quintet<Integer, Integer, JLabel, Direction.directionType, DefaultAI> entity = vars.entities.get(vars.freeGhosts + 1);
        entity.item1 = vars.topGhostInTiles.item1;
        entity.item2 = vars.topGhostInTiles.item2;
        entity.item3.setLocation(new Point(
                entity.item1 * newTileSize + newXPadding - 5,
                entity.item2 * newTileSize + (int)(42 * minMult)));
        entity.item4 = Direction.directionType.LEFT;
        vars.ghostRelease = vars.player2 ? (GameConsts.BASEGHOSTRELEASETIMER / 5)
                                : vars.player3 ? (GameConsts.BASEGHOSTRELEASETIMER / 7)
                                    : vars.player4 ? (GameConsts.BASEGHOSTRELEASETIMER / 10)
                                        : (GameConsts.BASEGHOSTRELEASETIMER - vars.level) / 3;
        vars.freeGhosts++;
    }

    /**
     * Ghost forcibly revert their direction when state is changed from HOSTILE ATTACK to HOSTILE RETREAT and vice versa
     */
    private void revertDirection(Quintet<Integer, Integer, JLabel, Direction.directionType, DefaultAI> entity)
    {
        if (entity.item4 != Direction.directionType.DIRECTION) {
            entity.item4 = Direction.directionType.values()[(entity.item4.ordinal() + 2) % 4];
            canMove(entity);
        }
    }

    /**
     * Checks if distance in tiles between pacman and each entity is bigger than 1.
     *
     * @throws IOException Exception is to be handled by caller.
     * @throws LineUnavailableException Exception is to be handled by caller.
     * @throws UnsupportedAudioFileException Exception is to be handled by caller.
     */
    private void checkCollision()
        throws IOException, LineUnavailableException, UnsupportedAudioFileException
    {
        for (int i = 1; i < GameConsts.ENTITYCOUNT; i++) {
            Quintet<Integer, Integer, JLabel, Direction.directionType, DefaultAI> entity = vars.entities.get(i);
            if ((Math.abs(entity.item1 - vars.entities.get(0).item1)
                    + Math.abs(entity.item2 - vars.entities.get(0).item2)) == 0
            || ((Math.abs(entity.item1 - vars.entities.get(0).item1)
                    + Math.abs(entity.item2 - vars.entities.get(0).item2)) == 1
                && entity.item4.ordinal() == (vars.entities.get(0).item4.ordinal() + 2) % 4))
            {
                if (entity.item5.state != DefaultAI.nType.EATEN && entity.item5.state != DefaultAI.nType.CANBEEATEN) {
                    if (killNextTick) {
                        killNextTick = false;
                        killPacman();
                        vars.killed = true;
                        return;
                    } else
                        killNextTick = true;
                }
                // In case of pacman's excitement and if the ghost is not already eaten
                // changes the ghost's state to eaten and increases player's score.
                else if (entity.item5.state != DefaultAI.nType.EATEN) {
                    if (vars.sound)
                        vars.playWithSoundPlayer(GameConsts.EATGHOSTSOUNDPLAYERID);
                    if (vars.music) {
                        vars.playWithMusicPLayer(ClasspathFileReader.getPACMAN_EATENSIREN(), true, 0, 17800);
                    }

                    model.pacUpdater.stop();
                    model.ghostUpdater.stop();
                    model.ghostSmoothTimer.stop();
                    model.pacSmoothTimer.stop();

                    SwingWorker<Void, Boolean> sw = new SwingWorker<>()
                    {
                        @Override
                        protected void process(List<Boolean> chunks)
                        {
                            Boolean last = chunks.get(chunks.size() - 1);
                            if (last) {
                                scoreLabel.setVisible(false);
                            } else {
                                scoreLabel.setVisible(true);
                            }

                            model.mainPanel.repaint();
                        }

                        @Override
                        protected void done()
                        {
                            model.pacUpdater.start();
                            model.ghostUpdater.start();
                            model.ghostSmoothTimer.start();
                            model.pacSmoothTimer.start();
                        }

                        @Override
                        protected Void doInBackground()
                        {
                            publish(false);
                            try {
                                Thread.sleep(600);
                            } catch (InterruptedException ignore) {
                            }
                            publish(true);
                            return null;
                        }
                    };

                    ++vars.ghostsEaten;
                    int bonusScore = GameConsts.GHOSTEATBASESCORE * (vars.ghostsEaten < 3 ? vars.ghostsEaten
                            : (vars.ghostsEaten < 4 ? 4 : 8));

                    scoreLabel.setFont(new Font("Arial", Font.BOLD, (int) (22 * minMult)));
                    scoreLabel.setText(Integer.toString(bonusScore));
                    scoreLabel.setLocation(entity.item3.getLocation());

                    sw.execute();

                    vars.score += bonusScore;
                    updateHud(vars.score, vars.scoreBox);
                    if (vars.ghostsEaten == 4 && vars.lives < GameConsts.MAXLIVES - 1) {
                        vars.pacLives[vars.lives - 1].setVisible(true);
                        ++vars.lives;
                        if (vars.sound)
                            vars.playWithSoundPlayer(GameConsts.EXTRAPACSOUNDPLAYERID);
                    }
                    entity.item5.state = DefaultAI.nType.EATEN;
                }
            }
        }
    }

    /**
     * Body of the update mechanism. Selectively updates entities and map.
     *
     * @param isPacman Boolean indicating whether to update pacman or ghosts.
     * @throws IOException Exception is to be handled by caller.
     * @throws UnsupportedAudioFileException Exception is to be handled by caller.
     * @throws LineUnavailableException Exception is to be handled by caller.
     */
    private void updateGame(boolean isPacman)
        throws IOException, UnsupportedAudioFileException, LineUnavailableException
    {
        updateMove(isPacman);
        checkCollision();

        if (vars.killed)
            return;

        if (isPacman) {
            updateEatEmTimer();
            updateFruit();
            updateEatPellet();

            // Gives player one extra life at reaching score of BonusLifeScore.
            if (vars.score >= GameConsts.BONUSLIFESCORE && !vars.extraLifeGiven) {
                vars.extraLifeGiven = true;
                if (vars.sound)
                    vars.playWithSoundPlayer(GameConsts.EXTRAPACSOUNDPLAYERID);

                vars.pacLives[vars.lives - 1].setVisible(true);
                ++vars.lives;
            }
            model.mainPanel.repaint();
            model.mainPanel.revalidate();
        } else {
            vars.ticks = ++vars.ticks % 6;
            //Handles successive ghost releasing
            if (vars.ghostRelease <= 0 && vars.freeGhosts < 4)
                setGhostFree();
            if (vars.ghostRelease > 0 && vars.freeGhosts < 4)
                --vars.ghostRelease;
        }
    }

    /**
     * Handles periodical blinking of 1up, 2up and Power Pellets.
     */
    private void blink()
    {
        if (vars.ticks % 3 == 0) {
            vars.up1.setVisible(false);
            if (vars.player2 || vars.player3 || vars.player4)
                vars.up2.setVisible(false);
        } else {
            vars.up1.setVisible(true);
            if (vars.player2 || vars.player3 || vars.player4)
                vars.up2.setVisible(true);
        }

        for (int i = 0; i < vars.map.item5.size(); ++i)
            if (vars.mapFresh[vars.map.item5.get(i).x][vars.map.item5.get(i).y].tile == Tile.nType.POWERDOT) {
                if (vars.ticks % 2 == 0)
                    vars.mapFresh[vars.map.item5.get(i).x][vars.map.item5.get(i).y].FreeTile(vars.bufferGraphics,
                            new Point(vars.map.item5.get(i).y * newTileSize,
                                    (vars.map.item5.get(i).x + 3) * newTileSize), Color.BLACK);
                else
                    vars.mapFresh[vars.map.item5.get(i).x][vars.map.item5.get(i).y].DrawTile(vars.bufferGraphics,
                            new Point(vars.map.item5.get(i).y * newTileSize,
                                    (vars.map.item5.get(i).x + 3) * newTileSize), Color.BLACK);
            }
    }

    /**
     * Checks whether the direction player intends to move in is free.
     * Nulls or continues in countdown otherwise based on result.
     *
     * @param direction Direction player intends to move in.
     * @param keyCountdown Number of tries left.
     */
    private void keyCountAndDir(Direction.directionType direction, Integer keyCountdown)
    {
        if (direction == Direction.directionType.DIRECTION && keyCountdown != 0)
            keyCountdown = 0;
        else if (direction != Direction.directionType.DIRECTION && keyCountdown > 1)
            --keyCountdown;
        else if (direction != Direction.directionType.DIRECTION && keyCountdown == 1) {
            direction = Direction.directionType.DIRECTION;
            keyCountdown = 0;
        }
    }

    /**
     * Topmost layer of instructions executed each frame.
     *
     * @param isPacman Boolean indicating whether to update pacman or ghosts.
     */
    private void gameLoop(boolean isPacman)
    {
        // Update tile size in case of resizing occured
        if (resize){
            minMult = Math.min(vars.vMult  * 1.05f, vars.hMult);
            newTileSize = (int)(LoadMap.TILESIZEINPXS * minMult);
        }
        newXPadding = (int)(((vars.defSize.width * vars.hMult) - (LoadMap.MAPWIDTHINTILES * newTileSize)) /2);
        vars.gameMap.setLocation(newXPadding - 8, vars.gameMap.getY());

        // In case that one of the players have pushed a valid key, countdown, which represents
        // the number tiles remaining until the information about the pushed button is lost, is started.
        if (vars.keyPressed1) {
            vars.keyCountdown1 = KEYTICKS;
        }
        if (vars.keyPressed2) {
            vars.keyCountdown2 = KEYTICKS;
        }
        if (vars.keyPressed3) {
            vars.keyCountdown3 = KEYTICKS;
        }
        if (vars.keyPressed4) {
            vars.keyCountdown4 = KEYTICKS;
        }
        vars.keyPressed1 = vars.keyPressed2 = vars.keyPressed3 = vars.keyPressed4 = false;

        try {
            updateGame(isPacman);
        }
        catch (IOException | UnsupportedAudioFileException | LineUnavailableException ignore) {
            // TODO: Handle game exception.
        }
        if(!vars.killed) {
            keyCountAndDir(newDirection1, vars.keyCountdown1);
            if (vars.player2) {
                keyCountAndDir(newDirection2, vars.keyCountdown2);
            }
            if (vars.player3) {
                keyCountAndDir(newDirection3, vars.keyCountdown3);
            }
            if (vars.player4) {
                keyCountAndDir(newDirection4, vars.keyCountdown4);
            }

            // Checks if the player has already collected all the pellets.
            // In such case in relation to level and game mode, plays another level or ends the game.
            if (vars.collectedDots >= vars.map.item2)
                endLevel();

            if (isPacman) {
                model.pacSmoothTimer.start();
            } else
                model.ghostSmoothTimer.start();
        }
        resize = false;
    }

    private void endLevel()
    {
        model.pacUpdater.stop();
        model.ghostUpdater.stop();
        vars.musicPlayer.stop();
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0)
                glc.renderMap(vars.mapFresh, Color.white);
            else
                glc.renderMap(vars.mapFresh, vars.mapColor);

            model.mainPanel.repaint();
            model.mainPanel.revalidate();
        }

        vars.level++;
        if (vars.level < GameConsts.MAXLEVEL && !(vars.player2 || vars.player3 || vars.player4))
            sendGameLoadRequest(loadGameType.NEXT);
        else
            endGame();
    }

    /**
     * Enumeration for identifying timers.
     */
    enum timer_types {
        /**
         * Timer of pacman's from tile to tile movement.
         */
        PACMAN,
        /**
         * Timer of ghosts' from tile to tile movement.
         */
        GHOST,
        /**
         * Timer for simulating pacman's smooth movement between tiles.
         */
        PACMAN_SMOOTH,
        /**
         * Timer for simulating ghosts' smooth movement between tiles.
         */
        GHOST_SMOOTH }

    /**
     * Handles events raised by timers and distributes them to appropriate functions.
     */
    private class TimerListener implements ActionListener
    {
        final timer_types timer_type;

        private TimerListener(timer_types type){
            timer_type = type;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            switch(timer_type) {
                case PACMAN:
                    pacUpdater_Tick();
                    break;
                case GHOST:
                    ghostUpdater_Tick();
                    break;
                case PACMAN_SMOOTH:
                    pacSmoothTimer_Tick();
                    break;
                case GHOST_SMOOTH:
                    ghostSmoothTimer_Tick();
                    break;
            }
        }
    }

    /**
     * Creates illusion of game loop.
     * Handles event raised by pacman's timer's periodical ticks.
     */
    private void pacUpdater_Tick()
    {
        model.pacSmoothTimer.stop();
        pacTick = !pacTick;
        gameLoop(true);
    }

    /**
     * Creates illusion of game loop.
     * Handles event raised by ghosts' timer's periodical ticks.
     */
    private void ghostUpdater_Tick()
    {
        model.ghostSmoothTimer.stop();
        recountBehaviourModes();
        ghostLegs = !ghostLegs;
        gameLoop(false);
    }

    /**
     * Recounts variables used to determine ghost behaviour mode
     */
    private void recountBehaviourModes()
    {
        if (vars.scatterMode > 0 ) {
            --vars.scatterMode;
            if (vars.scatterMode == 0) {
                vars.chaseMode = 20000 / model.ghostUpdater.getDelay();
                for (Quintet<Integer, Integer, JLabel, Direction.directionType, DefaultAI> entity : vars.entities)
                {
                    if (entity.item5.state == DefaultAI.nType.HOSTILERETREAT) {
                        entity.item5.state = DefaultAI.nType.HOSTILEATTACK;
                        revertDirection(entity);
                    }
                }
            }
        } else if (vars.chaseMode > 0 && vars.modeSwitchCounter < 4) {
            --vars.chaseMode;
            if (vars.chaseMode == 0) {
                ++vars.modeSwitchCounter;
                vars.scatterMode = (vars.modeSwitchCounter < 2 ? 7000 : 5000) / model.ghostUpdater.getDelay();
                for (Quintet<Integer, Integer, JLabel, Direction.directionType, DefaultAI> entity : vars.entities)
                {
                    if (entity.item5.state == DefaultAI.nType.HOSTILEATTACK) {
                        entity.item5.state = DefaultAI.nType.HOSTILERETREAT;
                        revertDirection(entity);
                    }
                }
            }
        }
    }

    /**
     * Handles animating of pacman's translation between old and the new tile.
     */
    private void pacSmoothTimer_Tick()
    {
        if (vars.entities.get(0).item4 != Direction.directionType.DIRECTION && !teleported[0]) {
            Point d = getDeltas((byte)0);

            // Last part of smooth move is done at the beginning of each update cycle.
            if (entitiesPixDeltas[0].x <= 1 && entitiesPixDeltas[0].y <= 1
                && entitiesPixDeltas[0].x >= -1 && entitiesPixDeltas[0].y >= -1)
            {
                model.pacSmoothTimer.stop();
            }
            else
                vars.entities.get(0).item3.setLocation(new Point(
                        vars.entities.get(0).item3.getLocation().x + d.x,
                        vars.entities.get(0).item3.getLocation().y + d.y));
        }
    }

    /**
     * Handles animating of ghosts' translation between old and the new tile.
     */
    private void ghostSmoothTimer_Tick()
    {
        for (byte i = 1; i <= vars.freeGhosts; i++)
            if (vars.entities.get(i).item4 != Direction.directionType.DIRECTION && !teleported[i]) {
                Point d = getDeltas(i);

                // Last part of smooth move is done at the beginning of each update cycle.
                if (entitiesPixDeltas[i].x <= 1 && entitiesPixDeltas[i].y <= 1
                && entitiesPixDeltas[i].x >= -1 && entitiesPixDeltas[i].y >= -1)
                    model.ghostSmoothTimer.stop();
                else
                    vars.entities.get(i).item3.setLocation(new Point(
                        vars.entities.get(i).item3.getLocation().x + d.x,
                        vars.entities.get(i).item3.getLocation().y + d.y));
            }
    }

    /**
     * Gets translation deltas for entity specified by input index.
     *
     * @param index Input index identifying entity.
     * @return Point of deltas in X and Y axes.
     */
    private Point getDeltas(byte index)
    {
        int dX = 0, dY = 0;
        if (entitiesPixDeltas[index].x >= 2) {
            dX = 2;
            entitiesPixDeltas[index].x -= 2;
        } else if (entitiesPixDeltas[index].x <= -2) {
            dX = -2;
            entitiesPixDeltas[index].x += 2;
        } else if (entitiesPixDeltas[index].y >= 2) {
            dY = 2;
            entitiesPixDeltas[index].y -= 2;
        } else if (entitiesPixDeltas[index].y <= -2) {
            dY = -2;
            entitiesPixDeltas[index].y += 2;
        }

        return new Point(dX, dY);
    }

    //</editor-fold>
}
