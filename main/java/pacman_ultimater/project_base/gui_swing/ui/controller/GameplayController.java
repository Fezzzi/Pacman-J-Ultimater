package pacman_ultimater.project_base.gui_swing.ui.controller;

import pacman_ultimater.project_base.core.Direction;
import pacman_ultimater.project_base.core.HighScoreClass;
import pacman_ultimater.project_base.core.IGameOverHandler;
import pacman_ultimater.project_base.core.IKeyDownHandler;
import pacman_ultimater.project_base.custom_utils.TimersListeners;
import pacman_ultimater.project_base.gui_swing.model.GameModel;
import pacman_ultimater.project_base.gui_swing.model.MainFrameModel;

import javax.sound.sampled.LineUnavailableException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

class GameplayController implements IKeyDownHandler {

    //<editor-fold desc="- VARIABLES Block -">

    private int keyTicks = 5;
    private int FreeGhost, GhostRelease, EatEmTimer;
//    bool killed;
//    bool[] teleported = new bool[EntityCount];
    private Direction.nType newDirection1 = Direction.nType.DIRECTION;
    private Direction.nType newDirection2 = Direction.nType.DIRECTION;
//    Point[] EntitiesPixDeltas = new Point[EntityCount];
//    byte RDPIndex = 0;
    private byte pacSmoothMoveStep;
    private byte ghostSmoothMoveStep;
    private TimersListeners timers;
    private GameLoadController glc;
    private MainFrameModel model;
    private GameModel vars;
    private IGameOverHandler gameOverHandler;
//
//        const
//    int BonusLifeScore = 10000;
//        const
//    int PelletScore = 10;
//        const
//    int PowerPelletScore = 50;
//        const
//    int BaseEatEmTimer = 100;
//        const
//    int ghostEatBaseScore = 200;
//        const
//    int BaseGhostReleaseTimer = 260;
//        const
//    byte GhostAccLimLevel = 30;
//        const
//    byte ghostMaxSpeed = 3;    // The smaller the number the faster the ghosts.
//        const
//    int ghostBaseSpeed = (GhostAccLimLevel / 2) + ghostMaxSpeed + 1;

    //</editor-fold>

    //<editor-fold desc="- GAMEPLAY Block -">

    GameplayController(MainFrameModel model, GameModel vars, IGameOverHandler gameOverHandler)
            throws LineUnavailableException
    {
        this.model = model;
        this.vars = vars;
        this.gameOverHandler = gameOverHandler;
        glc = new GameLoadController(model, vars);
        timers = new TimersListeners(
                new TimerListener(timer_types.PACMAN), new TimerListener(timer_types.GHOST),
                new TimerListener(timer_types.PACMAN_SMOOTH), new TimerListener(timer_types.GHOST_SMOOTH));
    }

    /**
     *
     * @param restart
     */
    void loadGame(boolean restart)
    {
        glc.playGame(restart, timers);
    }

    /**
     * Function handling key pressing during gameplay.
     * @param keyCode Identifies pressed key.
     */
    public void handleKey(int keyCode) {
        //Two booleans keyPressed[1|2] are used to distinguish which of the players has pressed the key during VS play.
        if (vars.player2) {
            if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_S)
                vars.keyPressed1 = true;
            else
                vars.keyPressed2 = true;
        } else
            vars.keyPressed1 = true;

        if (keyCode == KeyEvent.VK_ESCAPE) {
            endGame();
        }
        else if (vars.player2) {
            switch(keyCode){
                case KeyEvent.VK_LEFT:
                    newDirection2 = Direction.nType.LEFT;
                    break;
                case KeyEvent.VK_RIGHT:
                    newDirection2 = Direction.nType.RIGHT;
                    break;
                case KeyEvent.VK_UP:
                    newDirection2 = Direction.nType.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    newDirection2 = Direction.nType.DOWN;
                    break;
                default:
                    vars.keyPressed1 = false;
                    vars.keyPressed2 = false;
            }
        }
        else {
            if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT)
                newDirection1 = Direction.nType.LEFT;
            else if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP)
                newDirection1 = Direction.nType.UP;
            else if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT)
                newDirection1 = Direction.nType.RIGHT;
            else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN)
                newDirection1 = Direction.nType.DOWN;
            else
                vars.keyPressed1 = false;
        }
    }

    /**
     * Ends game by stopping game loop and enabling menu functionality.
     * Saves new highscore in case of beating the previous one by the player.
     * Generally destroys all of the forms's controls and loads them again with their default settings.
     */
    private void endGame() {
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
                HighScoreClass.tryToSaveScore(vars.player2, vars.score, model.resourcesPath);
            }
            catch (IOException ignore){ /* TODO: Notify user score weren't saved due to exception message */ }
        }
        if(vars.musicPlayer != null) {
            vars.musicPlayer.stop();
            vars.musicPlayer.close();
        }
        vars.size = vars.defSize;
        model.setMainFrameSize(vars.size);
        model.mainPanel.setSize(vars.size);
        model.recenterMainFrame(vars.size);
        model.mainPanel.repaint();
        gameOverHandler.handleGameOverRequest();
    }
