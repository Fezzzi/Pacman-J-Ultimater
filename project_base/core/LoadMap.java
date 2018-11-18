package pacman_ultimater.project_base.core;

import pacman_ultimater.project_base.custom_utils.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Class that handles manipulation with text files containing map.
 */
class LoadMap
{
    private final int MapWidthInTiles = 28;
    private final int MapHeightInTiles = 31;

    private Tile[][] tileMap;
    private Quintet<Tile[][], Integer, IntPair, Color, ArrayList<Point>> Map;

    /**
     * Calls loading function with default symbols.
     * @param path Path to the map file that is to be loaded.
     */
    public LoadMap(String path)
    {
        Map = LdMap(path, new char[] {' ','.','o','x','X'});
    }

    /**
     * Calls loading function with given symbols.
     * @param path Path to the map file that is to be loaded.
     * @param symbols Set of 5 symbols representing tile types in to be loaded file.
     */
    public LoadMap(String path, char[] symbols)
    {
        Map = LdMap(path, symbols);
    }

    /**
     * Handles function calling to ensure map's loading and testing of its playability.
     * @param path Path to the map file that is to be loaded.
     * @param symbols Set of 5 symbols representing tile types in to be loaded file.
     * @return Return array of loaded tiles, translated to game's language (null in case of failure).
     */
    private Quintet<Tile[][], Integer, IntPair, Color, ArrayList<Point>> LdMap(String path, char[] symbols)
    {
        try
        {
            Quartet<char[][], Integer, Color, ArrayList<Point>> map = LoadIt(path, symbols);
            if (map != null)
            {
                // if LoadIt ended successfully preforms test of map's playability.
                Pair<Boolean, IntPair> playable = IsPlayable(map.item1, symbols, map.item2);
                if (playable.item1)
                    return new Quintet<>(tileMap, map.item2, playable.item2, map.item3, map.item4);
            }
        }
        catch (IndexOutOfBoundsException | IOException ignore) {}

        // In case map is playable final Tuple is created and returned.
        // Null is returned to signalise something went wrong otherwise.
        return null;
    }

