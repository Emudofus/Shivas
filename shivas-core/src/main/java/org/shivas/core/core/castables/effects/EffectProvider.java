package org.shivas.core.core.castables.effects;

import org.shivas.data.entity.SpellLevel;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 10:20
 */
public interface EffectProvider {
    EffectInterface provide(SpellLevel level);
}
