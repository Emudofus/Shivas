package org.shivas.server.core.fights;

import org.shivas.common.threads.Timer;
import org.shivas.protocol.client.enums.FightTypeEnum;
import org.shivas.server.config.Config;
import org.shivas.server.core.maps.GameMap;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 20:13
 */
public class DuelFight extends Fight {
    public DuelFight(Config config, Timer<Fight> timer, GameMap map, PlayerFighter challenger, PlayerFighter defender) {
        super(config, timer, map, challenger, defender);
    }

    @Override
    public boolean canCancel() {
        return true;
    }

    @Override
    public int getRemainingPreparation() {
        return -1;
    }

    @Override
    public FightTypeEnum getFightType() {
        return FightTypeEnum.DUEL;
    }
}