    /**
     * Function that loads map from text file and creates Tile map if possible. Returns null otherwise.
     * @param path Path to the map file that is to be loaded.
     * @param symbols Set of 5 symbols representing tile types in to be loaded file.
     * @return Returns Tile map if possible, null otherwise.
     */
    private Quartet<char[][], Integer, Color, ArrayList<Point>> LoadIt(String path, char[] symbols)
            throws IOException
    {
        // Map is created bigger so it is possible to automaticaly call transform to tiles with indexes
        // out of range of map without causing IndexOutOfRange Exception.
        int numOfDots = 0;
        char[][] map = new char[MapHeightInTiles + 2][];
        this.tileMap = new Tile[MapHeightInTiles][];
        ArrayList<Point> pPArr = new ArrayList<>();
        int lineNum = 1,column = 0;
        CustomReader cr = new CustomReader(path);
        map[0] = new char[MapWidthInTiles + 2];
        map[1] = new char[MapWidthInTiles + 2];
        tileMap[0] = new Tile[MapWidthInTiles];
        Stack<Point> verticalTilesStack = new Stack<>();
        Stack<Point> horizontalTilesStack = new Stack<>();

        // Reads first line and performs parsing on it to get game map color specified on the first line.
        Color color = new Color(255,255,255,0); //TRANSPARENT
        int firstChar = cr.peek();

        if((char)firstChar == '#')
            color = ParseColor(cr.readLine());

        int symbol = 0;

        // Reads whole text file symbol by symbol and saves it in Map array.
        while (firstChar != -1 && symbol != -1)
        {
            column++;
            // Tests whether text file does not contain more that 31 lines.
            if (lineNum > MapHeightInTiles)
                return null;
            symbol = cr.read();
            while (symbol == 13 || symbol == 10)
                symbol = cr.read();

            // Counts number of pellets on the map.
            if (symbol == symbols[1] || symbol == symbols[2])
                numOfDots++;
            map[lineNum][column] = (char)symbol;

            //Transforms symbol in line above with use of already read surrounding symbols
            //The only one sybol not read yet is accessed via sr.peek function
            if (lineNum > 1)
            {
                tileMap[lineNum - 2][column - 1] = TransformToTile
                        (
                                map[lineNum - 1][column], map[lineNum - 1][column - 1],
                                map[lineNum - 1][column + 1], map[lineNum - 2][column],
                                (char)symbol, map[lineNum][column - 1], map[lineNum - 2][column - 1],
                                map[lineNum - 2][column + 1], (char)cr.peek(),
                                lineNum > 2 ? tileMap[lineNum - 3][column - 1] : null,
                                column > 1 ? tileMap[lineNum - 2][column - 2] : null, symbols
                        );

                // Quits whole reading in case the program was unable to identify tile.
                if (tileMap[lineNum - 2][column - 1] == null)
                    return null;

                if (tileMap[lineNum - 2][column - 1].tile == Tile.nType.POWERDOT)
                    pPArr.add(new Point(lineNum - 2, column - 1));

                // Saves Not Yet Decided Tiles for further reverse-order evaluation.
                if (tileMap[lineNum - 2][column - 1].tile == Tile.nType.VTBDTILE)
                    verticalTilesStack.push(new Point(lineNum - 2, column - 1));
                if (tileMap[lineNum - 2][column - 1].tile == Tile.nType.HTBDTILE)
                    horizontalTilesStack.push(new Point(lineNum - 2, column - 1));

            }
            if (column == 28)
            {
                // Moves to another line and initialize new array for this line.
                // Returns null in case the file has already reached 31 lines.
                column = 0;
                lineNum++;
                map[lineNum] = new char[MapWidthInTiles + 2];
                if (lineNum < MapHeightInTiles + 1)
                    tileMap[lineNum - 1] = new Tile[MapWidthInTiles];
                else return null;
            }
        }
        if(lineNum == MapHeightInTiles && column == MapWidthInTiles)
        {
            // Separately transform to tile last line because reading loop has ended on 31st line.
            lineNum++;
            for (int i = 1; i < column + 1; i++)
            {
                tileMap[lineNum - 2][i - 1] = TransformToTile
                        (
                                map[lineNum - 1][i], map[lineNum - 1][i - 1],
                                map[lineNum - 1][i + 1], map[lineNum - 2][i],
                                '\0', '\0', map[lineNum - 2][i - 1],
                                map[lineNum - 2][i + 1], '\0', tileMap[lineNum - 3][i-1],
                                i > 1 ? tileMap[lineNum - 2][i - 2] : null, symbols
                        );

                // Quits whole reading in case the program was unable to identify tile.
                if (tileMap[lineNum - 2][i - 1] == null)
                    return null;

                // Saves Not Yet Decided Tiles for further reverse-order evaluation.
                if (tileMap[lineNum - 2][i - 1].tile == Tile.nType.VTBDTILE)
                    verticalTilesStack.push(new Point(lineNum - 2, i - 1));
                if (tileMap[lineNum - 2][i - 1].tile == Tile.nType.HTBDTILE)
                    horizontalTilesStack.push(new Point(lineNum - 2, i - 1));
            }
        }

        if (horizontalTilesStack.size() > 0 || verticalTilesStack.size() > 0)
            if(!PerformReverseEvaluation(horizontalTilesStack, verticalTilesStack))
                return null;

        return new Quartet<>(map, numOfDots,color == null ? new Color(255,255,255,0) : color, pPArr);
    }

