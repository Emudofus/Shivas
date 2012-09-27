package org.shivas.server.core.guilds;

import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventType;
import org.shivas.server.database.models.Guild;

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

    protected final Guild guild;

    protected GuildEvent(Guild guild) {
        this.guild = guild;
    }

    @Override
    public EventType type() {
        return EventType.GUILD;
    }

    public abstract Type getGuildEventType();

    public Guild getGuild() {
        return guild;
    }
}
