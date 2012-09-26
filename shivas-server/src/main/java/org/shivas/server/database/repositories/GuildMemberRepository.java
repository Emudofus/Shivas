package org.shivas.server.database.repositories;

import org.atomium.EntityManager;
import org.atomium.repository.BaseEntityRepository;
import org.atomium.repository.impl.AbstractEntityRepository;
import org.atomium.util.pk.EmptyPrimaryKeyGenerator;
import org.atomium.util.query.Op;
import org.atomium.util.query.Query;
import org.atomium.util.query.QueryBuilder;
import org.shivas.protocol.client.enums.GuildRankEnum;
import org.shivas.server.core.guilds.GuildMemberRights;
import org.shivas.server.database.models.Guild;
import org.shivas.server.database.models.GuildMember;
import org.shivas.server.database.models.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 23/09/12
 * Time: 13:57
 */
@Singleton
public class GuildMemberRepository extends AbstractEntityRepository<Integer, GuildMember> {
    public static final String TABLE_NAME = "guild_members";

    private final BaseEntityRepository<Integer, Guild> guilds;
    private final BaseEntityRepository<Integer, Player> players;

    private final QueryBuilder deleteQuery, persistQuery, saveQuery;

    @Inject
    public GuildMemberRepository(EntityManager em, BaseEntityRepository<Integer, Guild> guilds, BaseEntityRepository<Integer, Player> players) {
        super(em, new EmptyPrimaryKeyGenerator<Integer>());
        this.guilds = guilds;
        this.players = players;

        deleteQuery = em.builder().delete(TABLE_NAME).where("id", Op.EQ);
        persistQuery = em.builder()
                .insert(TABLE_NAME)
                .values("id", "guild_id", "rank", "rights", "experience_rate", "experience_given");
        saveQuery = em.builder()
                .update(TABLE_NAME)
                .value("guild_id").value("rank").value("rights")
                .value("experience_rate").value("experience_given")
                .where("id", Op.EQ);
    }

    @Override
    protected Query buildDeleteQuery(GuildMember guildMember) {
        return deleteQuery.toQuery().setParameter("id", guildMember.getId());
    }

    private Query bindValues(QueryBuilder q, GuildMember guildMember) {
        return q.toQuery()
                .setParameter("id", guildMember.getId())
                .setParameter("guild_id", guildMember.getGuild().getId())
                .setParameter("rank", guildMember.getRank().value())
                .setParameter("rights", guildMember.getRights().toInt())
                .setParameter("experience_rate", guildMember.getExperienceRate())
                .setParameter("experience_given", guildMember.getExperienceGiven());
    }

    @Override
    protected Query buildPersistQuery(GuildMember guildMember) {
        return bindValues(persistQuery, guildMember);
    }

    @Override
    protected Query buildSaveQuery(GuildMember guildMember) {
        return bindValues(saveQuery, guildMember);
    }

    @Override
    protected Query buildLoadQuery() {
        return em.builder().select(TABLE_NAME).toQuery();
    }

    @Override
    protected GuildMember load(ResultSet rset) throws SQLException {
        GuildMember member = new GuildMember();
        member.setPlayer(players.find(rset.getInt("id")));
        member.setGuild(guilds.find(rset.getInt("guild_id")));
        member.setRank(GuildRankEnum.valueOf(rset.getInt("rank")));
        member.setRights(new GuildMemberRights(rset.getInt("rights")));
        member.setExperienceRate(rset.getByte("experience_rate"));
        member.setExperienceGiven(rset.getLong("experience_given"));

        member.getPlayer().setGuildMember(member);
        member.getGuild().getMembers().add(member);

        return member;
    }

    @Override
    protected void unhandledException(Exception e) {
    }
}
