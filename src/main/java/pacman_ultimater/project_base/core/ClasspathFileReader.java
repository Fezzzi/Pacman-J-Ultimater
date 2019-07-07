package pacman_ultimater.project_base.core;

import java.awt.*;
import java.io.*;

/**
 * Class handling resource files from classpath reading.
 * getResource requires '/' - separated path name.
 */
public class ClasspathFileReader
{
    //<editor-fold desc="- NAME CONSTANTS Block -">

    private static final String PACMAN_BEGINNING = "sounds/pacman_beginning.wav";
    private static final String PACMAN_CHOMP = "sounds/pacman_chomp.wav";
    private static final String PACMAN_DEATH = "sounds/pacman_death.wav";
    private static final String PACMAN_EATENSIREN = "sounds/pacman_eatensiren.wav";
    private static final String PACMAN_EATGHOST = "sounds/pacman_eatghost.wav";
    private static final String PACMAN_EXTRAPAC = "sounds/pacman_extrapac.wav";
    private static final String PACMAN_POWERSIREN = "sounds/pacman_powersiren.wav";
    private static final String PACMAN_SIREN = "sounds/pacman_siren.wav";
    private static final String PACMAN_INTERMISSION = "sounds/pacman_intermission.wav";
    private static final String CONFIG = "config.bin";
    static final String USER_CONFIG = System.getProperty("user.home") + File.separator +"PacManJUltimater"+ File.separator +"config.bin";
    private static final String ORIGINAL_MAP = "OriginalMap.txt";
    private static final String ICON = "PacManJUltimater.png";
    private static final String RAVIE = "Ravie.ttf";

    //</editor-fold>

    //<editor-fold desc="- SOUND GETTERS Block -">

    public static InputStream getPACMAN_BEGINNING() {
        return new BufferedInputStream(readFileFromClasspath(PACMAN_BEGINNING));
    }
    public static InputStream getPACMAN_CHOMP() {
        return new BufferedInputStream(readFileFromClasspath(PACMAN_CHOMP));
    }
    public static InputStream getPACMAN_DEATH() {
        return new BufferedInputStream(readFileFromClasspath(PACMAN_DEATH));
    }
    public static InputStream getPACMAN_EATENSIREN() {
        return new BufferedInputStream(readFileFromClasspath(PACMAN_EATENSIREN));
    }
    public static InputStream getPACMAN_EATGHOST() {
        return new BufferedInputStream(readFileFromClasspath(PACMAN_EATGHOST));
    }
    public static InputStream getPACMAN_EXTRAPAC() {
        return new BufferedInputStream(readFileFromClasspath(PACMAN_EXTRAPAC));
    }
    public static InputStream getPACMAN_POWERSIREN() {
        return new BufferedInputStream(readFileFromClasspath(PACMAN_POWERSIREN));
    }
    public static InputStream getPACMAN_SIREN() {
        return new BufferedInputStream(readFileFromClasspath(PACMAN_SIREN));
    }
    public static InputStream getPACMAN_INTERMISSION() {
        return new BufferedInputStream(readFileFromClasspath(PACMAN_INTERMISSION));
    }

    //</editor-fold>

    public static InputStream getORIGINAL_MAP() {
        return readFileFromClasspath(ORIGINAL_MAP);
    }
    public static InputStream getICON() {
        return readFileFromClasspath(ICON);
    }

    /**
     * Loads highscore from config. Primary searches for user's config. If not found uses deafault one provided.
     *
     * @return config file in form of InputStream.
     */
    static InputStream getCONFIG() {
        File config = new File(USER_CONFIG);
        if (!config.exists())
            return readFileFromClasspath(CONFIG);

        try {
            return new FileInputStream(config);
        }
        catch (FileNotFoundException e){
            return null;
        }
    }

    private static Font font;

    /**
     * Loads font that is to be used for all headlines.
     *
     * @return Loaded font.
     */
    public static Font getFONT(){
        if (font == null) {
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, readFileFromClasspath(RAVIE));
            }
            catch (IOException | FontFormatException e) {
                font = new Font("Helvetica", Font.BOLD, 48);
            }
        }

        return font;
    }

    /**
     * Reads file specified by given String form classpath.
     *
     * @param file String holding file url relative to resources folder.
     * @return given file as InputStream.
     */
    private static InputStream readFileFromClasspath(String file) {
        return ClasspathFileReader.class.getResourceAsStream('/' + file);
    }
}