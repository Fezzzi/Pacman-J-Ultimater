package pacman_ultimater.project_base.core;

import pacman_ultimater.project_base.custom_utils.IntPair;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class providing methods for AI movement and enumerable for easy programmatically recognition of entities.
 */
public class DefaultAI {
    /*  Instruction for adding another AI algorithm:
            1) Create Class that inherits from DefaultAI
            2) Override methods for AI's behaviour:
                2.1) HostileAttack - entity is hostile and attacking pacman
                2.2) HostileRetreat - entity is hostile but is retreating from attack
                2.3) Eaten - entity has been eaten
    *///        2.4) CanBeEaten - entity can be eaten by pacman

    private final nType State;
    private final int fieldSizeInColumns, fieldSizeInRows;

    /**
     * @param state Entity's state such as Player1, Player2, Hostile....
     * @param tsc   Tile Map Size in Columns.
     * @param tsr   Tile Map Size in Rows.
     */
    public DefaultAI(nType state, int tsc, int tsr)
    {
        this.State = state;
        fieldSizeInColumns = tsc;
        fieldSizeInRows = tsr;
    }

    /**
     * Enumerable that characterizes all the possible entity's states.
     */
    public enum nType { PLAYER1, PLAYER2, HOSTILEATTACK, HOSTILERETREAT, CANBEEATEN, EATEN }

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
    public Direction.nType NextStep(IntPair position, IntPair target, Direction.nType direction, Tile[][] map)
    {
        if (State == nType.HOSTILERETREAT)
            return HostileRetreat(position, target, direction, map);
        else if (State == nType.EATEN)
            return Eaten(position, target, direction, map);
        else if (State == nType.CANBEEATEN)
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
    private Direction.nType HostileAttack(IntPair position, IntPair target, Direction.nType direction, Tile[][] map)
    {
        return RandomAI(position, direction, map);
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
    private Direction.nType HostileRetreat(IntPair position, IntPair target, Direction.nType direction, Tile[][] map)
    {
        return RandomAI(position, direction, map);
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
    private Direction.nType Eaten(IntPair position, IntPair target, Direction.nType direction, Tile[][] map)
    {
        return RandomAI(position, direction, map);
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
    private Direction.nType CanBeEaten(IntPair position, IntPair target, Direction.nType direction, Tile[][] map)
    {
        return RandomAI(position, direction, map);
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
    private Direction.nType RandomAI(IntPair position, Direction.nType direction, Tile[][] map)
    {
        Random rndm = new Random();
        ArrayList<IntPair> possibilities = new ArrayList<>();
        Direction dir = new Direction();
        IntPair back = dir.directionToIntPair(direction);
        back = new IntPair(back.item1 * -1, back.item2 * -1);

        for (int j = 0; j < 2; j++) {
            for (int i = -1; i < 2; i += 2) {
                int deltaX = (j == 0 ? i : 0);
                int deltaY = (j == 1 ? i : 0);
                if (position.item1 + deltaY < 0) {
                    if (direction == Direction.nType.LEFT)
                        possibilities.add(new IntPair(deltaX, deltaY));
                } else if (position.item1 + deltaY >= fieldSizeInColumns) {
                    if (direction == Direction.nType.RIGHT)
                        possibilities.add(new IntPair(deltaX, deltaY));
                } else if (position.item2 + deltaX < 0) {
                    if (direction == Direction.nType.UP)
                        possibilities.add(new IntPair(deltaX, deltaY));
                } else if (position.item2 + deltaX >= fieldSizeInRows) {
                    if (direction == Direction.nType.DOWN)
                        possibilities.add(new IntPair(deltaX, deltaY));
                } else if (CanAdd(map[position.item2 + deltaX][position.item1 + deltaY])
                        && (deltaX != back.item1 || deltaY != back.item2))
                    possibilities.add(new IntPair(deltaX, deltaY));
            }
        }

        if (possibilities.size() > 0)
            return dir.intPairToDirection(possibilities.get(rndm.nextInt(possibilities.size())));
        else if (direction != Direction.nType.DIRECTION)
            return dir.intPairToDirection(back);
        else
            return Direction.nType.DIRECTION;
    }

    /**
     * Tests if the examined tile is free to move on.
     * Returns boolean indicating occupancy of the examined tile.
     *
     * @param tile The examined tile.
     * @return boolean
     */
    private boolean CanAdd(Tile tile)
    {
        return (tile.tile == Tile.nType.DOT || tile.tile == Tile.nType.POWERDOT || tile.tile == Tile.nType.FREE);
    }
}
