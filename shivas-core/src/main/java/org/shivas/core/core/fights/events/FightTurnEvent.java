package org.shivas.core.core.fights.events;

import org.shivas.core.core.fights.Fight;
import org.shivas.core.core.fights.FightTurn;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 20/10/12
 * Time: 17:20
 */
public class FightTurnEvent extends FightEvent {

    public enum Type { START, STOP }

    private final Type fightTurnEventType;
    private final Fight fight;
    private final FightTurn turn;

    public FightTurnEvent(Type fightTurnEventType, Fight fight, FightTurn turn) {
        this.fight = fight;
        this.fightTurnEventType = fightTurnEventType;
        this.turn = turn;
    }

    @Override
    public FightEventType getFightEventType() {
        return FightEventType.TURN;
    }

    public Type getFightTurnEventType() {
        return fightTurnEventType;
    }

    public Fight getFight() {
        return fight;
    }

    public FightTurn getTurn() {
        return turn;
    }
}
