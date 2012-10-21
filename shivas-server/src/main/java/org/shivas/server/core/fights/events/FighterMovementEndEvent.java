package org.shivas.server.core.fights.events;

import org.shivas.server.core.fights.Fighter;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 21/10/12
 * Time: 15:10
 */
public class FighterMovementEndEvent extends FighterEvent {
    private final int usedMovementPoints;

    public FighterMovementEndEvent(Fighter fighter, int usedMovementPoints) {
        super(FightEventType.FIGHTER_MOVEMENT_END, fighter);
        this.usedMovementPoints = usedMovementPoints;
    }

    public int getUsedMovementPoints() {
        return usedMovementPoints;
    }
}
