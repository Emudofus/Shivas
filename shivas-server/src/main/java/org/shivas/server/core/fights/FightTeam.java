package org.shivas.server.core.fights;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import org.shivas.protocol.client.enums.FightSideEnum;
import org.shivas.protocol.client.enums.FightTeamEnum;
import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.protocol.client.types.BaseFighterType;
import org.shivas.server.utils.Converters;

import java.util.Collection;
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
    private final FightSideEnum side;
    private final Fight fight;
    private final Fighter leader;

    public FightTeam(FightTeamEnum type, FightSideEnum side, Fight fight, Fighter leader) {
        this.type = type;
        this.side = side;
        this.fight = fight;
        this.leader = leader;
        add(leader);
    }

    public FightTeamEnum getType() {
        return type;
    }

    public FightSideEnum getSide() {
        return side;
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

    public boolean areReady() {
        for (Fighter fighter : fighters.values()) {
            if (!fighter.isReady()) {
                return false;
            }
        }
        return true;
    }

    public String getEncodedStartCells() {
        return fight.getMap().getCells().getEncodedStartCells(side);
    }

    public Fighter get(Integer id) {
        return fighters.get(id);
    }

    public void add(Fighter fighter) {
        if (!isAvailable() || fighters.containsKey(fighter.getId())) return;

        fighter.setFight(fight);
        fighter.setTeam(this);
        fighter.setCurrentCell(fight.firstAvailableStartCell(type));
        fighter.setCurrentOrientation(OrientationEnum.SOUTH_WEST);

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

    public Collection<BaseFighterType> toBaseFighterType() {
        return Collections2.transform(fighters.values(), Converters.FIGHTER_TO_BASEFIGHTERTYPE);
    }
}
