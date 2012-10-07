package org.shivas.server.core.fights;

import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.protocol.client.types.BaseFighterType;
import org.shivas.protocol.client.types.CharacterFighterType;
import org.shivas.server.database.models.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 07/10/12
 * Time: 11:10
 */
public class PlayerFighter extends Fighter {
    private final Player player;

    private FightCell currentCell;
    private OrientationEnum currentOrientation;
    private boolean dead;

    public PlayerFighter(Fight fight, Player player) {
        super(fight);
        this.player = player;
    }

    @Override
    public Integer getId() {
        return player.getId();
    }

    public FightCell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(FightCell currentCell) {
        this.currentCell = currentCell;
    }

    public OrientationEnum getCurrentOrientation() {
        return currentOrientation;
    }

    public void setCurrentOrientation(OrientationEnum currentOrientation) {
        this.currentOrientation = currentOrientation;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
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