    /**
     * Iterates through not yet decided tiles in reverse order.
     * First in row neighbours with already decided tiles and enables their evaluation.
     * @param hStack Horizontal stack of to be decided tiles.
     * @param vStack Vertical stack og to be decided tiles.
     * @return Boolean
     */
    private Boolean PerformReverseEvaluation(Stack<Point> hStack, Stack<Point> vStack)
    {
        while (hStack.size() > 0)
        {
            Point pt = hStack.pop();
            if (tileMap[pt.x][pt.y + 1].tile == Tile.nType.TWALLDOUBLE || tileMap[pt.x][pt.y + 1].tile == Tile.nType.BRCURVESINGLE)
                tileMap[pt.x][pt.y].tile = Tile.nType.TWALLDOUBLE;
            else if (tileMap[pt.x][pt.y + 1].tile == Tile.nType.BWALLDOUBLE || tileMap[pt.x][pt.y + 1].tile == Tile.nType.TRCURVESINGLE)
                tileMap[pt.x][pt.y].tile = Tile.nType.BWALLDOUBLE;
            else
                return false;
        }

        while (vStack.size() > 0)
        {
            Point pt = vStack.pop();
            if (tileMap[pt.x + 1][pt.y].tile == Tile.nType.RWALLDOUBLE || tileMap[pt.x + 1][pt.y].tile == Tile.nType.BRCURVESINGLE)
                tileMap[pt.x][pt.y].tile = Tile.nType.RWALLDOUBLE;
            else if (tileMap[pt.x + 1][pt.y].tile == Tile.nType.LWALLDOUBLE || tileMap[pt.x + 1][pt.y].tile == Tile.nType.BLCURVESINGLE)
                tileMap[pt.x][pt.y].tile = Tile.nType.LWALLDOUBLE;
            else
                return false;
        }

        return true;
    }

    /**
     *  Parses first line of the map text representation file to get color in which is game field to be displayed.
     * @param line Read first line of the file.
     * @return Color in which is game field to be displayed.
     */
    private Color ParseColor(String line)
    {
        String color = line.substring(1);
        if (color.equals("RANDOM"))
            return new Color(255,255,255,0); // TRANSPARENT

        if (color.length() == 6)
        {
            return new Color(   Integer.decode(color.substring(0, 1)),
                                Integer.decode(color.substring(2, 3)),
                                Integer.decode(color.substring(4, 5))
                            );
        }
        else if (color.length() == 8)
        {
            return new Color(   Integer.decode(color.substring(0, 1)),
                                Integer.decode(color.substring(2, 3)),
                                Integer.decode(color.substring(4, 5)),
                                Integer.decode(color.substring(6, 7))
            );
        }
        return new Color(255,255,255,0); // TRANSPARENT
    }

