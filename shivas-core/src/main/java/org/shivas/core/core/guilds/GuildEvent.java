package org.shivas.core.core.guilds;

import org.shivas.core.core.events.Event;
import org.shivas.core.core.events.EventType;
import org.shivas.core.database.models.Guild;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 26/09/12
 * Time: 15:54
 */
public abstract class GuildEvent implements Event {
    public enum Type {
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
