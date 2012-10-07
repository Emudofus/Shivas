package org.shivas.server.core.fights;

import com.google.common.collect.Maps;
import org.shivas.protocol.client.enums.FightTeamEnum;

import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 22:20
 */
public class FightTeam implements Iterable<Fighter> {
    private static final int MAX_FIGHTERS = 8;

    private final Map<Integer, Fighter> fighters = Maps.newHashMap();
    private final FightTeamEnum type;
    private final Fight fight;
    private final Fighter leader;

    public FightTeam(FightTeamEnum type, Fight fight, Fighter leader) {
        this.type = type;
        this.fight = fight;
        this.leader = leader;
        this.leader.setTeam(this);
        this.fighters.put(leader.getId(), leader);
    }

    public FightTeamEnum getType() {
        return type;
    }

    public Fight getFight() {
        return fight;
    }

    public Fighter getLeader() {
        return leader;
    }

    public int count() {
        return fighters.size();
    }

    public boolean isAvailable() {
        return count() < MAX_FIGHTERS;
    }

    public Fighter get(Integer id) {
        return fighters.get(id);
    }

    public void add(Fighter fighter) {
        if (!isAvailable() || fighters.containsKey(fighter.getId())) return;

        fighter.setTeam(this);

        fighters.put(fighter.getId(), fighter);
    }

    public Fighter remove(Integer id) {
        if (leader.getId().equals(id)) {
            throw new RuntimeException("you can't remove the leader from his team");
        }

        Fighter fighter = fighters.remove(id);
        if (fighter != null) {
            fighter.setTeam(null);

            return fighter;
        }
        return null;
    }

    public boolean remove(Fighter fighter) {
        return remove(fighter.getId()) != null;
    }

    @Override
    public Iterator<Fighter> iterator() {
        return fighters.values().iterator();
    }
}
