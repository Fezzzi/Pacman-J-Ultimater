package pacman_ultimater.project_base.ai;

import pacman_ultimater.project_base.core.Direction;
import pacman_ultimater.project_base.core.Tile;
import pacman_ultimater.project_base.custom_utils.IntPair;

public class InkyAI extends DefaultAI
{
    /**
     * @param state Entity's state such as NoAI, Hostile....
     * @param ghostId int ghost identifier
     * @param map Tile[][]
     */
    public InkyAI(nType state, int ghostId, Tile[][] map)
    {
        super(state, ghostId, map);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final Direction.directionType HostileAttack(IntPair position, IntPair target,
                                                          Direction.directionType direction, Tile[][] map)
    {
        System.out.println("INKY HOSTILE");
        return super.HostileAttack(position, target, direction, map);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final Direction.directionType CanBeEaten(IntPair position, IntPair target,
           Direction.directionType direction, Tile[][] map)
    {
        System.out.println("INKY CANBEEATEN");
        return super.CanBeEaten(position, target, direction, map);
    }
}
