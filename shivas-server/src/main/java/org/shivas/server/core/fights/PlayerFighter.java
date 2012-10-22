package org.shivas.server.core.fights;

import org.shivas.protocol.client.types.BaseFighterType;
import org.shivas.protocol.client.types.CharacterFighterType;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.statistics.PlayerStatistics;
import org.shivas.server.database.models.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 07/10/12
 * Time: 11:10
 */
public class PlayerFighter extends Fighter {
    private final Player player;

    private boolean ready;

    public PlayerFighter(Fight fight, Player player) {
        super(fight);
        this.player = player;
    }

    @Override
    public Integer getId() {
        return player.getId();
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public PlayerStatistics getStats() {
        return player.getStats();
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
                dead,
                player.getStats(),
                player.getGender(),
                (short) 0, (short) 0, false, // TODO alignment
                player.getLook().colors().first(),
                player.getLook().colors().second(),
                player.getLook().colors().third(),
                player.getLook().accessories(),
                team.getType()
        );
    }
}
