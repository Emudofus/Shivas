package org.shivas.server.core.fights;

import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.protocol.client.types.BaseFighterType;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 06/10/12
 * Time: 19:43
 */
public abstract class Fighter {
    protected FightAction currentAction;
    protected final Fight fight;

    protected FightTeam team;
    protected FightCell currentCell;
    protected OrientationEnum currentOrientation;
    protected boolean dead;

    protected Fighter(Fight fight) {
        this.fight = fight;
    }

    public abstract Integer getId();
    public abstract boolean isReady();

    public FightAction getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(FightAction currentAction) {
        this.currentAction = currentAction;
    }

    public Fight getFight() {
        return fight;
    }

    public FightTeam getTeam() {
        return team;
    }

    public void setTeam(FightTeam team) {
        this.team = team;
    }

    public FightCell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(FightCell currentCell) {
        if (this.currentCell != null) {
            this.currentCell.setCurrentFighter(null);
        }
        this.currentCell = currentCell;
        currentCell.setCurrentFighter(this);
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

    public abstract BaseFighterType toBaseFighterType();
}
