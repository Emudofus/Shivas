package org.shivas.server.core.castables;

import org.shivas.common.random.Dofus1Dice;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;
import org.shivas.server.core.castables.effects.DamageEffect;
import org.shivas.server.core.castables.effects.EffectInterface;

import java.util.Collection;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 17:04
 */
public final class Fists implements Castable {
    public static final Fists INSTANCE = new Fists();
    private Fists() {}

    public static final EffectInterface EFFECT = new DamageEffect(null, SpellEffectTypeEnum.DamageNeutral) {{
        setDice(new Dofus1Dice(1, 5)); // 1d5
    }};

    private static final Collection<EffectInterface> EFFECTS = Collections.singleton(EFFECT);

    @Override
    public Collection<EffectInterface> getEffects() {
        return EFFECTS;
    }

    @Override
    public Collection<EffectInterface> getCriticalEffects() {
        return null;
    }
}
