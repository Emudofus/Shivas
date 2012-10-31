package org.shivas.core.core.castables;

import com.google.common.base.Function;
import org.shivas.common.collections.CollectionQuery;
import org.shivas.common.maths.Range;
import org.shivas.data.entity.ItemEffect;
import org.shivas.data.entity.WeaponTemplate;
import org.shivas.core.core.castables.effects.EffectInterface;
import org.shivas.core.database.models.GameItem;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 11:16
 */
public class Weapon extends GameItem implements Castable {
    public static List<EffectInterface> toCritical(List<EffectInterface> effects, final short criticalBonus) {
        return CollectionQuery.from(effects).transform(new Function<EffectInterface, EffectInterface>() {
            public EffectInterface apply(EffectInterface input) {
                EffectInterface effect = input.copy();
                if (effect.getDice() != null) {
                    effect.getDice().upgrade(criticalBonus);
                } // effect.getDice()?.upgrade(criticalBonus);

                return effect;
            }
        }).computeList();
    }

    private List<EffectInterface> effects, criticalEffects;

    @Override
    public WeaponTemplate getTemplate() {
        return (WeaponTemplate) super.getTemplate();
    }

    @Override
    public void setItemEffects(Collection<ItemEffect> itemEffects) {
        super.setItemEffects(itemEffects);

        effects = CollectionQuery.from(getItemEffects()).ofType(EffectInterface.class).computeList();
        criticalEffects = toCritical(effects, getTemplate().getCriticalBonus());
    }

    @Override
    public short getCost() {
        return getTemplate().getCost();
    }

    @Override
    public short getCriticalRate() {
        return getTemplate().getCriticalRate();
    }

    @Override
    public short getFailureRate() {
        return getTemplate().getFailureRate();
    }

    @Override
    public Range getRange() {
        WeaponTemplate tpl = getTemplate();
        return new Range(tpl.getMinRange(), tpl.getMaxRange());
    }

    @Override
    public Collection<EffectInterface> getEffects(boolean critical) {
        return !critical ? effects : criticalEffects;
    }
}
