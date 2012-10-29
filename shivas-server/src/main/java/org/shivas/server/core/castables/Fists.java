package org.shivas.server.core.castables;

import org.shivas.common.maths.Range;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;
import org.shivas.server.core.castables.effects.DamageEffect;
import org.shivas.server.core.castables.effects.EffectInterface;
import org.shivas.server.core.castables.zones.SingleCellZone;

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
        setZone(SingleCellZone.INSTANCE);
    }};

    public static final Range RANGE = new Range(1, 1);

    private static final Collection<EffectInterface> EFFECTS = Collections.singleton(EFFECT);

    @Override
    public short getCost() {
        return 4;
    }

    @Override
    public short getCriticalRate() {
        return -1;
    }

    @Override
    public short getFailureRate() {
        return -1;
    }

    @Override
    public Range getRange() {
        return RANGE;
    }

    @Override
    public Collection<EffectInterface> getEffects(boolean critical) {
        return EFFECTS;
    }
}
