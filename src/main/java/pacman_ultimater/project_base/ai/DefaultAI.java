package pacman_ultimater.project_base.ai;

import pacman_ultimater.project_base.core.Direction;
import pacman_ultimater.project_base.core.LoadMap;
import pacman_ultimater.project_base.core.Tile;
import pacman_ultimater.project_base.custom_utils.IntPair;
import pacman_ultimater.project_base.custom_utils.Triplet;

import java.util.*;

/**
 * Class providing methods for AI movement and enumerable for easy programmatically recognition of entities.
 *
 * Instruction for adding another AI algorithm:
 *      1) Create Class that extends DefaultAI
 *      2) Override methods for AI's behaviour (HostileAttack/CanBeEaten)
 *      +) Eaten and HostileRetreat can be pverriden as well althouh it is not reccommended
 */
public class DefaultAI
{
    public nType state;
    public Direction.directionType prevDirection;
    private final IntPair retreatTile;

    /**
     * @param state Entity's state such as NoAI, Hostile....
     * @param ghostId int ghost identifier
     * @param connectedTiles Boolean[][]
     */
    public DefaultAI(nType state, int ghostId, Boolean [][] connectedTiles)
    {
        this.state = state;
        prevDirection = Direction.directionType.DIRECTION;
        if (state != nType.NOAI) {
            retreatTile = findRetreatTile(ghostId, connectedTiles);
        } else {
            retreatTile = null;
        }
    }

    /**
     * Enumerable that characterizes all the possible entity's states.
     */
    public enum nType {
        /**
         * No AI, movement is computed via keyboard input.
         */
        NOAI,
        /**
         * AI that should actively attempt to get to pacman.
         */
        HOSTILEATTACK,
        /**
        AI that should aims for one of four corners.
         */
        HOSTILERETREAT,
        /**
         * AI for situation when ghost are blue.
         */
        CANBEEATEN,
        /**
         * AI for eaten ghosts, should aim for ghost house.
         */
        EATEN }

    /**
     * Function that chooses entity's next position based on set AI algorithms and entity's current state.
     * Returns chosen direction for the entity.
     *
     * @param position IntPair
     * @param target IntPair
     * @param direction Direction.directionType
     * @param pacmanDirection Direction.directionType
     * @param map Tile[][]
     * @return Direction.nType
     */
    final public Direction.directionType NextStep(IntPair position, IntPair target,
            Direction.directionType direction, Direction.directionType pacmanDirection, Tile[][] map)
    {
        if (state == nType.HOSTILERETREAT)
            return HostileRetreat(position, target, direction, map);
        else if (state == nType.EATEN)
            return Eaten(position, target, direction, map);
        else if (state == nType.CANBEEATEN)
            return CanBeEaten(position, target, direction, map);
        else
            return HostileAttack(position, target, direction, pacmanDirection, map);
    }

    /**
     * AI Algorithm choosing next position for hostile entities during their attack phase.
     * Returns chosen direction for the entity.
     *
     * @param position IntPair
     * @param target IntPair
     * @param direction Direction.directionType
     * @param pacmanDirection Direction.directionType
     * @param map Tile[][]
     * @return Direction.nType
     */
    protected Direction.directionType HostileAttack(IntPair position, IntPair target,
            Direction.directionType direction, Direction.directionType pacmanDirection, Tile[][] map)
    {
        return randomAI(position, direction, map);
    }

    /**
     * AI Algorithm choosing next position for hostile entities during their retreat phase.
     * Returns chosen direction for the entity.
     *
     * @param position IntPair
     * @param target IntPair
     * @param direction Direction.directionType
     * @param map Tile[][]
     * @return Direction.nType
     */
    protected Direction.directionType HostileRetreat(IntPair position, IntPair target,
             Direction.directionType direction, Tile[][] map)
    {
        return bfsAI(position, retreatTile, direction, map, 0.75f);
    }

    /**
     * AI Algorithm choosing next position for eaten entities.
     * Returns chosen direction for the entity.
     *
     * @param position IntPair
     * @param target IntPair
     * @param direction Direction.directionType
     * @param map Tile[][]
     * @return Direction.nType
     */
    protected Direction.directionType Eaten(IntPair position, IntPair target,
            Direction.directionType direction, Tile[][] map)
    {
        return bfsAI(position, target, direction, map, 1);
    }

