package pacman_ultimater.project_base.core;

import java.io.*;
import java.util.Random;

/**
 * Contains two simple functions one for loading and other for saving highscore.
 */
public class HighScoreClass
{
    private static int ImportantLine = 10;
    private static int LinesInFile = 15;

    /**
     * Function for HighScore loading.
     * @param resourcesPath Path to the resources files.
     * @return int
     */
    public static int loadHighScore(String resourcesPath) throws IOException
    {
        // The actual highscore is saved at the 10th line of the config file.
        // It is necesary to just convert it from binary to decimaly number.

        int highScore = 0;
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
        highScore = Integer.parseInt(scoreLine, 2);
        br.close();
        return highScore;
    }

    /**
     * Function for HighScore Saving.
     * @param newHighScore New HighScore to be saved to config file.
     * @param resourcesPath Path to the resources files.
     */
    public static void saveHighScore(int newHighScore, String resourcesPath) throws IOException
    {
        // Function generates 9 lines of random binary numbers of aproximately same length as highscore.
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
