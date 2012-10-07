package org.shivas.server.core.fights;

import org.shivas.protocol.client.enums.FightTeamEnum;
import org.shivas.protocol.client.enums.FightTypeEnum;
import org.shivas.server.config.Config;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.maps.GameMap;
import org.shivas.server.database.models.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 20:13
 */
public class DuelFight extends Fight {
    private final Player source, target;

    public DuelFight(Config config, GameMap map, Player source, Player target) {
        super(config, map);
        this.source = source;
        this.target = target;
    }

    public Player getSource() {
        return source;
    }

    public Player getTarget() {
        return target;
    }

    @Override
    protected void beforeInit() throws InteractionException {
        super.beforeInit();

        PlayerFighter challenger = new PlayerFighter(this, source),
                      defender   = new PlayerFighter(this, target);

        source.setFighter(challenger);
        target.setFighter(defender);

        teams.put(FightTeamEnum.CHALLENGERS, new FightTeam(FightTeamEnum.CHALLENGERS, this, challenger));
        teams.put(FightTeamEnum.DEFENDERS,   new FightTeam(FightTeamEnum.DEFENDERS,   this, defender));
    }

    @Override
    public FightTypeEnum getFightType() {
        return FightTypeEnum.DUEL;
    }
}
