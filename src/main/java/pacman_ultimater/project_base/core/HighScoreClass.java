package pacman_ultimater.project_base.core;

import java.io.*;
import java.util.Random;

/**
 * Class provides high score loading and saving from/to encoded formate in congif file.
 */
public class HighScoreClass
{
    private static final int IPORTANTLINE = 10;
    private static final int LINESINFILE = 15;

    /**
     * Function for HighScore loading.
     *
     * @throws IOException To be handled by caller.
     * @return int
     */
    public static int loadHighScore()
            throws IOException
    {
        // The actual highscore is saved at the 10th line of the config file.
        // It is necessary to just convert it from binary to decimal number.

        String scoreLine = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(ClasspathFileReader.getCONFIG()));
        for (int i = 0; i <= IPORTANTLINE; i++)
        {
            scoreLine = br.readLine();
            if (scoreLine == null) {
                br.close();
                return 0;
            }
        }
        int highScore = Integer.parseInt(scoreLine, 2);
        br.close();
        return highScore;
    }

    /**
     * Attempts to save player's score to file. Score is not saved after multiplayer mode.
     *
     * @param multiplayer boolean
     * @param score int
     * @throws IOException To be handled by calling procedure.
     */
    public static void tryToSaveScore(boolean multiplayer, int score)
            throws IOException
    {
        if(!multiplayer && score > 0)
            HighScoreClass.saveHighScore(score);
    }

    /**
     * Function for HighScore Saving.
     *
     * @param newHighScore New HighScore to be saved to config file.
     * @throws IOException To be handled by caller.
     */
    private static void saveHighScore(int newHighScore)
            throws IOException
    {
        // Function generates 9 lines of random binary numbers of approximately same length as highscore.
        // After that the actual highscore is converted to binary number and saved at 10th line.
        // Another 4 lines of random binary data are generated and saved at the end of the file.

        File user_config = new File(ClasspathFileReader.USER_CONFIG);
        if (!user_config.exists()){
            File user_config_parent = user_config.getParentFile();
            if (!user_config_parent.exists() && !user_config_parent.mkdirs()) {
                throw new IOException();
            }
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(user_config));
        Random rndm = new Random();
        String binaryScore = Integer.toBinaryString(newHighScore);
        for (int i = 0; i < LINESINFILE; i++)
        {
            if (i == IPORTANTLINE)
                bw.write(binaryScore);
            else
                bw.write(Integer.toBinaryString(rndm.nextInt(64) + newHighScore - 32));
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }
}
