package org.shivas.core.core.fights;

import org.shivas.common.threads.Timer;
import org.shivas.protocol.client.enums.FightTypeEnum;
import org.shivas.core.config.Config;
import org.shivas.core.core.maps.GameMap;

import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 20:13
 */
public class DuelFight extends Fight {
    public DuelFight(Config config, Timer<Fight> timer, ExecutorService worker, GameMap map, PlayerFighter challenger, PlayerFighter defender) {
        super(config, timer, worker, map, challenger, defender);
    }

    @Override
    public boolean canQuit(Fighter fighter) {
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
