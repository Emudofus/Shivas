package org.shivas.server.core.fights.events;

import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventType;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 07/10/12
 * Time: 11:56
 */
public abstract class FightEvent implements Event {
    @Override
    public EventType type() {
        return EventType.FIGHT;
    }

    public abstract FightEventType getFightEventType();
}
