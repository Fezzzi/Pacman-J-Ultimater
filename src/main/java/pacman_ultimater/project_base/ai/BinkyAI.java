package pacman_ultimater.project_base.ai;

import pacman_ultimater.project_base.core.Direction;
import pacman_ultimater.project_base.core.Tile;
import pacman_ultimater.project_base.custom_utils.IntPair;

public class BinkyAI extends DefaultAI
{
    /**
     * @param state Entity's state such as NoAI, Hostile....
     * @param ghostId int ghost identifier
     * @param connectedTiles Boolean [][]
     */
    public BinkyAI(nType state, int ghostId, Boolean [][] connectedTiles)
    {
        super(state, ghostId, connectedTiles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final Direction.directionType HostileAttack(IntPair position, IntPair target,
          Direction.directionType direction, Direction.directionType pacmanDirection, Tile[][] map)
    {
        return super.bfsAI(position, target, direction, map, 0.95f);
    }
}