//
//    /// <summary>
//    /// Handles the event raised by unexcited pacman's contact with one of the ghosts.
//    /// </summary>
//    private async
//
//    void KillPacman() {
//            const int PauseBeforeDeath = 500;
//            const int ExplodingTime = 600;
//            const int P2ScoreForKill = 1500;
//
//        PacUpdater.Stop();
//        GhostUpdater.Stop();
//        if (Music) {
//            MusicPlayer.SoundLocation = "../sounds/pacman_death.wav";
//            MusicPlayer.Play();
//        }
//        if (Player2)
//            Score2 += P2ScoreForKill;
//        Entities[0].Item3.Image = Image.FromFile("../textures/PacStart.png");
//        Refresh();
//        await Task.Delay(PauseBeforeDeath);
//        Entities[0].Item3.Image = Image.FromFile("../textures/PacExplode.png");
//        Refresh();
//        await Task.Delay(ExplodingTime);
//        Lives--;
//        Controls.Clear();
//
//        if (Lives > 0)
//            PlayGame(true);
//        else
//            EndGame();
//    }
//
//    /// <summary>
//    /// Updates desired score box (highscore/player).
//    /// </summary>
//    /// <param name="score">New Value to be set.</param>
//    /// <param name="box">Box whose value is to be set.</param>
//    private void UpdateHud(int?score, Label box) {
//        box.Text = score.ToString();
//    }
//
//    /// <summary>
//    /// Checks whether the direction the entity is aiming in is free.
//    /// </summary>
//    /// <param name="y">Y-axis delta.</param>
//    /// <param name="x">X-axis delta.</param>
//    /// <param name="entity">The observed entity.</param>
//    /// <returns>Returns boolean value indicating emptiness of the observed tile.</returns>
//    private bool IsDirectionFree(int y, int x, Tuple<int, int, PictureBox, Direction.nType, DefaultAI> entity) {
//        int indexX = entity.Item1 + x;
//        int indexY = entity.Item2 + y;
//
//        if (indexX < 0 || indexX >= FieldSizeInColumns || indexY < 0 || indexY >= FieldSizeInRows
//                || MapFresh[indexY][indexX].tile == Tile.nType.FREE
//                || MapFresh[indexY][indexX].tile == Tile.nType.DOT
//                || MapFresh[indexY][indexX].tile == Tile.nType.POWERDOT)
//            return true;
//        else
//            return false;
//    }
//
//    /// <summary>
//    /// Tests whether the direction entity wants to move in is free and if so,
//    /// sests its direction and sets variable for saving direction to default value.
//    /// </summary>
//    /// <param name="newDirection">Variable with stored target direction (set to default in case of success).</param>
//    /// <param name="entity">The observed entity.</param>
//    private void SetToMove
//    (ref Direction.nType newDirection, ref Tuple<int, int, PictureBox, Direction.nType, DefaultAI>entity) {
//        //
//        Direction dir = new Direction();
//        Tuple<int, int> delta = dir.DirectionToTuple(newDirection);
//        if (IsDirectionFree(delta.Item1, delta.Item2, entity)) {
//            entity = new Tuple<int, int, PictureBox, Direction.nType, DefaultAI>
//                    (entity.Item1, entity.Item2, entity.Item3, newDirection, entity.Item5);
//            newDirection = Direction.nType.DIRECTION;
//        }
//    }
//
//    /// <summary>
//    /// Checks whether the entity can continue in the direction it goes otherwise stops it.
//    /// </summary>
//    /// <param name="entity">The observed Entity.</param>
//    private void CanMove(ref Tuple<int, int, PictureBox, Direction.nType, DefaultAI>entity) {
//        Direction dir = new Direction();
//        Tuple<int, int> delta = dir.DirectionToTuple(entity.Item4);
//        if (!IsDirectionFree(delta.Item1, delta.Item2, entity))
//            entity = new Tuple<int, int, PictureBox, Direction.nType, DefaultAI>
//                    (entity.Item1, entity.Item2, entity.Item3, Direction.nType.DIRECTION, entity.Item5);
//    }
//
//    /// <summary>
//    /// Combines entity's in-tiles movement with procedure handling its physical movement and
//    /// right texture loading.
//    /// </summary>
//    /// <param name="change">Boolean indicating whether the entity's direction has changed.</param>
//    /// <param name="entX">New entity's in-tiles X-axis position.</param>
//    /// <param name="entY">New entity's in-tiles Y-axis position.</param>
//    /// <param name="dX">Physical X-axis change.</param>
//    /// <param name="dY">Physical Y-axis change.</param>
//    /// <param name="entity">The observed entity.</param>
//    private void MoveEntity(bool change, int entX, int entY, int dX, int dY, byte entNum,
//                            ref Tuple<int, int, PictureBox, Direction.nType, DefaultAI>entity) {
//        entity = new Tuple<int, int, PictureBox, Direction.nType, DefaultAI>
//                (entX, entY, entity.Item3, entity.Item4, entity.Item5);
//
//        if (entity.Item5.State != DefaultAI.nType.PLAYER1 && MapFresh[entity.Item2][entity.Item1].tile != Tile.nType.FREE) {
//            redrawPellets[RDPIndex] = new Point(entity.Item2, entity.Item1);
//            ++RDPIndex;
//            RDPIndex %= RDPSize;
//        }
//
//        EntitiesPixDeltas[entNum] = new Point(dX, dY);
//    }
//
//    /// <summary>
//    /// Moves entity in its saved direction and tells called function true or false in order to stop it from overwriting entity's image
//    /// in case of teleporting (is reverse move from the program's point of view).
//    /// </summary>
//    /// <param name="entity">The observed entity.</param>
//    private void MoveIt(ref Tuple<int, int, PictureBox, Direction.nType, DefaultAI>entity, byte entNum) {
//        switch (entity.Item4) {
//            case Direction.nType.LEFT:
//                if (entity.Item1 > 0) {
//                    teleported[entNum] = false;
//                    MoveEntity(true, entity.Item1 - 1, entity.Item2, -TileSizeInPxs, 0, entNum, ref entity);
//                } else {
//                    teleported[entNum] = true;
//                    MoveEntity(false, FieldSizeInColumns - 1, entity.Item2, (FieldSizeInColumns - 1) * TileSizeInPxs, 0, entNum, ref entity);
//                }
//                break;
//            case Direction.nType.RIGHT:
//                if (entity.Item1 < FieldSizeInColumns - 1) {
//                    teleported[entNum] = false;
//                    MoveEntity(true, entity.Item1 + 1, entity.Item2, TileSizeInPxs, 0, entNum, ref entity);
//                } else {
//                    teleported[entNum] = true;
//                    MoveEntity(false, 0, entity.Item2, -(FieldSizeInColumns - 1) * TileSizeInPxs, 0, entNum, ref entity);
//                }
//                break;
//            case Direction.nType.UP:
//                if (entity.Item2 > 0) {
//                    teleported[entNum] = false;
//                    MoveEntity(true, entity.Item1, entity.Item2 - 1, 0, -TileSizeInPxs, entNum, ref entity);
//                } else {
//                    teleported[entNum] = true;
//                    MoveEntity(false, entity.Item1, FieldSizeInRows - 1, 0, (FieldSizeInRows - 1) * TileSizeInPxs, entNum, ref entity);
//                }
//                break;
//            case Direction.nType.DOWN:
//                if (entity.Item2 < FieldSizeInRows - 1) {
//                    teleported[entNum] = false;
//                    MoveEntity(true, entity.Item1, entity.Item2 + 1, 0, TileSizeInPxs, entNum, ref entity);
//                } else {
//                    teleported[entNum] = true;
//                    MoveEntity(false, entity.Item1, 0, 0, -(FieldSizeInRows - 1) * TileSizeInPxs, entNum, ref entity);
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    /// <summary>
//    /// Checks whether the position is a crossroad.
//    /// </summary>
//    /// <param name="x">X-axis postion.</param>
//    /// <param name="y">Y-axis position.</param>
//    /// <returns>Boolean indicating crossroad.</returns>
//    private bool IsAtCrossroad(int x, int y) {
//        int turns = 0;
//        for (int j = 0; j < 2; j++)
//            for (int i = -1; i < 2; i += 2) {
//                int indexY = y + (j == 0 ? i : 0);
//                int indexX = x + (j == 1 ? i : 0);
//                if (indexY < 0 || indexY >= FieldSizeInRows || indexX < 0 || indexX >= FieldSizeInColumns)
//                    turns = 1;
//                else if (Map.Item1[indexY][indexX].tile == Tile.nType.FREE ||
//                        Map.Item1[indexY][indexX].tile == Tile.nType.DOT ||
//                        Map.Item1[indexY][indexX].tile == Tile.nType.POWERDOT)
//                    turns++;
//            }
//        if (turns >= 3)
//            return true;
//        else return false;
//    }
//
//    /// <summary>
//    /// Loads right image depending on the game situation and direction.
//    /// </summary>
//    /// <param name="change">Boolean indicating whether the entity's direction has changed.</param>
//    /// <param name="dx">X-axis change.</param>
//    /// <param name="dy">Y-axis change.</param>
//    /// <param name="entity">The observed entity.</param>
//    private void UpdatePicture
//    (ref Tuple<int, int, PictureBox, Direction.nType, DefaultAI>entity) {
//        // Normal situation.
//        // Last Line of if statement ensures ghost flashing at the end of pacman excited mode
//        if (EatEmTimer <= 0 || entity.Item5.State == DefaultAI.nType.PLAYER1
//                || (entity.Item5.State != DefaultAI.nType.EATEN && (Ticks % 3 == 0) && EatEmTimer < ghostFlashingStart))
//            entity.Item3.Image = Image.FromFile("../Textures/" + entity.Item3.Name + entity.Item4.ToString() + ".png");
//            // Entity has been eaten.
//        else if (entity.Item5.State == DefaultAI.nType.EATEN)
//            entity.Item3.Image = Image.FromFile("../Textures/Eyes" + entity.Item4.ToString() + ".png");
//            // Pacman is in the excited mode - ghosts are vulnerable.
//        else
//            entity.Item3.Image = Image.FromFile("../textures/CanBeEaten.png");
//    }
//
//    /// <summary>
//    /// Places picture to the place it should be according to it's tile indexes.
//    /// Finishes one cycle of smooth move and enables start of another cycle.
//    /// </summary>
//    /// <param name="entity">Entity whose picture is to be corrected.</param>
//    private void CorrectPicture(ref Tuple<int, int, PictureBox, Direction.nType, DefaultAI>entity) {
//        entity.Item3.Location = new Point(entity.Item1 * TileSizeInPxs - 6, entity.Item2 * TileSizeInPxs + 42);
//        if (entity.Item4 != Direction.nType.DIRECTION)
//            UpdatePicture(ref entity);
//    }
//
//    /// <summary>
//    /// Function that moves all of the entities and checks whether the pacman and a ghost have met.
//    /// </summary>
//    private void UpdateMove(bool isPacman) {
//        // Direction of entities controlled by players are updated via newDirection variables.
//        // Direction of UI entities is set through AI algorithms.
//        if (isPacman) {
//            if (NewDirection1 != Direction.nType.DIRECTION)
//                SetToMove(ref NewDirection1, ref Entities[0]);
//
//            // Places picture to the place it should be according to it's tile indexes.
//            // Finishes one cycle of smooth move and enables start of another cycle.
//            CorrectPicture(ref Entities[0]);
//
//            CanMove(ref Entities[0]);
//            MoveIt(ref Entities[0], 0);
//        } else {
//            if (NewDirection2 != Direction.nType.DIRECTION)
//                SetToMove(ref NewDirection2, ref Entities[1]);
//
//            for (byte i = 1; i <= FreeGhost; i++) {
//                // Places picture to the place it should be according to it's tile indexes.
//                // Finishes one cycle of smooth move and enables start of another cycle.
//                CorrectPicture(ref Entities[i]);
//
//                // if entity is AI, creates new instance with direction selected by AI algorithm.
//                if (!Player2 || i > 1 && (Entities[i].Item4 == Direction.nType.DIRECTION || IsAtCrossroad(Entities[i].Item1, Entities[i].Item2)))
//                    Entities[i] = new Tuple<int, int, PictureBox, Direction.nType, DefaultAI>
//                            (Entities[i].Item1,
//                                    Entities[i].Item2,
//                                    Entities[i].Item3,
//                                    Entities[i].Item5.NextStep(new Tuple<int, int>(Entities[i].Item1, Entities[i].Item2),
//                                            Entities[i].Item5.State == DefaultAI.nType.EATEN ? TopGhostInTiles
//                                                    : new Tuple<int, int>(Entities[0].Item1, Entities[0].Item2),
//                                            Entities[i].Item4, Map.Item1),
//                                    Entities[i].Item5);
//
//                CanMove(ref Entities[i]);
//                MoveIt(ref Entities[i], i);
//            }
//        }
//    }
//
//    /// <summary>
//    /// Players sound specified as parameter with current soundplayer and moves to another one.
//    /// </summary>
//    /// <param name="file">Sounds name.</param>
//    private void PlayWithSoundPlayer(string file) {
//        SoundPlayers[SoundTick].URL = SoundPath + file;
//        SoundPlayers[SoundTick].controls.play();
//        SoundTick = (SoundTick + 1) % SoundPlayersCount;
//    }
//
//    /// <summary>
//    /// Provides timer for pacman's excitement and changes all of the ghost back to normal at the end.
//    /// </summary>
//    private void UpdateEatEmTimer() {
//        if (EatEmTimer > 1)
//            EatEmTimer--;
//        else if (EatEmTimer == 1) {
//            if (Music) {
//                MusicPlayer.SoundLocation = "../sounds/pacman_siren.wav";
//                MusicPlayer.PlayLooping();
//            }
//            if (GhostsEaten != 0) {
//                for (int i = 1; i < 5; i++)
//                    Entities[i].Item5.State = (Player2 && i == 1 ? DefaultAI.nType.PLAYER2 : DefaultAIs[i - 1].State);
//                GhostsEaten = 0;
//            }
//            GhostUpdater.Interval = Player2 ? (PacTimer + 40 - (Level > 13 ? 65 : Level * 5)) : PacUpdater.Interval + 10;
//            GhostSmoothTimer.Interval = GhostUpdater.Interval / ((TileSizeInPxs / 2) + 1);
//            EatEmTimer = 0;
//        }
//    }
//
//    /// <summary>
//    /// Checks whether pacman is not on a tile with pellet.
//    /// if so, plays the sound and increases number of collected pellets and score.
//    /// Also enables pacman's excitement via EatEmTimer.
//    /// </summary>
//    private void UpdateEatPellet() {
//        if (MapFresh[Entities[0].Item2][Entities[0].Item1].tile == Tile.nType.DOT ||
//                MapFresh[Entities[0].Item2][Entities[0].Item1].tile == Tile.nType.POWERDOT) {
//            if (Sound) {
//                PlayWithSoundPlayer("pacman_chomp.wav");
//            }
//            CollectedDots++;
//            if (MapFresh[Entities[0].Item2][Entities[0].Item1].tile == Tile.nType.DOT)
//                Score += PelletScore;
//            else {
//                Score += PowerPelletScore;
//                if (Music) {
//                    MusicPlayer.SoundLocation = "../sounds/pacman_powersiren.wav";
//                    MusicPlayer.PlayLooping();
//                }
//                //Pacman's excitement lasts shorter each level
//                EatEmTimer = Player2 ? (3 * BaseEatEmTimer) / 4 : BaseEatEmTimer - Level;
//                GhostsEaten = 0;
//                GhostUpdater.Interval = (PacTimer + 40 - (Level > 13 ? 65 : Level * 5)) * 2;
//                GhostSmoothTimer.Interval = GhostUpdater.Interval / ((TileSizeInPxs / 2) + 1);
//                //Return all of the ghost to normal state to be able to be eaten again later
//                for (int i = 1; i < 5; i++)
//                    Entities[i].Item5.State = DefaultAI.nType.CANBEEATEN;
//            }
//
//            //Deletes pellet from the tile
//            MapFresh[Entities[0].Item2][Entities[0].Item1].tile = Tile.nType.FREE;
//            MapFresh[Entities[0].Item2][Entities[0].Item1].FreeTile(bg.Graphics, new Point(Entities[0].Item1 * TileSizeInPxs,
//                    (Entities[0].Item2 + 3) * TileSizeInPxs), this.BackColor);
//
//            if (Score > HighScore) {
//                HighScore = Score;
//                UpdateHud(HighScore, HighScoreBox);
//            }
//            UpdateHud(Score, ScoreBox);
//        }
//
//        RedrawPellets();
//        Blink();
//    }
//
//    /// <summary>
//    /// Places ghost specified by number out of ghost house and sets its start direction.
//    /// Also resets timer for ghost releasing.
//    /// </summary>
//    /// <param name="ghostNum"></param>
//    private void SetGhostFree(int ghostNum) {
//        Entities[ghostNum] = new Tuple<int, int, PictureBox, Direction.nType, DefaultAI>
//                (TopGhostInTiles.Item1, TopGhostInTiles.Item2, Entities[ghostNum].Item3,
//                        Direction.nType.LEFT, Entities[ghostNum].Item5);
//        Entities[ghostNum].Item3.Location = new Point(Entities[ghostNum].Item1 * TileSizeInPxs - 7, Entities[ghostNum].Item2 * TileSizeInPxs + 42);
//        GhostRelease = Player2 ? (BaseGhostReleaseTimer / 2) / 3 : (BaseGhostReleaseTimer - Level) / 3;
//        FreeGhost++;
//    }
//
//    /// <summary>
//    /// Checks if distance in tiles between pacman and each entity is bigger than 1.
//    /// </summary>
//    private void CheckCollision() {
//        for (int i = 1; i < EntityCount; i++)
//            if ((Math.Abs(Entities[i].Item1 - Entities[0].Item1) + Math.Abs(Entities[i].Item2 - Entities[0].Item2)) <= 1) {
//                if (EatEmTimer <= 0) {
//                    KillPacman();
//                    killed = true;
//                    return;
//                }
//                // In case of pacman's excitemnet and if the ghost is not already eaten
//                // changes the ghost's state to eaten and increases player's score.
//                else if (Entities[i].Item5.State != DefaultAI.nType.EATEN) {
//                    if (Sound)
//                        PlayWithSoundPlayer("pacman_eatghost.wav");
//                    if (Music) {
//                        MusicPlayer.SoundLocation = "../sounds/pacman_eatensiren.wav";
//                        MusicPlayer.PlayLooping();
//                    }
//                    GhostsEaten++;
//                    Score += ghostEatBaseScore * GhostsEaten;
//                    UpdateHud(Score, ScoreBox);
//                    if (GhostsEaten == 4 && Lives < MaxLives - 1) {
//                        PacLives[Lives - 1].Visible = true;
//                        ++Lives;
//                        if (Sound)
//                            PlayWithSoundPlayer("pacman_extrapac.wav");
//                    }
//                    Entities[i].Item5.State = DefaultAI.nType.EATEN;
//                }
//            }
//    }
//
//    /// <summary>
//    /// Body of the update mechanism. Selectively updates entities and map.
//    /// </summary>
//    private void UpdateGame(bool isPacman) {
//        UpdateMove(isPacman);
//        CheckCollision();
//
//        if (killed) {
//            killed = false;
//            return;
//        }
//
//        if (isPacman) {
//            UpdateEatEmTimer();
//            UpdateEatPellet();
//
//            // Gives player one extra life at reaching score of BonusLifeScore.
//            if (Score >= BonusLifeScore && !extraLifeGiven) {
//                extraLifeGiven = true;
//                if (Sound)
//                    PlayWithSoundPlayer("pacman_extrapac.wav");
//                PacLives[Lives - 1].Visible = true;
//                ++Lives;
//            }
//            bg.Render(g);
//        } else {
//            Ticks = ++Ticks % 6;
//            //Handles successive ghost releasing
//            if (GhostRelease <= 0 && FreeGhost < 4)
//                SetGhostFree(FreeGhost + 1);
//            if (GhostRelease > 0 && FreeGhost < 4)
//                GhostRelease--;
//        }
//    }
//
//    /// <summary>
//    /// Handles periodical blinking of 1up, 2up and Power Pellets.
//    /// </summary>
//    private void Blink() {
//        if (Ticks % 3 == 0) {
//            this.up1.Visible = false;
//            if (Player2)
//                this.up2.Visible = false;
//        } else {
//            this.up1.Visible = true;
//            if (Player2)
//                this.up2.Visible = true;
//        }
//
//        for (int i = 0; i < Map.Item5.Count; ++i)
//            if (MapFresh[Map.Item5[i].X][Map.Item5[i].Y].tile == Tile.nType.POWERDOT) {
//                if (Ticks % 2 == 0)
//                    MapFresh[Map.Item5[i].X][Map.Item5[i].Y].FreeTile(bg.Graphics, new Point(Map.Item5[i].Y * TileSizeInPxs,
//                            (Map.Item5[i].X + 3) * TileSizeInPxs), this.BackColor);
//                else
//                    MapFresh[Map.Item5[i].X][Map.Item5[i].Y].DrawTile(bg.Graphics, new Point(Map.Item5[i].Y * TileSizeInPxs,
//                            (Map.Item5[i].X + 3) * TileSizeInPxs), this.BackColor);
//            }
//    }
//
//    /// <summary>
//    /// Handles necessary redrawing of pellets cover by ghosts.
//    /// </summary>
//    private void RedrawPellets() {
//        for (int i = 0; i < RDPSize; i++) {
//            MapFresh[redrawPellets[i].X][redrawPellets[i].Y].DrawTile(
//                    bg.Graphics, new Point(redrawPellets[i].Y * TileSizeInPxs, (redrawPellets[i].X + 3) * TileSizeInPxs), this.BackColor);
//        }
//    }
//
//    /// <summary>
//    /// Checks whether the direction player intends to move in is free and nulls assosiated timer
//    /// in such case, continues in countdown otherwise.
//    /// </summary>
//    /// <param name="direction">Direction player intends to move in.</param>
//    /// <param name="keyCountdown">Number of tries left.</param>
//    private void KeyCountAndDir(ref Direction.nType direction, ref int keyCountdown) {
//        if (direction == Direction.nType.DIRECTION && keyCountdown != 0)
//            keyCountdown = 0;
//        else if (direction != Direction.nType.DIRECTION && keyCountdown > 1)
//            keyCountdown--;
//        else if (direction != Direction.nType.DIRECTION && keyCountdown == 1) {
//            direction = Direction.nType.DIRECTION;
//            keyCountdown = 0;
//        }
//    }

    /// <summary>
    /// Topmost layer of instructions executed each frame.
    /// </summary>
    private void gameLoop(boolean isPacman) {
        // In case that one of the players have pushed a valid key, countdown, which represents
        // the number tiles remaining until the information about the pushed button is lost, is started.
        if (vars.keyPressed1) {
            vars.keyCountdown1 = keyTicks;
            vars.keyPressed1 = false;
        }
        if (vars.keyPressed2) {
            vars.keyCountdown2 = keyTicks;
            vars.keyPressed2 = false;
        }

//        // Calls main Update function.
//        UpdateGame(isPacman);
//
//        // Function for key countdown.
//        KeyCountAndDir(ref NewDirection1, ref keyCountdown1);
//        if (mfc.player2)
//            KeyCountAndDir(ref NewDirection2, ref keyCountdown2);

        // Checks if the player has already collected all the pellets.
        // In such case in relation to level and game mode, plays another level or ends the game.
        if (vars.collectedDots >= vars.map.item2)
            endLevel();

        if (isPacman) {
            model.pacSmoothTimer.start();
            pacSmoothMoveStep = 0;
        } else {
            model.ghostSmoothTimer.start();
            ghostSmoothMoveStep = 0;
        }

    }

    private void endLevel() {
        model.pacUpdater.stop();
        model.ghostUpdater.stop();
        vars.musicPlayer.stop();
//        for (int i = 0; i < 8; i++) {
//            if (i % 2 == 0)
//                RenderMap(MapFresh, Color.white);
//            else
//                RenderMap(MapFresh, MapColor);
//
//            bg.Render(g);
//            await Task.Delay(500);
//        }
//
//        Controls.Clear();
//        mfc.level++;
//        if (mfc.level < MainFrameController.MAXLEVEL && !mfc.player2)
//            playGame(false);
//        else
//            endGame();
    }

    enum timer_types { PACMAN, GHOST, PACMAN_SMOOTH, GHOST_SMOOTH }

    /**
     * Handles events raised by timers and distributes them to appropriate functions.
     */
    private class TimerListener implements ActionListener{
        timer_types timer_type;

        private TimerListener(timer_types type){
            timer_type = type;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
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
    private void pacUpdater_Tick() {
        model.pacSmoothTimer.stop();
        gameLoop(true);
    }

    /**
     * Creates illusion of game loop.
     * Handles event raised by ghosts' timer's periodical ticks.
     */
    private void ghostUpdater_Tick() {
        model.ghostSmoothTimer.stop();
        gameLoop(false);
    }
//
//    /// <summary>
//    /// Handles animating of pacman's translation between old and the new tile.
//    /// </summary>
    private void pacSmoothTimer_Tick() {
//        ++PacSmoothMoveStep;
//        if (Entities[0].Item4 != Direction.nType.DIRECTION && !teleported[0]) {
//            Point d = GetDeltas(0);
//
//            // Last part of smooth move is done at the beginning of each update cycle.
//            if (EntitiesPixDeltas[0].X <= 1 && EntitiesPixDeltas[0].Y <= 1 && EntitiesPixDeltas[0].X >= -1 && EntitiesPixDeltas[0].Y >= -1)
//                PacSmoothTimer.Stop();
//            else
//                Entities[0].Item3.Location = new Point((Entities[0].Item3.Location.X + d.X), (Entities[0].Item3.Location.Y + d.Y));
//
//            if (PacSmoothMoveStep % 2 == 0) {
//                if (PacSmoothMoveStep % 4 == 0)
//                    Entities[0].Item3.Image = Image.FromFile("../textures/PacStart.png");
//                else
//                    Entities[0].Item3.Image = Image.FromFile("../Textures/" + Entities[0].Item3.Name + Entities[0].Item4.ToString() + ".png");
//            }
//        }
    }
//
//    /// <summary>
//    /// Handles animating of ghosts' translation between old and the new tile.
//    /// </summary>
    private void ghostSmoothTimer_Tick() {
//        ++GhostSmoothMoveStep;
//        for (byte i = 1; i <= FreeGhost; i++)
//            if (Entities[i].Item4 != Direction.nType.DIRECTION && !teleported[i]) {
//                Point d = GetDeltas(i);
//
//                // Last part of smooth move is done at the beginning of each update cycle.
//                if (EntitiesPixDeltas[i].X <= 1 && EntitiesPixDeltas[i].Y <= 1 && EntitiesPixDeltas[i].X >= -1 && EntitiesPixDeltas[i].Y >= -1)
//                    GhostSmoothTimer.Stop();
//                else
//                    Entities[i].Item3.Location = new Point((Entities[i].Item3.Location.X + d.X), (Entities[i].Item3.Location.Y + d.Y));
//            }
    }
//
//    /// <summary>
//    /// Gets translation deltas for entity specified by input index.
//    /// </summary>
//    /// <param name="index">Input index identifying entity.</param>
//    /// <returns>Returns point of deltas in X and Y axes.</returns>
//    private Point GetDeltas(byte index) {
//        int dX = 0, dY = 0;
//        if (EntitiesPixDeltas[index].X >= 2) {
//            dX = 2;
//            EntitiesPixDeltas[index].X -= 2;
//        } else if (EntitiesPixDeltas[index].X <= -2) {
//            dX = -2;
//            EntitiesPixDeltas[index].X += 2;
//        } else if (EntitiesPixDeltas[index].Y >= 2) {
//            dY = 2;
//            EntitiesPixDeltas[index].Y -= 2;
//        } else if (EntitiesPixDeltas[index].Y <= -2) {
//            dY = -2;
//            EntitiesPixDeltas[index].Y += 2;
//        }
//
//        return new Point(dX, dY);
//    }
//
//    //</editor-fold>
}
