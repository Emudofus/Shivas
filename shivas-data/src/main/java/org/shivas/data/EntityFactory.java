package org.shivas.data;

import org.shivas.data.entity.*;

public interface EntityFactory {
	Breed newBreed();
	
	Experience newExperience();
	
	MapTemplate newMapTemplate();
	MapTrigger newMapTrigger();
	GameCell newGameCell();
	
	ItemSet newItemSet();
	ItemTemplate newItemTemplate();
	ItemEffectTemplate newItemEffectTemplate();
	ItemEffect newItemEffect();
	Item newItem();
	WeaponTemplate newWeaponTemplate();
	
	SpellTemplate newSpellTemplate();
	SpellLevel newSpellLevel();
	SpellEffect newSpellEffect();
	SpellBreed newSpellBreed();
}
