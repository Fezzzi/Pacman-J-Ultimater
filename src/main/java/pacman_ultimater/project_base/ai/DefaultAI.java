package pacman_ultimater.project_base.ai;

import pacman_ultimater.project_base.core.Direction;
import pacman_ultimater.project_base.core.LoadMap;
import pacman_ultimater.project_base.core.Tile;
import pacman_ultimater.project_base.custom_utils.IntPair;
import pacman_ultimater.project_base.custom_utils.Pair;

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
     * @param map Tile[][]
     */
    public DefaultAI(nType state, int ghostId, Tile[][] map)
    {
        this.state = state;
        prevDirection = Direction.directionType.DIRECTION;
        retreatTile = findRetreatTile(ghostId, map);
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
     * @param position  The entity's position.
     * @param target    Target tile.
     * @param direction The entity's current direction.
     * @param map       Game map in tiles.
     * @return Direction.nType
     */
    final public Direction.directionType NextStep(IntPair position, IntPair target,
            Direction.directionType direction, Tile[][] map)
    {
        if (state == nType.HOSTILERETREAT)
            return HostileRetreat(position, target, direction, map);
        else if (state == nType.EATEN)
            return Eaten(position, target, direction, map);
        else if (state == nType.CANBEEATEN)
            return CanBeEaten(position, target, direction, map);
        else
            return HostileAttack(position, target, direction, map);
    }

    /**
     * AI Algorithm choosing next position for hostile entities during their attack phase.
     * Returns chosen direction for the entity.
     *
     * @param position  The entity's position.
     * @param target    Target tile (Usually pacman's location).
     * @param direction The entity's current direction.
     * @param map       Game map in tiles.
     * @return Direction.nType
     */
    protected Direction.directionType HostileAttack(IntPair position, IntPair target,
            Direction.directionType direction, Tile[][] map)
    {
        return randomAI(position, direction, map);
    }

    /**
     * AI Algorithm choosing next position for hostile entities during their retreat phase.
     * Returns chosen direction for the entity.
     *
     * @param position  The entity's position.
     * @param target    Target tile (Usually some corner tile).
     * @param direction The entity's current direction.
     * @param map       Game map in tiles.
     * @return Direction.nType
     */
    protected Direction.directionType HostileRetreat(IntPair position, IntPair target,
             Direction.directionType direction, Tile[][] map)
    {
        return bfsAI(position, retreatTile, direction, map);
        //return randomAI(position, direction, map);
    }

    /**
     * AI Algorithm choosing next position for eaten entities.
     * Returns chosen direction for the entity.
     *
     * @param position  The entity's position.
     * @param target    Target tile (Usually ghost house entrance tile).
     * @param direction The entity's current direction.
     * @param map       Game map in tiles.
     * @return Direction.nType
     */
    protected Direction.directionType Eaten(IntPair position, IntPair target,
            Direction.directionType direction, Tile[][] map)
    {
        return randomAI(position, direction, map);
    }

    /**
     * AI Algorithm choosing next position for vulnerable entities.
     * Returns chosen direction for the entity.
     *
     * @param position  The entity's position.
     * @param target    Target tile.
     * @param direction The entity's current direction.
     * @param map       Game map in tiles.
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

    final Direction.directionType bfsAI (IntPair position, IntPair target,
             Direction.directionType direction, Tile[][] map)
    {
        IntPair back = Direction.directionToIntPair(direction != Direction.directionType.DIRECTION ? direction : prevDirection);
        back = new IntPair(back.item1 * -1, back.item2 * -1);
        ArrayList<IntPair> possibilities;
        Set<IntPair> used = new HashSet<>();
        LinkedList<Pair<IntPair, IntPair>> queue = new LinkedList<>(){};
        queue.push(new Pair<>(new IntPair(position.item1 + back.item1, position.item2 + back.item2),position));

        while(!queue.isEmpty()) {
            Pair<IntPair, IntPair> positions = queue.getFirst();
            back = new IntPair(positions.item2.item1 - positions.item1.item1, positions.item2.item2 - positions.item1.item2);
            Direction.directionType newDirection = Direction.intPairToDirection(back);
            back = new IntPair(back.item1 * -1, back.item2 * -1);

            possibilities = loadPossibilities(positions.item2, direction, map, back);

            for (IntPair newPosition : possibilities) {
                if (newPosition.equals(target))
                    return newDirection;

                if (!used.contains(newPosition)) {
                    used.add(newPosition);
                    queue.push(new Pair<>(positions.item2, newPosition));
                }
            }
        }

        return Direction.directionType.DIRECTION;
        //return bfs_reccursion(position, target, new HashSet<>(), possibilities, map);
    }

//    private Direction.directionType bfs_reccursion (IntPair position, IntPair target,
//            HashSet<IntPair> used, ArrayList<IntPair> stack, Tile[][] map)
//    {
//        used.add(position);
//        for (IntPair newPosition: stack)
//        {
//            IntPair back = new IntPair(position.item1 - newPosition.item1, position.item2 - newPosition.item2);
//            Direction.directionType newDirection = Direction.intPairToDirection(back);
//
//            if (newPosition.equals(target)) {
//                return newDirection;
//            }
//
//            back = new IntPair(back.item1 * -1, back.item2 * -1);
//            ArrayList<IntPair> possibilities = loadPossibilities(newPosition, newDirection, map, back);
//
//            for (IntPair possibility: possibilities) {
//                if (!used.contains(possibility)) {
//                    bfs_reccursion()
//                }
//            }
//        }
//    }

    /**
     * Finds possible neighbour tiles to move on
     *
     * @param position IntPair
     * @param direction Direction.directionType
     * @param map Tile[][]
     * @param back IntPair
     * @return ArrayList
     */
    final ArrayList<IntPair> loadPossibilities(IntPair position,
                                               Direction.directionType direction, Tile[][] map, IntPair back)
    {
        ArrayList<IntPair> possibilities = new ArrayList<>();

        for (int j = 0; j < 2; j++) {
            for (int i = -1; i < 2; i += 2) {
                int deltaX = (j == 0 ? i : 0);
                int deltaY = (j == 1 ? i : 0);
                if (position.item1 + deltaY < 0) {
                    if (direction == Direction.directionType.LEFT)
                        possibilities.add(new IntPair(deltaX, deltaY));
                } else if (position.item1 + deltaY >= LoadMap.MAPWIDTHINTILES) {
                    if (direction == Direction.directionType.RIGHT)
                        possibilities.add(new IntPair(deltaX, deltaY));
                } else if (position.item2 + deltaX < 0) {
                    if (direction == Direction.directionType.UP)
                        possibilities.add(new IntPair(deltaX, deltaY));
                } else if (position.item2 + deltaX >= LoadMap.MAPHEIGHTINTILES) {
                    if (direction == Direction.directionType.DOWN)
                        possibilities.add(new IntPair(deltaX, deltaY));
                } else if (canAdd(map[position.item2 + deltaX][position.item1 + deltaY])
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
    boolean canAdd(Tile tile)
    {
        return (tile.tile == Tile.nType.DOT || tile.tile == Tile.nType.POWERDOT || tile.tile == Tile.nType.FREE);
    }

    /**
     * Finds reatreat tile for given ghost
     *
     * @param idGhost int
     * @return IntPair
     */
    private IntPair findRetreatTile(int idGhost, Tile[][] map)
    {
        switch (idGhost) {
            case 1:
                return new IntPair(LoadMap.MAPWIDTHINTILES - 1, 0);
            case 2:
                return new IntPair(0, 0);
            case 3:
                return new IntPair(0, LoadMap.MAPHEIGHTINTILES - 1);
            default:
                return new IntPair(LoadMap.MAPWIDTHINTILES - 1, LoadMap.MAPWIDTHINTILES - 1);
        }
    }
}
