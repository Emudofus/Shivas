package org.shivas.server.core.guilds;

import org.shivas.server.database.models.Guild;
import org.shivas.server.database.models.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 26/09/12
 * Time: 15:55
 */
public final class MemberGuildEvent extends GuildEvent {
    private final Player player, source;
    private final Type type;

    public MemberGuildEvent(Guild guild, Player player, Player source, Type type) {
        super(guild);
        this.player = player;
        this.source = source;
        this.type = type;
    }

    @Override
    public Type getGuildEventType() {
        return type;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getSource() {
        return source;
    }
}
