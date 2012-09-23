package org.shivas.server.database.models;

import org.atomium.PersistableEntity;
import org.shivas.protocol.client.types.BaseGuildMemberType;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 23/09/12
 * Time: 11:56
 */
public class GuildMember implements PersistableEntity<Long> {
    private Long id;
    private Guild guild;
    private Player player;

    public GuildMember() {}

    public GuildMember(Guild guild, Player player) {
        this.guild = guild;
        this.player = player;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public BaseGuildMemberType toBaseGuildMemberType() {
        return new BaseGuildMemberType( // TODO guilds
                player.getId(),
                player.getName(),
                player.getExperience().level(),
                player.getLook().skin(),
                0, // rank
                (byte) 0, // experience given
                0, // experience rate
                0, // rights
                player.getClient() != null,
                0, // TODO alignment
                player.getOwner().getLastConnection()
        );
    }
}
