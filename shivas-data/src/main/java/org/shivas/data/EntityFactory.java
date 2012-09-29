package org.shivas.data;

import org.shivas.data.entity.*;
import org.shivas.data.entity.factory.ActionFactory;
import org.shivas.protocol.client.enums.ItemEffectEnum;

public interface EntityFactory {
	Breed newBreed();
	
	Experience newExperience();
	
	MapTemplate newMapTemplate();
	MapTrigger newMapTrigger();
	GameCell newGameCell();
	Waypoint newWaypoint();
	
	ItemSet newItemSet();
	ItemTemplate newItemTemplate();
	WeaponTemplate newWeaponTemplate();
	UsableItemTemplate newUsableItemTemplate();
	ItemEffectTemplate newItemEffectTemplate();

	Item newItem();
	ItemEffect newItemEffect(ItemEffectEnum type);
	ConstantItemEffect newConstantItemEffect();
	WeaponItemEffect newWeaponItemEffect();
	
	ActionFactory newActionFactory(Container ctner);
	
	SpellTemplate newSpellTemplate();
	SpellLevel newSpellLevel();
	SpellEffect newSpellEffect();
	SpellBreed newSpellBreed();

    NpcTemplate newNpcTemplate();
    Npc newNpc();
}
