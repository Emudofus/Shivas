package org.shivas.data.entity.factory;

import org.shivas.data.EntityFactory;
import org.shivas.data.entity.*;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;

public abstract class AbstractEntityFactory implements EntityFactory {

	@Override
	public Breed newBreed() {
		return new Breed();
	}

	@Override
	public Experience newExperience() {
		return new Experience();
	}

	@Override
	public MapTemplate newMapTemplate() {
		return new MapTemplate();
	}

	@Override
	public MapTrigger newMapTrigger() {
		return new MapTrigger();
	}
	
	@Override
	public Waypoint newWaypoint() {
		return new Waypoint();
	}

	@Override
	public GameCell newGameCell() {
		return new GameCell();
	}

	@Override
	public ItemSet newItemSet() {
		return new ItemSet();
	}

	@Override
	public ItemTemplate newItemTemplate() {
		return new ItemTemplate(this);
	}

	@Override
	public ItemEffectTemplate newItemEffectTemplate() {
		return new ItemEffectTemplate(this);
	}

	@Override
	public ItemEffect newItemEffect(ItemEffectTemplate template) {
		return template.getEffect().isWeaponEffect() ?
				newWeaponItemEffect(template) :
				newConstantItemEffect(template);
	}

	@Override
	public WeaponItemEffect newWeaponItemEffect(ItemEffectTemplate template) {
		return new WeaponItemEffect(template.getEffect(), template.getBonus());
	}

    @Override
    public ConstantItemEffect newConstantItemEffect() {
        return new ConstantItemEffect();
    }

	@Override
	public ConstantItemEffect newConstantItemEffect(ItemEffectTemplate template) {
		return new ConstantItemEffect(template.getEffect(), (short) template.getBonus().roll());
	}

	@Override
	public WeaponTemplate newWeaponTemplate() {
		return new WeaponTemplate(this);
	}
	
	@Override
	public UsableItemTemplate newUsableItemTemplate() {
		return new UsableItemTemplate(this);
	}

	@Override
	public SpellTemplate newSpellTemplate() {
		return new SpellTemplate();
	}
	
	@Override
	public SpellLevel newSpellLevel() {
		return new SpellLevel();
	}
	
	@Override
	public SpellEffect newSpellEffect(SpellLevel level, SpellEffectTypeEnum type) {
		return new SpellEffect();
	}

	@Override
	public SpellBreed newSpellBreed() {
		return new SpellBreed();
	}

    @Override
    public NpcTemplate newNpcTemplate() {
        return new NpcTemplate();
    }

    @Override
    public Npc newNpc() {
        return new Npc();
    }

    @Override
    public NpcQuestion newNpcQuestion() {
        return new NpcQuestion();
    }

    @Override
    public NpcAnswer newNpcAnswer() {
        return new NpcAnswer();
    }

    @Override
    public NpcSale newNpcSale() {
        return new NpcSale();
    }

}
