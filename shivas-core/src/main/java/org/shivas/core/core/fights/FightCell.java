package org.shivas.core.core.fights;

import org.shivas.data.entity.GameCell;
import org.shivas.protocol.client.enums.FightSideEnum;
import org.shivas.protocol.client.enums.FightTeamEnum;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 19:45
 */
public class FightCell extends GameCell {
    private static final long serialVersionUID = -5402560800514478590L;

    private final FightTeamEnum startCellTeam;

    private Fighter currentFighter;

    public FightCell(short id, boolean lineOfSight, MovementType movementType, int groundLevel, int groundSlope, FightSideEnum startFightSide, FightTeamEnum startCellTeam) {
        super(id, lineOfSight, movementType, groundLevel, groundSlope, startFightSide);
        this.startCellTeam = startCellTeam;
    }

    public static FightCell copyFrom(GameCell cell, FightTeamEnum startCellTeam) {
        return new FightCell(
                cell.getId(),
                cell.isLineOfSight(),
                cell.getMovementType(),
                cell.getGroundLevel(),
                cell.getGroundSlope(),
                cell.getStartFightSide(),
                startCellTeam);
    }

    public FightTeamEnum getStartCellTeam() {
        return startCellTeam;
    }

    public Fighter getCurrentFighter() {
        return currentFighter;
    }

    public void setCurrentFighter(Fighter currentFighter) {
        this.currentFighter = currentFighter;
    }

    public boolean isAvailable() {
        return currentFighter == null;
    }
}
