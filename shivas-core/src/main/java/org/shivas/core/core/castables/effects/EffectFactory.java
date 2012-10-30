package org.shivas.core.core.castables.effects;

import org.shivas.data.entity.SpellLevel;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 10:02
 */
public interface EffectFactory {
    EffectInterface create(SpellLevel level, SpellEffectTypeEnum effect);
}
