package org.shivas.core.core.castables.effects.filters;

import org.shivas.core.core.fights.FightCell;
import org.shivas.core.core.fights.Fighter;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 03/11/12
 * Time: 16:20
 */
public interface EffectFilter {
    boolean apply(Fighter caster, FightCell target);
}
