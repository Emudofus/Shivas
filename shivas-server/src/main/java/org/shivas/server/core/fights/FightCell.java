package org.shivas.server.core.fights;

import org.shivas.data.entity.GameCell;
import org.shivas.protocol.client.enums.FightTeamEnum;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 19:45
 */
public class FightCell extends GameCell {
    private final FightTeamEnum startCellTeam;

    private Fighter currentFighter;

    public FightCell(GameCell cell, FightTeamEnum startCellTeam) {
        this.startCellTeam = startCellTeam;

        setId(cell.getId());
        setLineOfSight(cell.isLineOfSight());
        setGroundLevel(cell.getGroundLevel());
        setMovementType(cell.getMovementType());
        setGroundSlope(cell.getGroundSlope());
        setStartFightSide(cell.getStartFightSide());
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
