package org.shivas.server.core.guilds;

import org.shivas.server.database.models.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 26/09/12
 * Time: 15:55
 */
public final class MemberGuildEvent extends GuildEvent {
    private final Player player;
    private final Type type;

    public MemberGuildEvent(Player player, Type type) {
        this.player = player;
        this.type = type;
    }

    @Override
    public Type getGuildEventType() {
        return type;
    }

    public Player getPlayer() {
        return player;
    }
}
