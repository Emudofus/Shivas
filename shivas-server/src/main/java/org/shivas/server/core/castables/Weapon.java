package org.shivas.server.core.castables;

import com.google.common.base.Function;
import org.shivas.common.collections.CollectionQuery;
import org.shivas.data.entity.ItemEffect;
import org.shivas.data.entity.WeaponTemplate;
import org.shivas.server.core.castables.effects.EffectInterface;
import org.shivas.server.database.models.GameItem;

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
                effect.getDice().upgrade(criticalBonus);

                return effect;
            }
        }).computeList();
    }

    private List<EffectInterface> effects, criticalEffects;

    @Override
    public void setItemEffects(Collection<ItemEffect> itemEffects) {
        super.setItemEffects(itemEffects);

        WeaponTemplate tpl = (WeaponTemplate) getTemplate();

        effects = CollectionQuery.from(getItemEffects()).ofType(EffectInterface.class).computeList();
        criticalEffects = toCritical(effects, tpl.getCriticalBonus());
    }

    @Override
    public Collection<EffectInterface> getEffects() {
        return effects;
    }

    @Override
    public Collection<EffectInterface> getCriticalEffects() {
        return criticalEffects;
    }
}
