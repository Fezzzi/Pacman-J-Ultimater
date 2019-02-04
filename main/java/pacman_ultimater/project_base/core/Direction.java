package pacman_ultimater.project_base.core;

import pacman_ultimater.project_base.custom_utils.IntPair;

/**
 * Class providing data structure for directions via enumerable nType.
 * It also provides two useful functions for conversions.
 */
public class Direction {

    /**
     * Enumerable representing possible directions.
     */
    public enum nType { LEFT, RIGHT, UP, DOWN, DIRECTION }

    /**
     * Function for converting from direction to delta tuple.
     * Returns input's representation as delta tuple of two integers.
     *
     * @param direction direction representation in form of nType
     * @return IntPair
     */
    IntPair directionToIntPair(nType direction)
    {
        switch (direction)
        {
            case DOWN:
                return new IntPair(1, 0);
            case UP:
                return new IntPair(-1, 0);
            case LEFT:
                return new IntPair(0, -1);
            case RIGHT:
                return new IntPair(0, 1);
            default:
                return new IntPair(0, 0);
        }
    }

    /**
     * Function for converting from delta tuple to nType direction.
     * Returns input's representation as member of nType enumerable of possible directions.
     *
     * @param intPair Input delta tuple of two integers.
     * @return nType
     */
    nType intPairToDirection(IntPair intPair)
    {
        if(intPair.item1 == 0)
        {
            if (intPair.item2 == 1)
                return nType.RIGHT;
            else if (intPair.item2 == -1)
                return nType.LEFT;
            else return nType.DIRECTION;
        }
        else
        {
            if (intPair.item1 == 1)
                return nType.DOWN;
            else if (intPair.item1 == -1)
                return nType.UP;
            else return nType.DIRECTION;
        }
    }
}
