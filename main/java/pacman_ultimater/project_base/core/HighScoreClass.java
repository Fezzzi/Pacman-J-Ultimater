package pacman_ultimater.project_base.core;

import java.io.*;
import java.util.Random;

public class HighScoreClass
{
    private static int ImportantLine = 10;
    private static int LinesInFile = 15;

    /**
     * Function for HighScore loading.
     *
     * @param resourcesPath Path to the resources files.
     * @return int
     */
    public static int loadHighScore(String resourcesPath)
            throws IOException
    {
        // The actual highscore is saved at the 10th line of the config file.
        // It is necessary to just convert it from binary to decimal number.

        String scoreLine = "";
        BufferedReader br = new BufferedReader(new FileReader(resourcesPath + "\\config.bin"));
        for (int i = 0; i <= ImportantLine; i++)
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
     * Attempts to save player's score to file.
     *
     * @param player2 Score is not saved after vs mode.
     * @param score Player's score.
     * @param resourcesPath Path to the resources folder.
     * @throws IOException To be handled by calling procedure.
     */
    public static void tryToSaveScore(boolean player2, int score, String resourcesPath)
            throws IOException
    {
        if(!player2 && score > 0)
            HighScoreClass.saveHighScore(score, resourcesPath);
    }

    /**
     * Function for HighScore Saving.
     *
     * @param newHighScore New HighScore to be saved to config file.
     * @param resourcesPath Path to the resources files.
     */
    private static void saveHighScore(int newHighScore, String resourcesPath)
            throws IOException
    {
        // Function generates 9 lines of random binary numbers of approximately same length as highscore.
        // After that the actual highscore is converted to binary number and saved at 10th line.
        // Another 4 lines of random binary data are generated and saved at the end of the file.

        BufferedWriter bw = new BufferedWriter(new FileWriter(resourcesPath + "\\config.bin"));
        Random rndm = new Random();
        String binaryScore = Integer.toBinaryString(newHighScore);
        for (int i = 0; i < LinesInFile; i++)
        {
            if (i == ImportantLine)
                bw.write(binaryScore);
            else
                bw.write(Integer.toBinaryString(rndm.nextInt(64) + newHighScore - 32));
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }
}
