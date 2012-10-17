package org.shivas.server.core.fights;

import org.shivas.protocol.client.enums.FightSideEnum;
import org.shivas.protocol.client.enums.FightTeamEnum;
import org.shivas.protocol.client.enums.FightTypeEnum;
import org.shivas.server.config.Config;
import org.shivas.server.core.maps.GameMap;
import org.shivas.server.database.models.Player;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 20:13
 */
public class DuelFight extends Fight {
    public DuelFight(Config config, GameMap map, Player source, Player target) {
        super(config, map);

        PlayerFighter challenger = new PlayerFighter(this, source),
                      defender   = new PlayerFighter(this, target);

        source.setFighter(challenger);
        target.setFighter(defender);

        Random random = new Random(System.nanoTime());
        FightSideEnum first  = random.nextBoolean()        ? FightSideEnum.BLUE : FightSideEnum.RED,
                      second = first == FightSideEnum.BLUE ? FightSideEnum.RED  : FightSideEnum.BLUE;

        teams.put(FightTeamEnum.CHALLENGERS, new FightTeam(FightTeamEnum.CHALLENGERS, first, this, challenger));
        teams.put(FightTeamEnum.DEFENDERS,   new FightTeam(FightTeamEnum.DEFENDERS, second, this, defender));
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
