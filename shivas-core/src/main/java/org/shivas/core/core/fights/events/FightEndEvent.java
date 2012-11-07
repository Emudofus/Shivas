package org.shivas.core.core.fights.events;

import org.shivas.core.core.fights.Fight;
import org.shivas.core.core.fights.FightTeam;
import org.shivas.protocol.client.enums.FightStateEnum;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 05/11/12
 * Time: 15:32
 */
public class FightEndEvent extends StateUpdateEvent {
    private final FightTeam winners, losers;

    public FightEndEvent(Fight fight, FightTeam winners, FightTeam losers) {
        super(fight, FightStateEnum.FINISHED);
        this.winners = winners;
        this.losers = losers;
    }

    public FightTeam getWinners() {
        return winners;
    }

    public FightTeam getLosers() {
        return losers;
    }
}
