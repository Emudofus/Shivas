package org.shivas.core.core.castables.effects.filters;

import org.shivas.core.core.fights.FightCell;
import org.shivas.core.core.fights.Fighter;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 03/11/12
 * Time: 16:21
 */
public class DefaultEffectFilter implements EffectFilter {
    public static DefaultEffectFilter fromFlags(int flags) {
        return new DefaultEffectFilter(
                (flags & 1)  != 0,
                (flags & 2)  != 0,
                (flags & 4)  != 0,
                (flags & 8)  != 0,
                (flags & 16) != 0
        );
    }

    private boolean ennemies, allies, caster, iCaster, invocations;

    public DefaultEffectFilter() {
        this(true, true, true, false, false);
    }

    public DefaultEffectFilter(boolean ennemies, boolean allies, boolean caster, boolean iCaster, boolean invocations) {
        this.ennemies = ennemies;
        this.allies = allies;
        this.caster = caster;
        this.iCaster = iCaster;
        this.invocations = invocations;
    }

    @Override
    public boolean apply(Fighter fighter, FightCell targetCell) {
        if (targetCell.getCurrentFighter() == null) return true;
        Fighter target = targetCell.getCurrentFighter();

        return ennemies && target.getTeam() != fighter.getTeam() ||
               allies   && target.getTeam() == fighter.getTeam() ||
               caster   && target == fighter;
    }
}
