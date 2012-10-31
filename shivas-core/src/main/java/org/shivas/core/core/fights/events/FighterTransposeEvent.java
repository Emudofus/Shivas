package org.shivas.core.core.fights.events;

import org.shivas.core.core.fights.Fighter;
import org.shivas.protocol.client.enums.ActionTypeEnum;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 31/10/12
 * Time: 14:21
 */
public class FighterTransposeEvent extends FighterActionEvent {
    private final Fighter target;

    public FighterTransposeEvent(Fighter caster, Fighter target) {
        super(caster, ActionTypeEnum.CELL_CHANGEMENT);
        this.target = target;
    }

    public Fighter getTarget() {
        return target;
    }
}
