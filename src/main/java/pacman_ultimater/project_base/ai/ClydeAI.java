package pacman_ultimater.project_base.ai;

import pacman_ultimater.project_base.core.Direction;
import pacman_ultimater.project_base.core.Tile;
import pacman_ultimater.project_base.custom_utils.IntPair;

public class ClydeAI extends DefaultAI
{
    /**
     * @param state Entity's state such as NoAI, Hostile....
     * @param ghostId int ghost identifier
     * @param map Tile[][]
     */
    public ClydeAI(nType state, int ghostId, Tile[][] map)
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
        System.out.println("CLYDE HOSTILE");
        return super.HostileAttack(position, target, direction, map);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final Direction.directionType CanBeEaten(IntPair position, IntPair target,
           Direction.directionType direction, Tile[][] map)
    {
        System.out.println("CLYDE CANBEEATEN");
        return super.CanBeEaten(position, target, direction, map);
    }
}
