package org.shivas.data.entity.factory;

import org.shivas.data.EntityFactory;
import org.shivas.data.entity.*;

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
		return new ItemEffectTemplate();
	}

	@Override
	public ItemEffect newItemEffect() {
		return new ItemEffect();
	}

	@Override
	public WeaponTemplate newWeaponTemplate() {
		return new WeaponTemplate(this);
	}

}
