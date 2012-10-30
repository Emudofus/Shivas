package org.shivas.core.database;

import org.shivas.data.Container;
import org.shivas.data.entity.*;
import org.shivas.data.entity.factory.AbstractEntityFactory;
import org.shivas.data.entity.factory.ActionFactory;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;
import org.shivas.core.config.Config;
import org.shivas.core.core.actions.ShivasActionFactory;
import org.shivas.core.core.castables.Weapon;
import org.shivas.core.core.castables.effects.EffectFactory;
import org.shivas.core.core.castables.effects.EffectInterface;
import org.shivas.core.core.castables.effects.SpellEffectAdapter;
import org.shivas.core.core.castables.effects.WeaponItemEffectAdapter;
import org.shivas.core.core.maps.GameMap;
import org.shivas.core.core.npcs.GameNpc;
import org.shivas.core.database.models.GameItem;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ShivasEntityFactory extends AbstractEntityFactory {

	@Inject
	private Config config;

    @Inject
    private EffectFactory effectFactory;
	
	@Override
	public GameMap newMapTemplate() {
		return new GameMap();
	}

	@Override
	public Item newItem(ItemTemplate template) {
		if (template instanceof WeaponTemplate) {
            return new Weapon();
        }
        return new GameItem();
	}

    @Override
    public Npc newNpc() {
        return new GameNpc();
    }

	@Override
	public ActionFactory newActionFactory(Container ctner) {
		return new ShivasActionFactory(ctner, config);
	}

    @Override
    public WeaponItemEffect newWeaponItemEffect(ItemEffectTemplate template) {
        EffectInterface effect = effectFactory.create(null, template.getEffect().toDamageSpellEffectType());
        return effect == null ?
                new WeaponItemEffect(template.getEffect(), template.getBonus()) :
                new WeaponItemEffectAdapter(template.getEffect(), template.getBonus(), effect);
    }

    @Override
    public SpellEffect newSpellEffect(SpellLevel level, SpellEffectTypeEnum type) {
        EffectInterface effect = effectFactory.create(level, type);
        return effect == null ?
                new SpellEffect() :
                new SpellEffectAdapter(effect);
    }
}
