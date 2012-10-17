package org.shivas.server.core.fights;

import org.shivas.data.entity.GameCell;
import org.shivas.protocol.client.enums.FightSideEnum;
import org.shivas.protocol.client.enums.FightTeamEnum;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 19:45
 */
public class FightCell {
    private final GameCell cell;
    private final FightTeamEnum startCellTeam;

    private Fighter currentFighter;

    public FightCell(GameCell cell, FightTeamEnum startCellTeam) {
        this.cell = cell;
        this.startCellTeam = startCellTeam;
    }

    public short getId() {
        return cell.getId();
    }

    public FightTeamEnum getStartCellTeam() {
        return startCellTeam;
    }

    public int getGroundLevel() {
        return cell.getGroundLevel();
    }

    public GameCell.MovementType getMovementType() {
        return cell.getMovementType();
    }

    public int getGroundSlope() {
        return cell.getGroundSlope();
    }

    public void setGroundSlope(int groundSlope) {
        cell.setGroundSlope(groundSlope);
    }

    public FightSideEnum getStartFightSide() {
        return cell.getStartFightSide();
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
