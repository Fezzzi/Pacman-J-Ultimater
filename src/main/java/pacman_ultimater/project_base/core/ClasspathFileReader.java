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
    private static final String PACMAN_EATFRUIT = "sounds/pacman_eatfruit.wav";
    private static final String PACMAN_EXTRAPAC = "sounds/pacman_extrapac.wav";
    private static final String PACMAN_POWERSIREN = "sounds/pacman_powersiren.wav";
    private static final String PACMAN_SIREN = "sounds/pacman_siren.wav";
    private static final String PACMAN_INTERMISSION = "sounds/pacman_intermission.wav";
    private static final String CANBEEATEN = "textures/CanBeEaten.png";
    private static final String ENTITY1RIGHTBIG = "textures/Entity1RightBig.png";
    private static final String LIFE = "textures/Life.png";
    private static final String PACEXPLODE = "textures/PacExplode.png";
    private static final String PACSTART = "textures/PacStart.png";
    private static final String PACSTARTBIG = "textures/PacStartBig.png";
    private static final String ENTITY1DOWN = "textures/Entity1Down.png";
    private static final String ENTITY1LEFT = "textures/Entity1Left.png";
    private static final String ENTITY1RIGHT = "textures/Entity1Right.png";
    private static final String ENTITY1UP = "textures/Entity1Up.png";
    private static final String ENTITY2DOWN = "textures/Entity2Down.png";
    private static final String ENTITY2LEFT = "textures/Entity2Left.png";
    private static final String ENTITY2RIGHT = "textures/Entity2Right.png";
    private static final String ENTITY2UP = "textures/Entity2Up.png";
    private static final String ENTITY3DOWN = "textures/Entity3Down.png";
    private static final String ENTITY3LEFT = "textures/Entity3Left.png";
    private static final String ENTITY3RIGHT = "textures/Entity3Right.png";
    private static final String ENTITY3UP = "textures/Entity3Up.png";
    private static final String ENTITY4DOWN = "textures/Entity4Down.png";
    private static final String ENTITY4LEFT = "textures/Entity4Left.png";
    private static final String ENTITY4RIGHT = "textures/Entity4Right.png";
    private static final String ENTITY4UP = "textures/Entity4Up.png";
    private static final String ENTITY5DOWN = "textures/Entity5Down.png";
    private static final String ENTITY5LEFT = "textures/Entity5Left.png";
    private static final String ENTITY5RIGHT = "textures/Entity5Right.png";
    private static final String ENTITY5UP = "textures/Entity5Up.png";
    private static final String EYESDOWN = "textures/EyesDown.png";
    private static final String EYESLEFT = "textures/EyesLeft.png";
    private static final String EYESRIGHT = "textures/EyesRight.png";
    private static final String EYESUP = "textures/EyesUp.png";
    private static final String CONFIG = "config.bin";
    public static final String USER_CONFIG = System.getProperty("user.home") + File.separator +"PacManJUltimater"+ File.separator +"config.bin";
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
    public static InputStream getPACMAN_EATFRUIT() {
        return new BufferedInputStream(readFileFromClasspath(PACMAN_EATFRUIT));
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

    //<editor-fold desc="- TEXTURE GETTERS Block -">

    private static InputStream
            canbeeaten, entity1rightbig, life, pacexplode, pacstart, pacstartbig, entity1down, entity1left,
            entity1right, entity1up, entity2down, entity2left, entity2right, entity2up, entity3down, entity3left,
            entity3right, entity3up, entity4down, entity4left, entity4right, entity4up, entity5down, entity5left,
            entity5right, entity5up, eyesdown, eyesleft, eyesright, eyesup;

    /**
     * Performs conversion from entity name (Entity[1-5]) and its direction (LEFT,RIGHT,...) to corresponding File's InputStream.
     *
     * @param entityName      Entity's name
     * @param entityDirection Entity's direciton.
     * @return InputStream corresponding to specified texture.
     */
    public static InputStream getEntityFile(String entityName, String entityDirection) {
        if (entityName.equals("Eyes")) {
            switch (entityDirection) {
                case "LEFT":
                    return getEYESLEFT();
                case "RIGHT":
                    return getEYESRIGHT();
                case "UP":
                    return getEYESUP();
                case "DOWN":
                    return getEYESDOWN();
            }
        } else {
            switch (entityDirection) {
                case "LEFT":
                    switch (entityName) {
                        case "Entity1":
                            return getENTITY1LEFT();
                        case "Entity2":
                            return getENTITY2LEFT();
                        case "Entity3":
                            return getENTITY3LEFT();
                        case "Entity4":
                            return getENTITY4LEFT();
                        case "Entity5":
                            return getENTITY5LEFT();
                    }
                case "RIGHT":
                    switch (entityName) {
                        case "Entity1":
                            return getENTITY1RIGHT();
                        case "Entity2":
                            return getENTITY2RIGHT();
                        case "Entity3":
                            return getENTITY3RIGHT();
                        case "Entity4":
                            return getENTITY4RIGHT();
                        case "Entity5":
                            return getENTITY5RIGHT();
                    }
                case "UP":
                    switch (entityName) {
                        case "Entity1":
                            return getENTITY1UP();
                        case "Entity2":
                            return getENTITY2UP();
                        case "Entity3":
                            return getENTITY3UP();
                        case "Entity4":
                            return getENTITY4UP();
                        case "Entity5":
                            return getENTITY5UP();
                    }
                case "DOWN":
                    switch (entityName) {
                        case "Entity1":
                            return getENTITY1DOWN();
                        case "Entity2":
                            return getENTITY2DOWN();
                        case "Entity3":
                            return getENTITY3DOWN();
                        case "Entity4":
                            return getENTITY4DOWN();
                        case "Entity5":
                            return getENTITY5DOWN();
                    }
            }
        }
        return null;
    }

    public static InputStream getCANBEEATEN() {
		return getVar(canbeeaten, CANBEEATEN);
    }
    public static InputStream getENTITY1RIGHTBIG() {
		return getVar(entity1rightbig, ENTITY1RIGHTBIG);
    }
    public static InputStream getLIFE() {
		return getVar(life, LIFE);
    }
    public static InputStream getPACEXPLODE() {
		return getVar(pacexplode, PACEXPLODE);
    }
    public static InputStream getPACSTART() {
		return getVar(pacstart, PACSTART);
    }
    public static InputStream getPACSTARTBIG() {
		return getVar(pacstartbig, PACSTARTBIG);
    }
    public static InputStream getENTITY1DOWN() {
		return getVar(entity1down, ENTITY1DOWN);
    }
    public static InputStream getENTITY1LEFT() {
		return getVar(entity1left, ENTITY1LEFT);
    }
    public static InputStream getENTITY1RIGHT() {
		return getVar(entity1right, ENTITY1RIGHT);
    }
    public static InputStream getENTITY1UP() {
		return getVar(entity1up, ENTITY1UP);
    }
    public static InputStream getENTITY2DOWN() {
		return getVar(entity2down, ENTITY2DOWN);
    }
    public static InputStream getENTITY2LEFT() {
		return getVar(entity2left, ENTITY2LEFT);
    }
    public static InputStream getENTITY2RIGHT() {
		return getVar(entity2right, ENTITY2RIGHT);
    }
    public static InputStream getENTITY2UP() {
		return getVar(entity2up, ENTITY2UP);
    }
    public static InputStream getENTITY3DOWN() {
		return getVar(entity3down, ENTITY3DOWN);
    }
    public static InputStream getENTITY3LEFT() {
		return getVar(entity3left, ENTITY3LEFT);
    }
    public static InputStream getENTITY3RIGHT() {
		return getVar(entity3right, ENTITY3RIGHT);
    }
    public static InputStream getENTITY3UP() {
		return getVar(entity3up, ENTITY3UP);
    }
    public static InputStream getENTITY4DOWN() {
		return getVar(entity4down, ENTITY4DOWN);
    }
    public static InputStream getENTITY4LEFT() {
		return getVar(entity4left, ENTITY4LEFT);
    }
    public static InputStream getENTITY4RIGHT() {
		return getVar(entity4right, ENTITY4RIGHT);
    }
    public static InputStream getENTITY4UP() {
		return getVar(entity4up, ENTITY4UP);
    }
    public static InputStream getENTITY5DOWN() {
		return getVar(entity5down, ENTITY5DOWN);
    }
    public static InputStream getENTITY5LEFT() {
		return getVar(entity5left, ENTITY5LEFT);
    }
    public static InputStream getENTITY5RIGHT() {
		return getVar(entity5right, ENTITY5RIGHT);
    }
    public static InputStream getENTITY5UP() {
		return getVar(entity5up, ENTITY5UP);
    }
    public static InputStream getEYESDOWN() {
		return getVar(eyesdown, EYESDOWN);
    }
    public static InputStream getEYESLEFT() {
		return getVar(eyesleft, EYESLEFT);
    }
    public static InputStream getEYESRIGHT() {
		return getVar(eyesright, EYESRIGHT);
    }
    public static InputStream getEYESUP() {
        return getVar(eyesup, EYESUP);
    }

    /**
     * Returns input stream either fetched from allready loaded variable, or loaded with given string id.
     *
     * @param streamVar InputStream's variable.
     * @param streamStr InputStream's string id.
     * @return Fetched InputStream corresponding to given variable and string id.
     */
    private static InputStream getVar(InputStream streamVar, String streamStr){
        if (streamVar != null) {
            return safeReset(streamVar);
        }

        streamVar = new BufferedInputStream(readFileFromClasspath(streamStr));
        streamVar.mark(0);
        return streamVar;
    }

    /**
     * Function performing safe reset - return null and unsets given stream on failure.
     * @param stream Stream to be reset.
     * @return reset stream or null.
     */
    private static InputStream safeReset(InputStream stream){
        try{
            stream.reset();
            return stream;
        }
        catch (IOException e){
            stream = null;
            return null;
        }
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
    public static InputStream getCONFIG() {
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