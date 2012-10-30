package org.shivas.core.core.fights;

import org.shivas.common.statistics.CharacteristicType;
import org.shivas.core.core.paths.Node;
import org.shivas.core.core.paths.Path;
import org.shivas.core.core.paths.PathNotFoundException;
import org.shivas.core.core.paths.Pathfinder;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 21/10/12
 * Time: 14:14
 */
public class FightPathfinder extends Pathfinder {
    private final Fighter fighter;

    private int points;

    public FightPathfinder(Fighter fighter, short target) {
        super(fighter.getCurrentCell().getId(), target, fighter.getCurrentOrientation(), fighter.getFight().getMap(), false);
        this.fighter = fighter;
    }

    @Override
    public Path find() throws PathNotFoundException {
        Path path = super.find();
        path.add(0, start);

        return path;
    }

    @Override
    protected void onAdded(Node node) {
        super.onAdded(node);

        ++points;
    }

    @Override
    protected boolean mayStop(Node node) {
        return points >= fighter.getStats().get(CharacteristicType.MovementPoints).total() || super.mayStop(node);
    }

    @Override
    protected boolean canAdd(short cellId) {
        FightCell cell = fighter.getFight().getCell(cellId);
        return (cell == null || cell.isAvailable()) && super.canAdd(cellId);
    }
}
