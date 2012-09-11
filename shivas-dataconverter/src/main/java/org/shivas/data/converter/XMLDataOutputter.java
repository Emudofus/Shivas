package org.shivas.data.converter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.shivas.common.maths.Range;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.data.converter.Structs.Breed;
import org.shivas.data.converter.Structs.Waypoint;

public class XMLDataOutputter implements DataOutputter {
	
	private static final String EXTENSION = ".xml";
	
	private final XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());

	@Override
	public void outputBreed(Structs.Breed breed, String fileName) throws IOException {
		Element root_elem = new Element("breeds");
		
		Element breed_elem = new Element("breed");
		breed_elem.setAttribute("id", String.valueOf(breed.id));
		breed_elem.setAttribute("startActionPoints", String.valueOf(breed.startActionPoints));
		breed_elem.setAttribute("startMovementPoints", String.valueOf(breed.startMovementPoints));
		breed_elem.setAttribute("startLife", String.valueOf(breed.startLife));
		breed_elem.setAttribute("startProspection", String.valueOf(breed.startProspection));
		
		for (Map.Entry<CharacteristicType, Map<Range, Structs.BreedLevel>> entry1 : breed.levels.entrySet()) {
			Element levels_elem = new Element("levels");
			levels_elem.setAttribute("type", entry1.getKey().name());
			
			for (Map.Entry<Range, Structs.BreedLevel> entry2 : entry1.getValue().entrySet()) {
				Element level_elem = new Element("level");
				level_elem.setAttribute("range", entry2.getKey().toString());
				level_elem.setAttribute("bonus", String.valueOf(entry2.getValue().bonus));
				level_elem.setAttribute("cost", String.valueOf(entry2.getValue().cost));
				
				levels_elem.addContent(level_elem);
			}
			breed_elem.addContent(levels_elem);
		}
		
		Element spells_elem = new Element("spells");
		Collections.sort(breed.spells, new Comparator<Structs.SpellBreed>() {
			public int compare(Structs.SpellBreed o1, Structs.SpellBreed o2) {
				return o1.level - o2.level;
			}
		});
		for (Structs.SpellBreed spell : breed.spells) {
			Element spell_elem = new Element("spell");
			
			spell_elem.setAttribute("id", String.valueOf(spell.template.id));
			spell_elem.setAttribute("minLevel", String.valueOf(spell.level));
			spell_elem.setAttribute("startPosition", String.valueOf(spell.position));
			
			spells_elem.addContent(spell_elem);
		}
		breed_elem.addContent(spells_elem);
		
		root_elem.addContent(breed_elem);

		out.output(root_elem, new BufferedWriter(new FileWriter(fileName + EXTENSION, false)));
	}

	@Override
	public void outputBreeds(Collection<Structs.Breed> breeds, String directory) throws IOException {
		for (Breed breed : breeds) {
			outputBreed(breed, directory + breed.name);
		}
	}

	@Override
	public void outputExperiences(Collection<Structs.Experience> exps, String fileName) throws IOException {
		Element root_elem = new Element("experiences");
		
		for (Structs.Experience exp : exps) {
			Element exp_elem = new Element("experience");
			exp_elem.setAttribute("level", String.valueOf(exp.level));
			exp_elem.setAttribute("player", String.valueOf(exp.player));
			exp_elem.setAttribute("job", String.valueOf(exp.job));
			exp_elem.setAttribute("mount", String.valueOf(exp.mount));
			exp_elem.setAttribute("alignment", String.valueOf(exp.alignment));
			
			root_elem.addContent(exp_elem);
		}
		
		out.output(root_elem, new BufferedWriter(new FileWriter(fileName + EXTENSION, false)));
	}

	@Override
	public void outputMaps(Collection<Structs.GameMap> maps, String fileName) throws IOException {
		Element root_elem = new Element("maps");
		
		for (Structs.GameMap map : maps) {			
			Element map_elem = new Element("map");
			map_elem.setAttribute("id", String.valueOf(map.id));
			map_elem.setAttribute("abscissa", String.valueOf(map.position.abscissa()));
			map_elem.setAttribute("ordinate", String.valueOf(map.position.ordinate()));
			map_elem.setAttribute("width", String.valueOf(map.width));
			map_elem.setAttribute("height", String.valueOf(map.height));
			map_elem.setAttribute("date", String.valueOf(map.date));
			map_elem.setAttribute("subscriber", map.subscriber ? "1" : "0");
			
			Element data_elem = new Element("data");
			data_elem.setAttribute("value", map.data);
			map_elem.addContent(data_elem);
			
			Element key_elem = new Element("key");
			key_elem.setAttribute("value", map.key);
			map_elem.addContent(key_elem);
			
			for (Structs.GameMapTrigger trigger : map.triggers) {
				Element trigger_elem = new Element("trigger");
				trigger_elem.setAttribute("id", String.valueOf(trigger.id));
				trigger_elem.setAttribute("cell", String.valueOf(trigger.cell));
				trigger_elem.setAttribute("next_map", trigger.nextMap != null ? String.valueOf(trigger.nextMap.id) : "");
				trigger_elem.setAttribute("next_cell", String.valueOf(trigger.nextCell));
				
				map_elem.addContent(trigger_elem);
			}
			
			root_elem.addContent(map_elem);
		}
		
		out.output(root_elem, new BufferedWriter(new FileWriter(fileName + EXTENSION, false)));
	}

	@Override
	public void outputItemSets(Collection<Structs.ItemSet> itemsets, String fileName) throws IOException {
		Element root_elem = new Element("itemsets");
		
		for (Structs.ItemSet itemset : itemsets) {
			Element itemset_elem = new Element("itemset");
			
			itemset_elem.setAttribute("id", String.valueOf(itemset.id));
			
			for (Structs.ItemTemplate item : itemset.items) {
				Element item_elem = new Element("item");
				item_elem.setAttribute("id", String.valueOf(item.id));
				
				itemset_elem.addContent(item_elem);
			}
			
			for (Map.Entry<Integer, Collection<Structs.ItemEffect>> entry : itemset.effects.asMap().entrySet()) {
				Element effects_elem = new Element("effects");
				
				effects_elem.setAttribute("level", entry.getKey().toString());
				
				for (Structs.ItemEffect effect : entry.getValue()) {
					Element effect_elem = new Element("effect");
					
					effect_elem.setAttribute("type", String.valueOf(effect.effect.value()));
					effect_elem.setAttribute("bonus", String.valueOf(effect.bonus));
					
					effects_elem.addContent(effect_elem);
				}
				
				itemset_elem.addContent(effects_elem);
			}
			
			root_elem.addContent(itemset_elem);
		}

		out.output(root_elem, new BufferedWriter(new FileWriter(fileName + EXTENSION, false)));
	}

	@Override
	public void outputItems(Collection<Structs.ItemTemplate> items, String fileName) throws IOException {
		Element root_elem = new Element("items");
		
		for (Structs.ItemTemplate item : items) {
			Element item_elem = new Element("item");
			
			item_elem.setAttribute("id", String.valueOf(item.id));
			item_elem.setAttribute("type", item.type != null ? String.valueOf(item.type.value()) : "-1");
			item_elem.setAttribute("level", String.valueOf(item.level));
			item_elem.setAttribute("weight", String.valueOf(item.weight));
			item_elem.setAttribute("forgemageable", String.valueOf(item.forgemageable));
			item_elem.setAttribute("price", String.valueOf(item.price));
			
			if (item.type.isWeapon()) {
				Structs.WeaponItemTemplate weapon = (Structs.WeaponItemTemplate) item;
				item_elem.setAttribute("twoHands", String.valueOf(weapon.twoHands));
				item_elem.setAttribute("ethereal", String.valueOf(weapon.ethereal));
			} else if (item.type.isUsable()) {
				// TODO usable items
			}
			
			item_elem.addContent(new Element("conditions").setText(item.conditions));
			
			for (Structs.ItemEffectTemplate effect : item.effects) {
				Element effect_elem = new Element("effect");
				
				effect_elem.setAttribute("type", String.valueOf(effect.effect.value()));
				effect_elem.setAttribute("bonus", effect.bonus.toString());
				
				item_elem.addContent(effect_elem);
			}
			
			root_elem.addContent(item_elem);
		}

		out.output(root_elem, new BufferedWriter(new FileWriter(fileName + EXTENSION, false)));
	}
	
	private Element toElement(Structs.SpellEffect effect) {
		Element effect_elem = new Element("effect");
		effect_elem.setAttribute("type", String.valueOf(effect.type));
		effect_elem.setAttribute("first", String.valueOf(effect.first));
		effect_elem.setAttribute("second", String.valueOf(effect.second));
		effect_elem.setAttribute("third", String.valueOf(effect.third));
		if (effect.turns >= 0) effect_elem.setAttribute("turns", String.valueOf(effect.turns));
		if (effect.chance >= 0) effect_elem.setAttribute("chance", String.valueOf(effect.chance));
		if (!effect.dice.equals(Dofus1Dice.ZERO)) effect_elem.setAttribute("dice", String.valueOf(effect.dice.toString()));
		if (effect.target != "") effect_elem.setAttribute("target", String.valueOf(effect.target));
		
		return effect_elem;
	}

	@Override
	public void outputSpells(Collection<Structs.SpellTemplate> spells, String fileName) throws IOException {
		Element root_elem = new Element("spells");
		
		for (Structs.SpellTemplate spell : spells) {
			Element spell_elem = new Element("spell");
			spell_elem.setAttribute("id", String.valueOf(spell.id));
			
			Element sprite_elem = new Element("sprite");
			sprite_elem.setAttribute("id", String.valueOf(spell.sprite));
			sprite_elem.setAttribute("infos", spell.spriteInfos);
			spell_elem.addContent(sprite_elem);
			
			for (Structs.SpellLevel level : spell.levels) {
				if (level == null) continue;
				
				Element level_elem = new Element("level");
				level_elem.setAttribute("id", String.valueOf(level.id));
				level_elem.setAttribute("costAP", String.valueOf(level.costAP));
				level_elem.setAttribute("minRange", String.valueOf(level.minRange));
				level_elem.setAttribute("maxRange", String.valueOf(level.maxRange));
				level_elem.setAttribute("criticalRate", String.valueOf(level.criticalRate));
				level_elem.setAttribute("failureRate", String.valueOf(level.failureRate));
				level_elem.setAttribute("inline", String.valueOf(level.inline));
				level_elem.setAttribute("los", String.valueOf(level.los));
				level_elem.setAttribute("emptyCell", String.valueOf(level.emptyCell));
				level_elem.setAttribute("adjustableRange", String.valueOf(level.adjustableRange));
				level_elem.setAttribute("endsTurnOnFailure", String.valueOf(level.endsTurnOnFailure));
				level_elem.setAttribute("maxPerTurn", String.valueOf(level.maxPerTurn));
				level_elem.setAttribute("maxPerPlayer", String.valueOf(level.maxPerPlayer));
				level_elem.setAttribute("turns", String.valueOf(level.turns));
				level_elem.setAttribute("rangeType", String.valueOf(level.rangeType));
				
				for (Structs.SpellEffect effect : level.effects) {					
					level_elem.addContent(toElement(effect));
				}
				for (Structs.SpellEffect effect : level.criticalEffects) {
					Element effect_elem = toElement(effect);
					effect_elem.setAttribute("critical", "");
					
					level_elem.addContent(effect_elem);
				}
				
				spell_elem.addContent(level_elem);
			}
			
			root_elem.addContent(spell_elem);
		}

		out.output(root_elem, new BufferedWriter(new FileWriter(fileName + EXTENSION, false)));
	}

	@Override
	public void outputWaypoints(Collection<Waypoint> waypoints, String fileName) throws IOException {
		Element root_elem = new Element("waypoints");
		
		for (Waypoint waypoint : waypoints) {
			Element waypoint_elem = new Element("waypoint");
			
			waypoint_elem.setAttribute("id", String.valueOf(waypoint.id));
			waypoint_elem.setAttribute("map", String.valueOf(waypoint.mapId));
			waypoint_elem.setAttribute("cell", String.valueOf(waypoint.cell));
			
			root_elem.addContent(waypoint_elem);
		}

		out.output(root_elem, new BufferedWriter(new FileWriter(fileName + EXTENSION, false)));
	}

}
