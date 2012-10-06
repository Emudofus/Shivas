package org.shivas.server.core.fights;

import org.shivas.data.entity.GameCell;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 19:45
 */
public class FightCell {
    private final GameCell cell;

    private Fighter currentFighter;

    public FightCell(GameCell cell) {
        this.cell = cell;
    }

    public short getId() {
        return cell.getId();
    }

    public int getGroundLevel() {
        return cell.getGroundLevel();
    }

    public boolean isLineOfSight() {
        return cell.isLineOfSight();
    }

    public void setLineOfSight(boolean lineOfSight) {
        cell.setLineOfSight(lineOfSight);
    }

    public void setMovementType(GameCell.MovementType movementType) {
        cell.setMovementType(movementType);
    }

    public GameCell.MovementType getMovementType() {
        return cell.getMovementType();
    }

    public int getGroundSlope() {
        return cell.getGroundSlope();
    }

    public void setId(short id) {
        cell.setId(id);
    }

    public void setGroundSlope(int groundSlope) {
        cell.setGroundSlope(groundSlope);
    }

    public void setGroundLevel(int groundLevel) {
        cell.setGroundLevel(groundLevel);
    }

    public Fighter getCurrentFighter() {
        return currentFighter;
    }

    public void setCurrentFighter(Fighter currentFighter) {
        this.currentFighter = currentFighter;
    }
}