    /**
     * Tests whether the already loaded map is playable and finishable.
     * That means: Contains Ghost House, Pacman initial position is free
     * and all the pellets are accessible for both pacman and ghosts.
     * @param map Loaded tile map.
     * @param symbols Set of 5 symbols representing tile types in to be loaded file.
     * @param numOfDots Number of pellets found on the loaded map.
     * @return Tuple indicating playability of the map and position of the ghost house on the map.
     */
    private Pair<Boolean, IntPair> IsPlayable(char[][] map, char[] symbols, int numOfDots)
    {
        final int GhostHouseSize = 38;
        final int PacManInitialY = 23;
        final int PacManInitialX = 13;

        int ghostHouse = 0, dotsFound = 0;
        IntPair ghostPosition = null;
        IntPair position = new IntPair(PacManInitialY + 1, PacManInitialX + 1);
        Boolean[][] connectedTiles = new Boolean[MapHeightInTiles + 2][];
        Stack<IntPair> stack = new Stack<>();

        if (map[position.item1][position.item2] == symbols[0] && map[position.item1][position.item2 + 1] == symbols[0])
        {
            // Performs classical BFS from pacman's initial position.
            connectedTiles[position.item1] = new Boolean[MapWidthInTiles + 2];
            connectedTiles[position.item1][position.item2] = true;
            stack.push(position);
            while ((dotsFound != numOfDots || ghostHouse != GhostHouseSize) && stack.size() > 0)
            {
                position = stack.pop();
                if (map[position.item1][position.item2] != symbols[3] &&
                        map[position.item1][position.item2] != symbols[4] && position.item2 > 0 && position.item2 < MapWidthInTiles + 1)
                {
                    // Counts number of dots accessible from starting point for further comparison.
                    if (map[position.item1][position.item2] != symbols[0])
                        dotsFound++;
                    if (connectedTiles[position.item1] == null)
                        connectedTiles[position.item1] = new Boolean[MapWidthInTiles + 2];
                    if (connectedTiles[position.item1 - 1] == null)
                        connectedTiles[position.item1 - 1] = new Boolean[MapWidthInTiles + 2];
                    if (connectedTiles[position.item1 + 1] == null)
                        connectedTiles[position.item1 + 1] = new Boolean[MapWidthInTiles + 2];
                    if (!connectedTiles[position.item1][position.item2 - 1])
                    {
                        stack.push(new IntPair(position.item1, position.item2 - 1));
                        connectedTiles[position.item1][position.item2 - 1] = true;
                    }
                    if (!connectedTiles[position.item1][position.item2 + 1])
                    {
                        stack.push(new IntPair(position.item1, position.item2 + 1));
                        connectedTiles[position.item1][position.item2 + 1] = true;
                    }
                    if (!connectedTiles[position.item1 - 1][position.item2])
                    {
                        stack.push(new IntPair(position.item1 - 1, position.item2));
                        connectedTiles[position.item1 - 1][position.item2] = true;
                    }
                    if (!connectedTiles[position.item1 + 1][position.item2])
                    {
                        stack.push(new IntPair(position.item1 + 1, position.item2));
                        connectedTiles[position.item1 + 1][position.item2] = true;
                    }

                    if (map[position.item1 + 1][position.item2] == symbols[4] && map[position.item1 + 1][position.item2 + 1] == symbols[4])
                    {
                        //If the algorithm has reached gate Tiles searches tiles underneath to find out if the contain ghost House
                        //The algorithm knows how the ghost house should look and tests each tile if it contains what it should
                        //the algorithm increases ghostHouse for each successfull comparison
                        if (map[position.item1][position.item2] == symbols[0] && map[position.item1][position.item2 + 1] == symbols[0])
                        {
                            ghostPosition = new IntPair(position.item2, position.item1);
                            // Saves location of tiles above the gate for further in-game use.
                            for (int k = 1; k < 6; k++)
                                for (int l = -3; l < 5; l++)
                                    switch (k)
                                    {
                                        case 1:
                                            if (map[position.item1 + k][position.item2 + l] == symbols[3] && (l != 0 || l != 1))
                                                ghostHouse++;
                                            break;
                                        case 5:
                                            if (map[position.item1 + k][position.item2 + l] == symbols[3])
                                                ghostHouse++;
                                            break;
                                        default:
                                            if (map[position.item1 + k][position.item2 + l] == symbols[3] && (l == -3 || l == 4))
                                                ghostHouse++;
                                            else if (map[position.item1 + k][position.item2 + l] == symbols[0])
                                                ghostHouse++;
                                            break;
                                    }
                        }
                    }
                }
            }

            // Compares number of found pellets and ghostHouse and decides whether the map is playable or not.
            // The fact that ghost house was found means it is accessible from pacman's starting position.
            if (dotsFound == numOfDots && ghostHouse == GhostHouseSize)
                return new Pair<>(true, ghostPosition);
            else return new Pair<>(false, null);
        }
        else return new Pair<>(false, null);
    }

