package org.shivas.server.core.fights;

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

    protected Fighter(Fight fight, FightTeam team) {
        this.fight = fight;
        this.team = team;
    }

    public abstract Integer getId();

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

    public abstract BaseFighterType toBaseFighterType();
}
