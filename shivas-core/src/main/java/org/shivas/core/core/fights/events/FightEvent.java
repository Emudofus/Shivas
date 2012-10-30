package org.shivas.core.core.fights.events;

import org.shivas.core.core.events.Event;
import org.shivas.core.core.events.EventType;

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
