package pacman_ultimater.project_base.gui_swing.model;

import pacman_ultimater.project_base.ai.DefaultAI;
import pacman_ultimater.project_base.core.*;
import pacman_ultimater.project_base.custom_utils.IntPair;
import pacman_ultimater.project_base.custom_utils.Quintet;
import pacman_ultimater.project_base.custom_utils.Sextet;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Structure holding together all game variables that may be accessible from all of the code.
 */
public class GameModel {

    public int score, score2, lives, highScore, soundTick, collectedDots, ghostsEaten, ghostRelease,
            level, freeGhosts, ticks, eatEmTimer, fruitLife, scatterMode, chaseMode, modeSwitchCounter;
    public Integer keyCountdown1, keyCountdown2, keyCountdown3, keyCountdown4;
    public float vMult, hMult;
    public Boolean gameOn, editor;
    public boolean player2, player3, player4, sound, music, extraLifeGiven, keyPressed1, keyPressed2, keyPressed3,
            keyPressed4, killed;
    public Clip musicPlayer;
    public Clip[] soundPlayers;
    public Sextet<Tile[][], Integer, IntPair, Color, ArrayList<Point>, Boolean[][]> map;
    public Tile[][] mapFresh;
    public ArrayList<JLabel> addedComponents;
    public LoadMap loadedMap;

    public ArrayList<Quintet<Integer, Integer, JLabel, Direction.directionType, DefaultAI>> entities;
    public IntPair topGhostInTiles;
    public JLabel[] pacLives;
    public final Dimension defSize;
    public final JLabel up1, up2, scoreBox, highScoreBox, score2Box, fruitLabel, fruitHud;
    public JLabel gameMap;
    public ArrayList<Integer> collectedFruits;
    public Color mapColor;
    public Graphics bufferGraphics;

    public GameModel()
    {
        defSize = new Dimension(LoadMap.DEFAULTWIDTH, LoadMap.DEFAULTHEIGHT);
        score = 0;
        score2 = 0;
        lives = 0;
        vMult = 1;
        hMult = 1;
        highScore = -1;
        gameOn = false;
        editor = false;
        player2 = player3 = player4 = false;
        sound = true;
        music = true;

        up1 = new JLabel();
        up2 = new JLabel();
        scoreBox = new JLabel();
        score2Box = new JLabel();
        highScoreBox = new JLabel();
        fruitLabel = new JLabel();
        fruitHud = new JLabel();
    }

    /**
     * Initializes sound players.
     *
     * @throws LineUnavailableException Exception is to be handled by caller;
     * @throws IOException Exception is to be handled by caller;
     * @throws UnsupportedAudioFileException Exception is to be handled by caller;
     */
    public void initSoundPlayers()
        throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        soundPlayers = new Clip[GameConsts.SOUNDPLAYERSCOUNT + 3];

        for (int i = 0; i < GameConsts.SOUNDPLAYERSCOUNT; i++) {
            soundPlayers[i] = AudioSystem.getClip();
            AudioInputStream ChompSound = AudioSystem.getAudioInputStream(ClasspathFileReader.getPACMAN_CHOMP());
            byte[] ChompSoundBuffer = new byte[65536];
            ChompSound.read(ChompSoundBuffer, 0, 65536);

            soundPlayers[i].open(ChompSound.getFormat(), ChompSoundBuffer, 0, 65536);
        }

        soundPlayers[GameConsts.EXTRAPACSOUNDPLAYERID] = AudioSystem.getClip();
        AudioInputStream ExtraPacSound = AudioSystem.getAudioInputStream(ClasspathFileReader.getPACMAN_EXTRAPAC());
        byte[] ExtraPacSoundBuffer = new byte[65536];
        ExtraPacSound.read(ExtraPacSoundBuffer, 0, 65536);
        soundPlayers[GameConsts.EXTRAPACSOUNDPLAYERID].open(ExtraPacSound.getFormat(), ExtraPacSoundBuffer, 0, 65536);

        soundPlayers[GameConsts.EATGHOSTSOUNDPLAYERID] = AudioSystem.getClip();
        AudioInputStream EatGhostSound = AudioSystem.getAudioInputStream(ClasspathFileReader.getPACMAN_EATGHOST());
        byte[] EatGhostsoundBuffer = new byte[65536];
        EatGhostSound.read(EatGhostsoundBuffer, 0, 65536);
        soundPlayers[GameConsts.EATGHOSTSOUNDPLAYERID].open(EatGhostSound.getFormat(), EatGhostsoundBuffer, 0, 65536);
    }

    /**
     * Plays sound associated with sound player specified as parameter.
     *
     * @param playerId ID of sound player that is to be played.
     */
    public void playWithSoundPlayer(int playerId)
    {
        soundPlayers[playerId].setFramePosition(0);
        soundPlayers[playerId].start();
    }

    /**
     * Plays music specified as parameter with current music player with conditional loop effect.
     *
     * @param stream Input stream to be played.
     * @param loop Loop file or play just once?
     * @param startLoopPoint Starting sample of the loop play.
     * @param endLoopPoint Ending sample of the loop pay.
     * @throws LineUnavailableException Exception is to be handled by caller.
     * @throws IOException Exception is to be handled by caller.
     * @throws UnsupportedAudioFileException Exception is to be handled by caller.
     */
    public void playWithMusicPLayer(InputStream stream, boolean loop, int startLoopPoint, int endLoopPoint)
            throws LineUnavailableException, IOException, UnsupportedAudioFileException
    {
        AudioInputStream sound = AudioSystem.getAudioInputStream(stream);
        byte[] buffer1 = new byte[65536];
        sound.read(buffer1, 0, 65536);

        musicPlayer.close();
        musicPlayer.open(sound.getFormat(), buffer1, 0, 65536);
        if(loop){
            musicPlayer.setLoopPoints(startLoopPoint, endLoopPoint);
            musicPlayer.loop(Clip.LOOP_CONTINUOUSLY);
        }
        musicPlayer.start();
    }
}
