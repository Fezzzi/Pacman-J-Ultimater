package pacman_ultimater.project_base.gui_swing.model;

import pacman_ultimater.project_base.core.*;
import pacman_ultimater.project_base.custom_utils.IntPair;
import pacman_ultimater.project_base.custom_utils.Quintet;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameModel {

    public int score, score2, lives, highScore, soundTick, collectedDots, ghostsEaten, ghostRelease,
                level, freeGhosts, ticks, eatEmTimer;
    public Integer keyCountdown1, keyCountdown2;
    public Boolean gameOn;
    public boolean player2, sound, music, extraLifeGiven, keyPressed1, keyPressed2, killed;
    public Clip musicPlayer;
    public Clip[] soundPlayers;
    public Quintet<Tile[][], Integer, IntPair, Color, ArrayList<Point>> map;
    public Tile[][] mapFresh;
    public Point[] redrawPellets;
    public LoadMap loadedMap;

    public ArrayList<Quintet<Integer, Integer, JLabel, Direction.directionType, DefaultAI>> entities;
    public DefaultAI[] defaultAIs;
    public IntPair topGhostInTiles;
    public JLabel[] pacLives;
    public Dimension defSize, size;
    public JLabel up1, up2, gameMap, scoreBox, highScoreBox, score2Box;
    public Color mapColor;
    public Graphics bufferGraphics;

    public GameModel()
    {
        redrawPellets = new Point[LoadMap.RDPSIZE];
        score = 0;
        score2 = 0;
        lives = 0;
        highScore = -1;
        gameOn = false;
        player2 = false;
        sound = true;
        music = true;
    }
}
