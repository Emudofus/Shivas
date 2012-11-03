package org.shivas.core.core.fights.events;

import org.shivas.core.core.fights.FightCell;
import org.shivas.core.core.fights.Fighter;
import org.shivas.protocol.client.enums.ActionTypeEnum;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 02/11/12
 * Time: 12:55
 */
public class FighterSlideEvent extends FighterActionEvent {
    private final Fighter target;
    private final FightCell newCell;

    public FighterSlideEvent(Fighter caster, Fighter target, FightCell newCell) {
        super(caster, ActionTypeEnum.CELL_SLIDE);
        this.target = target;
        this.newCell = newCell;
    }

    public Fighter getTarget() {
        return target;
    }

    public FightCell getNewCell() {
        return newCell;
    }
}
