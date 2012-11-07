package org.shivas.core.core.fights;

import org.shivas.core.core.Look;
import org.shivas.core.core.interactions.InteractionException;
import org.shivas.core.core.statistics.PlayerStatistics;
import org.shivas.core.database.models.Player;
import org.shivas.protocol.client.types.BaseEndFighterType;
import org.shivas.protocol.client.types.BaseFighterType;
import org.shivas.protocol.client.types.BaseRolePlayActorType;
import org.shivas.protocol.client.types.CharacterFighterType;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 07/10/12
 * Time: 11:10
 */
public class PlayerFighter extends Fighter {
    private final Player player;
    private final PlayerStatistics stats;

    private boolean ready;

    public PlayerFighter(Player player) {
        this.player = player;
        this.stats = player.getStats().copy();
    }

    @Override
    public Integer getId() {
        return player.getId();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public Look getLook() {
        return player.getLook();
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public PlayerStatistics getStats() {
        return stats;
    }

    @Override
    public short getLevel() {
        return player.getExperience().level();
    }

    public void setReady() throws InteractionException {
        ready = !ready;
        fight.notifyReady(this);
    }

    @Override
    public BaseFighterType toBaseFighterType() {
        return new CharacterFighterType(
                player.getPublicId(),
                player.getName(),
                (byte) player.getBreed().getId(),
                player.getLook().skin(),
                player.getLook().size(),
                player.getExperience().level(),
                currentCell.getId(),
                currentOrientation,
                !isAlive(),
                stats,
                player.getGender(),
                (short) 0, (short) 0, false, // TODO alignment
                player.getLook().colors().first(),
                player.getLook().colors().second(),
                player.getLook().colors().third(),
                player.getLook().accessories(),
                team.getType()
        );
    }

    @Override
    public BaseEndFighterType toBaseEndFighterType() {
        return new BaseEndFighterType(
                player.getId(),
                player.getName(),
                player.getExperience().level(),
                stats.life().current(),
                isAlive(),
                0, 0, 0, 0, 0, 0, 0 // TODO
        );
    }

    @Override
    public BaseRolePlayActorType toBaseRolePlayActorType() {
        return player.toBaseRolePlayActorType();
    }
}