    /**
     * AI Algorithm choosing next position for vulnerable entities.
     * Returns chosen direction for the entity.
     *
     * @param position IntPair
     * @param target IntPair
     * @param direction Direction.directionType
     * @param map Tile[][]
     * @return Direction.nType
     */
    protected Direction.directionType CanBeEaten(IntPair position, IntPair target,
            Direction.directionType direction, Tile[][] map)
    {
        return randomAI(position, direction, map);
    }

    /**
     * Random AI algorithm that decides new direction randomly at each crossroad.
     * if there is no other way, AI chooses direction it came from or stops.
     * Returns chosen direction for the entity.
     *
     * @param position  The entity's position.
     * @param direction The entity's current direction.
     * @param map       Game map in tiles.
     * @return Direction.nType
     */
    private Direction.directionType randomAI(IntPair position, Direction.directionType direction, Tile[][] map)
    {
        Random rndm = new Random();
        IntPair back = Direction.directionToIntPair(direction != Direction.directionType.DIRECTION ? direction : prevDirection);
        back = new IntPair(back.item1 * -1, back.item2 * -1);

        ArrayList<IntPair> possibilities = loadPossibilities(position, direction, map, back);
        if (possibilities.size() > 0)
            return Direction.intPairToDirection(possibilities.get(rndm.nextInt(possibilities.size())));
        else if (direction != Direction.directionType.DIRECTION)
            return Direction.intPairToDirection(back);
        else
            return Direction.directionType.DIRECTION;
    }

    /**
     * Finds fastest way from position tile to target tile and returns the first direction on this route.
     * There is also probability for the ghost to choose random insted.
     *
     * @param position IntPair
     * @param target IntPair
     * @param direction Direction.directionType
     * @param map Tile[][]
     * @param randomness float
     * @return Direction.directionType
     */
    final Direction.directionType bfsAI (IntPair position, IntPair target,
             Direction.directionType direction, Tile[][] map, float randomness)
    {
        if ((new Random()).nextFloat() < randomness) {
            IntPair back = Direction.directionToIntPair(direction != Direction.directionType.DIRECTION ? direction : prevDirection);
            ArrayList<IntPair> possibilities;
            boolean[][] used = new boolean[LoadMap.MAPHEIGHTINTILES][LoadMap.MAPWIDTHINTILES];
            ArrayDeque<Triplet<IntPair, IntPair, Direction.directionType>> queue = new ArrayDeque<>();

            back = new IntPair(back.item1 * -1, back.item2 * -1);
            possibilities = loadPossibilities(position, direction, map, back);
            for (IntPair newPosition : possibilities) {
                IntPair candidate = new IntPair(position.item1 + newPosition.item1, position.item2 + newPosition.item2);
                Direction.directionType newDirection = Direction.intPairToDirection(newPosition);
                if (candidate.equals(target))
                    return newDirection;

                if (!used[candidate.item2][candidate.item1]) {
                    used[candidate.item2][candidate.item1] = true;
                    queue.push(new Triplet<>(position, candidate, newDirection));
                }
            }
            used[position.item2][position.item1] = true;

            while (!queue.isEmpty()) {
                Triplet<IntPair, IntPair, Direction.directionType> positions = queue.pop();
                back = new IntPair(
                        (positions.item2.item1 - positions.item1.item1) * -1,
                        (positions.item2.item2 - positions.item1.item2) * -1);
                possibilities = loadPossibilities(positions.item2, direction, map, back);

                for (IntPair newPosition : possibilities) {
                    IntPair candidate = new IntPair(
                            (positions.item2.item1 + newPosition.item1 + LoadMap.MAPWIDTHINTILES) % LoadMap.MAPWIDTHINTILES,
                            (positions.item2.item2 + newPosition.item2 + LoadMap.MAPHEIGHTINTILES) % LoadMap.MAPHEIGHTINTILES);
                    if (candidate.equals(target))
                        return positions.item3;

                    if (!used[candidate.item2][candidate.item1]) {
                        used[candidate.item2][candidate.item1] = true;
                        queue.addLast(new Triplet<>(positions.item2, candidate, positions.item3));
                    }
                }
            }
        }

        return randomAI(position, direction, map);
    }

