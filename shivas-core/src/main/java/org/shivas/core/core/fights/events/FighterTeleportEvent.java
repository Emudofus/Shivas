package org.shivas.core.core.fights.events;

import org.shivas.core.core.fights.FightCell;
import org.shivas.core.core.fights.Fighter;
import org.shivas.protocol.client.enums.ActionTypeEnum;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 31/10/12
 * Time: 14:07
 */
public class FighterTeleportEvent extends FighterActionEvent {
    private final FightCell target;

    public FighterTeleportEvent(Fighter fighter, FightCell target) {
        super(fighter, ActionTypeEnum.CELL_CHANGEMENT);
        this.target = target;
    }

    public FightCell getTarget() {
        return target;
    }
}
