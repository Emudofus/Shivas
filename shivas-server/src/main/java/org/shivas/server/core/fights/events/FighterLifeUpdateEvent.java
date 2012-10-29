package org.shivas.server.core.fights.events;

import org.shivas.protocol.client.enums.ActionTypeEnum;
import org.shivas.server.core.fights.Fighter;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 21:33
 */
public class FighterLifeUpdateEvent extends FighterActionEvent {
    private final Fighter target;
    private final int delta;

    public FighterLifeUpdateEvent(Fighter fighter, Fighter target, int delta) {
        super(fighter, ActionTypeEnum.LIFE_CHANGEMENT);
        this.target = target;
        this.delta = delta;
    }

    public Fighter getTarget() {
        return target;
    }

    public int getDelta() {
        return delta;
    }
}
