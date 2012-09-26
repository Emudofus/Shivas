package org.shivas.server.core.guilds;

import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventType;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 26/09/12
 * Time: 15:54
 */
public abstract class GuildEvent implements Event {
    public static enum Type {
        ADD_MEMBER,
        REMOVE_MEMBER,
    }

    @Override
    public EventType type() {
        return EventType.GUILD;
    }

    public abstract Type getGuildEventType();
}
