package org.shivas.core.core.fights.events;

import org.shivas.protocol.client.enums.ActionTypeEnum;
import org.shivas.core.core.castables.Castable;
import org.shivas.core.core.castables.Fists;
import org.shivas.core.core.castables.Weapon;
import org.shivas.core.core.fights.FightCell;
import org.shivas.core.core.fights.Fighter;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 18:24
 */
public class FighterCastEvent extends FighterActionEvent {
    static ActionTypeEnum byCastableType(Castable castable) {
        return castable instanceof Weapon || castable == Fists.INSTANCE ?
                ActionTypeEnum.MELEE_ATTACK :
                ActionTypeEnum.CAST_SPELL;
    }

    private final Castable castable;
    private final FightCell target;
    private final boolean critical, failure;

    public FighterCastEvent(Fighter fighter, Castable castable, FightCell target, boolean critical, boolean failure) {
        super(fighter, byCastableType(castable));
        this.castable = castable;
        this.target = target;
        this.critical = critical;
        this.failure = failure;
    }

    public Castable getCastable() {
        return castable;
    }

    public FightCell getTarget() {
        return target;
    }

    public boolean isCritical() {
        return critical;
    }

    public boolean isFailure() {
        return failure;
    }
}
