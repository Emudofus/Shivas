package org.shivas.core.core.fights.events;

import org.shivas.core.core.fights.Fight;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 07/10/12
 * Time: 12:03
 */
public final class FightInitializationEvent extends FightEvent {
    private final Fight fight;

    public FightInitializationEvent(Fight fight) {
        this.fight = fight;
    }

    @Override
    public FightEventType getFightEventType() {
        return FightEventType.INITIALIZATION;
    }

    public Fight getFight() {
        return fight;
    }
}
