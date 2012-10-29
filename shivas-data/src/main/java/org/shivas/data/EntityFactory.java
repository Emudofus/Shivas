package org.shivas.data;

import org.shivas.data.entity.*;
import org.shivas.data.entity.factory.ActionFactory;
import org.shivas.protocol.client.enums.SpellEffectTypeEnum;

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
	ItemEffect newItemEffect(ItemEffectTemplate template);
    ConstantItemEffect newConstantItemEffect();
	ConstantItemEffect newConstantItemEffect(ItemEffectTemplate template);
	WeaponItemEffect newWeaponItemEffect(ItemEffectTemplate template);
	
	ActionFactory newActionFactory(Container ctner);
	
	SpellTemplate newSpellTemplate();
	SpellLevel newSpellLevel();
	SpellEffect newSpellEffect(SpellLevel level, SpellEffectTypeEnum type);
	SpellBreed newSpellBreed();

    NpcTemplate newNpcTemplate();
    Npc         newNpc();
    NpcQuestion newNpcQuestion();
    NpcAnswer   newNpcAnswer();
    NpcSale     newNpcSale();
}
