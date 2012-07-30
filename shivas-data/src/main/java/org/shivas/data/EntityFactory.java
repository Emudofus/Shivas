package org.shivas.data;

import org.shivas.data.entity.*;
import org.shivas.data.entity.factory.ItemActionFactory;
import org.shivas.protocol.client.enums.ItemEffectEnum;

public interface EntityFactory {
	Breed newBreed();
	
	Experience newExperience();
	
	MapTemplate newMapTemplate();
	MapTrigger newMapTrigger();
	GameCell newGameCell();
	
	ItemSet newItemSet();
	ItemTemplate newItemTemplate();
	WeaponTemplate newWeaponTemplate();
	UsableItemTemplate newUsableItemTemplate();
	ItemEffectTemplate newItemEffectTemplate();

	Item newItem();
	ItemEffect newItemEffect(ItemEffectEnum type);
	ConstantItemEffect newConstantItemEffect();
	WeaponItemEffect newWeaponItemEffect();
	
	ItemActionFactory newItemActionFactory();
	
	SpellTemplate newSpellTemplate();
	SpellLevel newSpellLevel();
	SpellEffect newSpellEffect();
	SpellBreed newSpellBreed();
}
