package pacman_ultimater.project_base.ai;

import pacman_ultimater.project_base.core.Direction;
import pacman_ultimater.project_base.core.Tile;
import pacman_ultimater.project_base.custom_utils.IntPair;

import java.util.Random;

public class InkyAI extends DefaultAI
{
    private AIHelper helper;

    /**
     * @param state Entity's state such as NoAI, Hostile....
     * @param ghostId int ghost identifier
     * @param connectedTiles Boolean [][]
     */
    public InkyAI(nType state, int ghostId, Boolean [][] connectedTiles)
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
        float prob = (new Random()).nextFloat();
        if (prob < 0.33) {
            return super.bfsAI(position, target, direction, map, 0.90f);
        } else if (prob < 0.66) {
            IntPair newTarget = helper.findPacmansCrossRoad(target, pacmanDirection);
            return super.bfsAI(position, newTarget, direction, map, 0.90f);
        } else {
            if ((position.item1 - target.item1) + (position.item2 - target.item2) > 10) {
                return bfsAI(position, target, direction, map, 0.5f);
            } else {
                return CanBeEaten(position, target, direction, map);
            }
        }
    }
}
