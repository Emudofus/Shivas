package org.shivas.server.core.fights.events;

import org.shivas.protocol.client.enums.ActionTypeEnum;
import org.shivas.server.core.fights.Fighter;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 21/10/12
 * Time: 15:03
 */
public class FighterActionEvent extends FighterEvent {
    private final ActionTypeEnum actionType;

    public FighterActionEvent(Fighter fighter, ActionTypeEnum actionType) {
        super(FightEventType.FIGHTER_ACTION, fighter);
        this.actionType = actionType;
    }

    public ActionTypeEnum getActionType() {
        return actionType;
    }
}
