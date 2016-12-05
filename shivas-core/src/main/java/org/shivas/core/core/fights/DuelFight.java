package org.shivas.core.core.fights;

import org.shivas.common.threads.Timer;
import org.shivas.core.config.Config;
import org.shivas.core.core.maps.GameMap;
import org.shivas.protocol.client.enums.FightTypeEnum;

import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 20:13
 */
public class DuelFight extends AbstractFight {
    public DuelFight(int id, Config config, Timer<Fight> timer, ExecutorService worker, GameMap map, PlayerFighter challenger, PlayerFighter defender) {
        super(id, config, timer, worker, map, challenger, defender);
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
