package org.shivas.core.core.guilds;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import org.atomium.repository.EntityRepository;
import org.shivas.protocol.client.enums.GuildRankEnum;
import org.shivas.protocol.client.types.BaseGuildMemberType;
import org.shivas.core.database.models.Guild;
import org.shivas.core.database.models.GuildMember;
import org.shivas.core.database.models.Player;
import org.shivas.core.utils.Converters;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 23/09/12
 * Time: 12:21
 */
public class GuildMemberList implements Iterable<GuildMember> {
    private final Guild owner;
    private final EntityRepository<Integer, GuildMember> repo;
    private final Map<Integer, GuildMember> members = Maps.newHashMap();

    public GuildMemberList(Guild owner, EntityRepository<Integer, GuildMember> repo) {
        this.owner = owner;
        this.repo = repo;
    }

    public int count() {
        return members.size();
    }

    public boolean isEmpty() {
        return count() <= 0;
    }

    public GuildMember get(int playerId) {
        return members.get(playerId);
    }

    public GuildMember get(String playerName) {
        for (GuildMember member : members.values()) {
            if (member.getPlayer().getName().equals(playerName)) return member;
        }
        return null;
    }

    public void add(Player player, Player source) {
        GuildMember member = new GuildMember(owner, player, GuildRankEnum.TESTING, new GuildMemberRights());
        player.setGuildMember(member);
        repo.persistLater(member);

        add(member);

        owner.publish(new MemberGuildEvent(owner, member.getPlayer(), source, GuildEvent.Type.ADD_MEMBER));
    }

    public void add(GuildMember member) {
        members.put(member.getPlayer().getId(), member);
    }

    public boolean remove(int playerId, Player source) {
        GuildMember member = members.remove(playerId);
        if (member != null) {
            member.getPlayer().setGuildMember(null);
            repo.deleteLater(member);

            owner.publish(new MemberGuildEvent(owner, member.getPlayer(), source, GuildEvent.Type.REMOVE_MEMBER));

            return true;
        }
        return false;
    }

    public void remove(GuildMember member, Player source) {
        remove(member.getId(), source);
    }

    public Collection<BaseGuildMemberType> toBaseGuildMemberType() {
        return Collections2.transform(members.values(), Converters.GUILDMEMBER_TO_BASEGUILDMEMBERTYPE);
    }

    @Override
    public Iterator<GuildMember> iterator() {
        return members.values().iterator();
    }
}
