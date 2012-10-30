package org.shivas.core.core.fights.events;

import org.shivas.common.statistics.Characteristic;
import org.shivas.protocol.client.enums.ActionTypeEnum;
import org.shivas.core.core.fights.Fighter;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 21/10/12
 * Time: 15:07
 */
public class FighterStatsUpdateEvent extends FighterActionEvent {
    private final Characteristic charac;

    public FighterStatsUpdateEvent(Fighter fighter, ActionTypeEnum actionType, Characteristic charac) {
        super(fighter, actionType);
        this.charac = charac;
    }

    public Characteristic getCharac() {
        return charac;
    }
}