    /**
     * Finds possible neighbour tiles to move on
     *
     * @param position IntPair
     * @param direction Direction.directionType
     * @param map Tile[][]
     * @param back IntPair
     * @return ArrayList
     */
    private ArrayList<IntPair> loadPossibilities(IntPair position,
            Direction.directionType direction, Tile[][] map, IntPair back)
    {
        ArrayList<IntPair> possibilities = new ArrayList<>();

        for (int j = 0; j < 2; j++) {
            for (int i = -1; i < 2; i += 2) {
                int deltaX = (j == 0 ? i : 0);
                int deltaY = (j == 1 ? i : 0);
                if (position.item1 + deltaX < 0) {
                    if (direction == Direction.directionType.LEFT)
                        possibilities.add(new IntPair(deltaX, deltaY));
                } else if (position.item1 + deltaX >= LoadMap.MAPWIDTHINTILES) {
                    if (direction == Direction.directionType.RIGHT)
                        possibilities.add(new IntPair(deltaX, deltaY));
                } else if (position.item2 + deltaY < 0) {
                    if (direction == Direction.directionType.UP)
                        possibilities.add(new IntPair(deltaX, deltaY));
                } else if (position.item2 + deltaY >= LoadMap.MAPHEIGHTINTILES) {
                    if (direction == Direction.directionType.DOWN)
                        possibilities.add(new IntPair(deltaX, deltaY));
                } else if (canAdd(map[position.item2 + deltaY][position.item1 + deltaX])
                        && (deltaX != back.item1 || deltaY != back.item2))
                    possibilities.add(new IntPair(deltaX, deltaY));
            }
        }

        return possibilities;
    }

    /**
     * Tests if the examined tile is free to move on.
     * Returns boolean indicating occupancy of the examined tile.
     *
     * @param tile The examined tile.
     * @return boolean
     */
    private boolean canAdd(Tile tile)
    {
        return (tile.tile == Tile.nType.DOT || tile.tile == Tile.nType.POWERDOT || tile.tile == Tile.nType.FREE);
    }

    /**
     * Finds reatreat tile for given ghost
     *
     * @param idGhost int
     * @return IntPair
     */
    private IntPair findRetreatTile(int idGhost, Boolean[][] connectedTiles)
    {
        int deltaX, deltaY, startX, startY;
        if (idGhost < 3) {
            startY = 0;
            deltaY = 1;
        } else {
            startY = LoadMap.MAPHEIGHTINTILES - 1;
            deltaY = -1;
        }

        if (idGhost == 1 || idGhost == 4) {
            startX = LoadMap.MAPWIDTHINTILES - 1;
            deltaX = -1;
        } else {
            startX = 0;
            deltaX = 1;
        }

        int iterationX = 0, iterationY = 0;
        boolean iterateX = true, iterateY = true;
        //We will always find some free tiles asmap wil be invalid otherwise
        while (true) {
            if (connectedTiles[startY + deltaY*iterationY][startX + deltaX*iterationX] != null)
                return new IntPair(startX + deltaX*iterationX, startY + deltaY*iterationY);

            for (int i = startX; iterateX && (i - startX) != iterationX*deltaX; i += deltaX) {
                if (connectedTiles[startY + deltaY*iterationY][i] != null)
                    return new IntPair(i, startY + deltaY*iterationY);
            }

            for (int i = startY; iterateY && (i - startY) != iterationY*deltaY; i += deltaY) {
                if (connectedTiles[i][startX + deltaX*iterationX] != null)
                    return new IntPair(startX + deltaX*iterationX, i);
            }

            if (iterateX
            && (iterationX + 1)*deltaX + startX >= 0 && (iterationX + 1)*deltaX + startX < LoadMap.MAPWIDTHINTILES)
                ++iterationX;
            else
                iterateX = false;

            if (iterateY
            && (iterationY + 1)*deltaY + startY >= 0 && (iterationY + 1)*deltaY + startY < LoadMap.MAPHEIGHTINTILES)
                ++iterationY;
            else
                iterateY = false;
        }
    }
}
