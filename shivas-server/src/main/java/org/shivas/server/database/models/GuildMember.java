package org.shivas.server.database.models;

import org.atomium.PersistableEntity;
import org.shivas.protocol.client.enums.GuildRankEnum;
import org.shivas.protocol.client.types.BaseGuildMemberType;
import org.shivas.server.core.guilds.GuildMemberRights;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 23/09/12
 * Time: 11:56
 */
public class GuildMember implements PersistableEntity<Integer> {
    private Guild guild;
    private Player player;
    private GuildRankEnum rank;
    private GuildMemberRights rights;
    private byte experienceRate;
    private long experienceGiven;

    public GuildMember() {}

    public GuildMember(Guild guild, Player player, GuildRankEnum rank, GuildMemberRights rights) {
        this.guild = guild;
        this.player = player;
        this.rank = rank;
        this.rights = rights;
    }

    @Override
    public Integer getId() {
        return player.getId();
    }

    @Override
    public void setId(Integer id) { }

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

    public GuildRankEnum getRank() {
        return rank;
    }

    public void setRank(GuildRankEnum rank) {
        this.rank = rank;
    }

    public GuildMemberRights getRights() {
        return rights;
    }

    public void setRights(GuildMemberRights rights) {
        this.rights = rights;
    }

    public byte getExperienceRate() {
        return experienceRate;
    }

    public void setExperienceRate(byte experienceRate) {
        this.experienceRate = experienceRate;
    }

    public long getExperienceGiven() {
        return experienceGiven;
    }

    public void setExperienceGiven(long experienceGiven) {
        this.experienceGiven = experienceGiven;
    }

    public GuildMember plusExperienceGiven(long experienceGiven) {
        this.experienceGiven += experienceGiven;
        return this;
    }

    public BaseGuildMemberType toBaseGuildMemberType() {
        return new BaseGuildMemberType(
                player.getId(),
                player.getName(),
                player.getExperience().level(),
                player.getLook().skin(),
                rank,
                experienceGiven,
                experienceRate,
                rights.toInt(),
                player.getClient() != null,
                0, // TODO alignment
                player.getOwner().getLastConnection()
        );
    }
}
