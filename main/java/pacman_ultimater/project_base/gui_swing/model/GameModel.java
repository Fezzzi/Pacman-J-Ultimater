package pacman_ultimater.project_base.gui_swing.model;

import pacman_ultimater.project_base.core.*;
import pacman_ultimater.project_base.custom_utils.IntPair;
import pacman_ultimater.project_base.custom_utils.Quintet;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GameModel {

    public int score, score2, lives, highScore, soundTick, collectedDots, ghostsEaten, ghostRelease,
                level, keyCountdown1, keyCountdown2, freeGhosts, ticks, eatEmTimer;
    public Boolean gameOn;
    public boolean player2, sound, music, extraLifeGiven, keyPressed1, keyPressed2, killed;
    public Clip musicPlayer;
    public Clip[] soundPlayers;
    public Quintet<Tile[][], Integer, IntPair, Color, ArrayList<Point>> map;
    public Tile[][] mapFresh;
    public Point[] redrawPellets;
    public LoadMap loadedMap;

    public ArrayList<Quintet<Integer, Integer, JLabel, Direction.nType, DefaultAI>> entities;
    public DefaultAI[] defaultAIs;
    public IntPair topGhostInTiles;
    public JLabel[] pacLives;
    public Dimension defSize, size;
    public JLabel up1, up2, loading, levelLabel, ready, gameMap, scoreBox, highScoreBox, score2Box;
    public Color mapColor;
    public int[] animLabelsIds = new int[5];

    public GameModel(){
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

    /**
     * Attempts to save player's score to file.
     */
    public void tryToSaveScore(String resourcesPath) throws IOException {
        if(!player2 && score > 0)
            HighScoreClass.saveHighScore(score, resourcesPath);
    }
}
