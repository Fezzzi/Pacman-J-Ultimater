package pacman_ultimater.project_base.ai;

import pacman_ultimater.project_base.core.Direction;
import pacman_ultimater.project_base.core.Tile;
import pacman_ultimater.project_base.custom_utils.IntPair;

/**
 * The orange ghost AI
 */
public class ClydeAI extends DefaultAI
{
    /**
     * @param state Entity's state such as NoAI, Hostile....
     * @param ghostId int ghost identifier
     * @param connectedTiles Boolean [][]
     */
    public ClydeAI(nType state, int ghostId, Boolean [][] connectedTiles)
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
        if ((position.item1 - target.item1) + (position.item2 - target.item2) > 10) {
            return bfsAI(position, target, direction, map, 0.8f);
        } else {
            return CanBeEaten(position, target, direction, map);
        }
    }
}
