package pacman_ultimater.project_base.ai;

import pacman_ultimater.project_base.core.Direction;
import pacman_ultimater.project_base.core.Tile;
import pacman_ultimater.project_base.custom_utils.IntPair;

/**
 * The Pink ghost AI
 */
public class PinkyAI extends DefaultAI
{
    private final AIHelper helper;

    /**
     * @param state Entity's state such as NoAI, Hostile....
     * @param ghostId int ghost identifier
     * @param connectedTiles Boolean [][]
     */
    public PinkyAI(nType state, int ghostId, Boolean [][] connectedTiles)
    {
        super(state, ghostId, connectedTiles);
        helper = new AIHelper(connectedTiles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final Direction.directionType HostileAttack(IntPair position, IntPair target,
          Direction.directionType direction, Direction.directionType pacmanDirection, Tile[][] map)
    {
        IntPair newTarget = helper.findPacmansCrossRoad(target, pacmanDirection);
        return super.bfsAI(position, newTarget, direction, map, 0.95f);
    }
}
