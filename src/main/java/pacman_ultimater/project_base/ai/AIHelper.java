package pacman_ultimater.project_base.ai;

import pacman_ultimater.project_base.core.Direction;
import pacman_ultimater.project_base.core.LoadMap;
import pacman_ultimater.project_base.custom_utils.IntPair;

/**
 * Helper class for AIs, providing tools for target tiles recomputing
 */
class AIHelper
{
    private final Boolean[][] connectedTiles;

    AIHelper(Boolean[][] connectedTiles)
    {
        this.connectedTiles = connectedTiles;
    }

    /**
     * Finds the closest crossroad ahead of the pacman
     *
     * @param pacPosition IntPair
     * @param pacDirection Direction.directionType
     * @return IntPair
     */
    IntPair findPacmansCrossRoad(IntPair pacPosition, Direction.directionType pacDirection)
    {
        IntPair delta = Direction.directionToIntPair(pacDirection);
        if (isCorner(pacPosition.item1, pacPosition.item2)) {
            Direction.directionType newDirection = turnDirection(pacPosition.item1, pacPosition.item2, delta.item1, delta.item2);
            IntPair nd = Direction.directionToIntPair(newDirection);
            return findPacmansCrossRoad(
                    new IntPair(pacPosition.item1 + nd.item1, pacPosition.item2 + nd.item2), newDirection);
        }

        if (delta.item1 == 0) {
            for (int y = pacPosition.item2; y >= 0 && y < LoadMap.MAPHEIGHTINTILES; y += delta.item2) {
                if (isCrossroad(pacPosition.item1, y)) {
                    return new IntPair(pacPosition.item1, y);
                } else if (isCorner(pacPosition.item1, y)) {
                    Direction.directionType newDirection = turnDirection(pacPosition.item1, y, delta.item1, delta.item2);
                    IntPair nd = Direction.directionToIntPair(newDirection);
                    return findPacmansCrossRoad(new IntPair(pacPosition.item1 + nd.item1, y + nd.item2), newDirection);
                }
            }
        } else {
            for (int x = pacPosition.item1; x >= 0 && x < LoadMap.MAPWIDTHINTILES; x += delta.item1) {
                if (isCrossroad(x, pacPosition.item2)) {
                    return new IntPair(x, pacPosition.item2);
                } else if (isCorner(x, pacPosition.item2)) {
                    Direction.directionType newDirection = turnDirection(x, pacPosition.item2, delta.item1, delta.item2);
                    IntPair nd = Direction.directionToIntPair(newDirection);
                    return findPacmansCrossRoad(new IntPair(x + nd.item1, pacPosition.item2 + nd.item2), newDirection);
                }
            }
        }

        return pacPosition;
    }

    /**
     * Locates target tile for CANBEEATEN mode
     *
     * @param position IntPair
     * @param pacman IntPair
     * @return IntPair
     */
    IntPair findSafety(IntPair position, IntPair pacman)
    {
        // TODO: Implement function
        return null;
    }

    /**
     * Changes direction at corner
     *
     * @param x int
     * @param y int
     * @param dx int
     * @param dy int
     * @return Direction.directionType
     */
    private Direction.directionType turnDirection (int x, int y, int dx, int dy)
    {
        if (connectedTiles[y + dx][x + dy] != null)
            return Direction.intPairToDirection(new IntPair(dy, dx));

        return Direction.intPairToDirection(new IntPair(-dy, -dx));
    }

    /**
     * Denotes whether the given tile is a corner
     *
     * @param x int
     * @param y int
     * @return IntPair
     */
    private boolean isCorner(int x, int y)
    {
        return x-1 >= 0 && x+1 < LoadMap.MAPWIDTHINTILES && y-1 >= 0 && y+1 < LoadMap.MAPHEIGHTINTILES && (
                (connectedTiles[y][x+1] != null && (connectedTiles[y+1][x] != null || connectedTiles[y-1][x] != null))
                        || (connectedTiles[y][x-1] != null && (connectedTiles[y+1][x] != null || connectedTiles[y-1][x] != null)));
    }

    /**
     * Denotes whether tile x,y is a crossroad
     *
     * @param x int
     * @param y int
     * @return boolean
     */
    private boolean isCrossroad(int x, int y)
    {
        byte count = 0;
        count += x + 1 < LoadMap.MAPWIDTHINTILES ? (connectedTiles[y][x + 1] != null ? 1 : 0) : 0;
        count += x - 1 >= 0 ? (connectedTiles[y][x - 1] != null ? 1 : 0) : 0;
        count += y + 1 < LoadMap.MAPHEIGHTINTILES ? (connectedTiles[y + 1][x] != null ? 1 : 0) : 0;
        count += y - 1 >= 0 ? (connectedTiles[y - 1][x] != null ? 1 : 0) : 0;
        return count > 2;
    }
}
