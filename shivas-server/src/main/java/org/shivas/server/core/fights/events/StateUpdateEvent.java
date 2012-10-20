package org.shivas.server.core.fights.events;

import org.shivas.protocol.client.enums.FightStateEnum;
import org.shivas.server.core.fights.Fight;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 18/10/12
 * Time: 18:28
 */
public class StateUpdateEvent extends FightEvent {
    private final Fight fight;
    private final FightStateEnum newState;

    public StateUpdateEvent(Fight fight, FightStateEnum newState) {
        this.fight = fight;
        this.newState = newState;
    }

    @Override
    public FightEventType getFightEventType() {
        return FightEventType.STATE_UPDATE;
    }

    public Fight getFight() {
        return fight;
    }

    public FightStateEnum getNewState() {
        return newState;
    }
}
