package org.shivas.server.core.fights.frames;

import org.shivas.common.statistics.CharacteristicType;
import org.shivas.server.core.fights.FightException;
import org.shivas.server.core.fights.Fighter;
import org.shivas.server.core.fights.events.FighterMovementEndEvent;
import org.shivas.server.core.fights.events.FighterMovementEvent;
import org.shivas.server.core.paths.Node;
import org.shivas.server.core.paths.Path;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 22/10/12
 * Time: 19:42
 */
public class FighterMovementFrame extends Frame {
    private final Fighter fighter;
    private final Path path;

    public FighterMovementFrame(Fighter fighter, Path path) {
        super(fighter.getFight(), fighter.getTurn());
        this.fighter = fighter;
        this.path = path;
    }

    @Override
    protected void doBegin() throws FightException {
        if (!fighter.getTurn().isCurrent()) throw new FightException("it is not your turn");

        fight.getEvent().publish(new FighterMovementEvent(fighter, path));

        scheduleEnd(path.estimateTimeOn(fight.getMap()));
    }

    @Override
    protected void doEnd() {
        // set new position
        Node lastNode = path.last();
        fighter.setCurrentCell(fight.getCell(lastNode.cell()));
        fighter.setCurrentOrientation(lastNode.orientation());

        // remove movement points
        int usedMovementPoints = path.size() - 1; // ignores fighter's current cell
        fighter.getStats().get(CharacteristicType.MovementPoints).minusContext((short) usedMovementPoints);

        // warn other fighters
        fight.getEvent().publish(new FighterMovementEndEvent(fighter, usedMovementPoints));
    }
}
