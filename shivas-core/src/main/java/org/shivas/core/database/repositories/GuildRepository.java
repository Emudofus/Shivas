package org.shivas.core.database.repositories;

import org.atomium.EntityManager;
import org.atomium.repository.BaseEntityRepository;
import org.atomium.repository.EntityRepository;
import org.atomium.repository.impl.AbstractEntityRepository;
import org.atomium.util.pk.IntegerPrimaryKeyGenerator;
import org.atomium.util.query.Op;
import org.atomium.util.query.Query;
import org.atomium.util.query.QueryBuilder;
import org.shivas.data.Container;
import org.shivas.data.entity.Experience;
import org.shivas.protocol.client.enums.GuildRankEnum;
import org.shivas.protocol.client.types.GuildEmblem;
import org.shivas.core.core.experience.GuildExperience;
import org.shivas.core.core.guilds.GuildMemberList;
import org.shivas.core.core.guilds.GuildMemberRights;
import org.shivas.core.database.models.Guild;
import org.shivas.core.database.models.GuildMember;
import org.shivas.core.database.models.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 23/09/12
 * Time: 12:46
 */
@Singleton
public class GuildRepository extends AbstractEntityRepository<Integer, Guild> {
    public static final String TABLE_NAME = "guilds";

    private final QueryBuilder deleteQuery, persistQuery, saveQuery;

    private final BaseEntityRepository<Integer, Player> players;
    private final EntityRepository<Integer, GuildMember> guildMembers;
    private final Container ctner;

    @Inject
    public GuildRepository(EntityManager em, BaseEntityRepository<Integer, Player> players, EntityRepository<Integer, GuildMember> guildMembers, Container ctner) {
        super(em, new IntegerPrimaryKeyGenerator());
        this.players = players;
        this.guildMembers = guildMembers;
        this.ctner = ctner;

        deleteQuery = em.builder().delete(TABLE_NAME).where("id", Op.EQ);
        persistQuery = em.builder()
                .insert(TABLE_NAME)
                .values(
                        "id", "name", "leader_id",
                        "emblem_background_id", "emblem_background_color",
                        "emblem_foreground_id", "emblem_foreground_color",
                        "level", "experience"
                );
        saveQuery = em.builder()
                .update(TABLE_NAME)
                .value("name").value("leader_id")
                .value("emblem_background_id").value("emblem_background_color")
                .value("emblem_foreground_id").value("emblem_foreground_color")
                .value("level").value("experience")
                .where("id", Op.EQ);
    }

    public Guild createDefault(String name, Player leader, GuildEmblem emblem) {
        Guild guild = new Guild();
        guild.setName(name);
        guild.setLeader(leader);
        guild.setEmblem(emblem);
        guild.setExperience(new GuildExperience(ctner.get(Experience.class).byId(1)));
        guild.setMembers(new GuildMemberList(guild, guildMembers));

        GuildMember leaderMember = new GuildMember(guild, leader, GuildRankEnum.LEADER, new GuildMemberRights().fill(true));
        leader.setGuildMember(leaderMember);
        guild.getMembers().add(leaderMember);

        persistLater(guild);
        guildMembers.persistLater(leaderMember);

        return guild;
    }

    public boolean exists(String guildName) {
        for (Guild guild : entities.values()) {
            if (guild.getName().equals(guildName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected Query buildDeleteQuery(Guild guild) {
        return deleteQuery.toQuery().setParameter("id", guild.getId());
    }

    protected Query bindValues(QueryBuilder q, Guild guild) {
        return q.toQuery()
                .setParameter("id", guild.getId())
                .setParameter("name", guild.getName())
                .setParameter("leader_id", guild.getLeader().getId())
                .setParameter("emblem_background_id", guild.getEmblem().getBackgroundId())
                .setParameter("emblem_background_color", guild.getEmblem().getBackgroundColor())
                .setParameter("emblem_foreground_id", guild.getEmblem().getForegroundId())
                .setParameter("emblem_foreground_color", guild.getEmblem().getForegroundColor())
                .setParameter("level", guild.getExperience().level())
                .setParameter("experience", guild.getExperience().current());
    }

    @Override
    protected Query buildPersistQuery(Guild guild) {
        return bindValues(persistQuery, guild);
    }

    @Override
    protected Query buildSaveQuery(Guild guild) {
        return bindValues(saveQuery, guild);
    }

    @Override
    protected Query buildLoadQuery() {
        return em.builder().select(TABLE_NAME).toQuery();
    }

    @Override
    protected Guild load(ResultSet rset) throws SQLException {
        Guild guild = new Guild();
        guild.setId(rset.getInt("id"));
        guild.setName(rset.getString("name"));
        guild.setLeader(players.find(rset.getInt("leader_id")));
        guild.setEmblem(new GuildEmblem(
                rset.getInt("emblem_background_id"),
                rset.getInt("emblem_background_color"),
                rset.getInt("emblem_foreground_id"),
                rset.getInt("emblem_foreground_color")
        ));
        guild.setExperience(new GuildExperience(
                ctner.get(Experience.class).byId(rset.getInt("level")),
                rset.getLong("experience")
        ));
        guild.setMembers(new GuildMemberList(guild, guildMembers));

        return guild;
    }

    @Override
    protected void unhandledException(Exception e) {
    }
}