    /**
     * Function that transforms read symbol to tile.
     * @param tile Currently processed symbol.
     * @param tileLeft Symbol to the left.
     * @param tileRight Symbol to the right.
     * @param tileUp Symbol above.
     * @param tileDown Symbol under.
     * @param BLCorner Symbol in bottom-left corner.
     * @param TLCorner Symbol in top-left corner.
     * @param TRCorner Symbol in top-right corner.
     * @param BRCorner Symbol in bottom-right corner.
     * @param upperTile Already translated tile above.
     * @param leftTile Already translated tile to the left.
     * @param symbols Set of 5 symbols representing tile types in to be loaded file.
     * @return Returns input symbol translated to the program's tile alphabet.
     */
    private Tile TransformToTile
    (
            char tile, char tileLeft, char tileRight,
            char tileUp, char tileDown, char BLCorner,
            char TLCorner,char TRCorner, char BRCorner,
            Tile upperTile, Tile leftTile,
            char[] symbols)
    {
        if (tile == symbols[0])
            return new Tile(" ");
        else if (tile == symbols[1])
            return new Tile(".");
        else if (tile == symbols[2])
            return new Tile("o");
        else if (tile == symbols[3])
        {
            // Determines type of wall with use of adjacent symbols and tiles.
            if (tileLeft == symbols[3] && tileRight == symbols[3] &&
                    tileUp == symbols[3] && tileDown == symbols[3])
            {
                if (TLCorner == symbols[0] || TLCorner == symbols[1] || TLCorner == symbols[2])
                    return new Tile("SCBR");
                else if (TRCorner == symbols[0] || TRCorner == symbols[1] || TRCorner == symbols[2])
                    return new Tile("SCBL");
                else if (BRCorner == symbols[0] || BRCorner == symbols[1] || BRCorner == symbols[2])
                    return new Tile("SCTL");
                else if (BLCorner == symbols[0] || BLCorner == symbols[1] || BLCorner == symbols[2])
                    return new Tile("SCTR");
                else return null;
            }
            else if ((tileLeft == symbols[3] || tileLeft == symbols[4]) &&
                    (tileRight == symbols[3] || tileRight == symbols[4]) &&
                    (tileUp != symbols[3] || tileDown != symbols[3]) || (tileRight == '\0' && tileUp != symbols[3] && tileDown != symbols[3]))
            {
                if (tileUp == symbols[3])
                    return new Tile("SWB");
                else if (tileDown == symbols[3])
                    return new Tile("SWT");
                else if (tileUp == symbols[1] || tileUp == symbols[2] || leftTile.tile == Tile.nType.BWALLDOUBLE || leftTile.tile == Tile.nType.TLCURVESINGLE)
                    return new Tile("DWB");
                else if (tileDown == symbols[1] || tileDown == symbols[2] || leftTile.tile == Tile.nType.TWALLDOUBLE || leftTile.tile == Tile.nType.BLCURVESINGLE)
                    return new Tile("DWT");
                else if (tileUp == symbols[0])
                    return new Tile("HTBDTILE");
                else if (tileDown == symbols[0])
                    return new Tile("HTBDTILE");
                else return null;
            }
            else if (tileUp == symbols[3] && tileDown == symbols[3])
            {
                if (tileRight == symbols[3])
                    return new Tile("SWL");
                else if (tileLeft == symbols[3])
                    return new Tile("SWR");
                else if (tileRight == symbols[1] || tileRight == symbols[2])
                    return new Tile("DWL");
                else if (tileLeft == symbols[1] || tileLeft == symbols[2])
                    return new Tile("DWR");
                else if (tileLeft == symbols[0] && (upperTile.tile == Tile.nType.LWALLDOUBLE || upperTile.tile == Tile.nType.TRCURVESINGLE))
                    return new Tile("DWL");
                else if (tileRight == symbols[0] && (upperTile.tile == Tile.nType.RWALLDOUBLE || upperTile.tile == Tile.nType.TLCURVESINGLE))
                    return new Tile("DWR");
                else return null;
            }
            else if (tileUp == symbols[3] && tileLeft == symbols[3])
            {
                if (TLCorner == symbols[3] || TLCorner == symbols[0])
                    return new Tile("SCBR");
                else if (TLCorner == symbols[1] || TLCorner == symbols[2])
                    return new Tile("DCBR");
                else return null;
            }
            else if (tileRight == symbols[3] && tileDown == symbols[3])
            {
                if (BRCorner == symbols[3] || BRCorner == symbols[0])
                    return new Tile("SCTL");
                else if (BRCorner == symbols[1] || BRCorner == symbols[2])
                    return new Tile("DCTL");
                else return null;
            }
            else if (tileUp == symbols[3] && tileRight == symbols[3])
            {
                if (TRCorner == symbols[3] || TRCorner == symbols[0])
                    return new Tile("SCBL");
                else if (TRCorner == symbols[1] || TRCorner == symbols[2])
                    return new Tile("DCBL");
                else return null;
            }
            else if (tileLeft == symbols[3] && tileDown == symbols[3])
            {
                if (BLCorner == symbols[3] || BLCorner == symbols[0])
                    return new Tile("SCTR");
                else if (BLCorner == symbols[1] || BLCorner == symbols[2])
                    return new Tile("DCTR");
                else return null;
            }
            else if (tileLeft == '\0' && tileRight == symbols[3])
                return new Tile("HTBDTILE");
            else if ((tileUp == '\0' && tileDown == symbols[3]) ||
                    (tileDown == '\0' && tileUp == symbols[3]))
                return new Tile("VTBDTILE");
            else return null;
        }
        else if (tile == symbols[4])
            return new Tile("X");
        else return null;
    }
}