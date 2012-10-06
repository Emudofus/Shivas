package org.shivas.server.core.fights;

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
    public DuelFight(Config config, GameMap map) {
        super(config, map);
    }

    @Override
    public FightTypeEnum getFightType() {
        return FightTypeEnum.DUEL;
    }
}
