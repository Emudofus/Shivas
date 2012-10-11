package org.shivas.data.converter;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.shivas.common.maths.Range;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.data.entity.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class XMLDataOutputter implements DataOutputter {
	
	private static final String EXTENSION = ".xml";
	
	private final XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());

	@Override
	public void outputBreed(Breed breed, String fileName) throws IOException {
		Element root_elem = new Element("breeds");
		
		Element breed_elem = new Element("breed");
		breed_elem.setAttribute("id", String.valueOf(breed.getId()));
		breed_elem.setAttribute("startActionPoints", String.valueOf(breed.getStartActionPoints()));
		breed_elem.setAttribute("startMovementPoints", String.valueOf(breed.getStartMovementPoints()));
		breed_elem.setAttribute("startLife", String.valueOf(breed.getStartLife()));
		breed_elem.setAttribute("startProspection", String.valueOf(breed.getStartProspection()));
		
		for (Map.Entry<CharacteristicType, Map<Range, Breed.Level>> entry1 : breed.getLevels().entrySet()) {
			Element levels_elem = new Element("levels");
			levels_elem.setAttribute("type", entry1.getKey().name());
			
			for (Map.Entry<Range, Breed.Level> entry2 : entry1.getValue().entrySet()) {
				Element level_elem = new Element("level");
				level_elem.setAttribute("range", entry2.getKey().toString());
				level_elem.setAttribute("bonus", String.valueOf(entry2.getValue().bonus()));
				level_elem.setAttribute("cost", String.valueOf(entry2.getValue().cost()));
				
				levels_elem.addContent(level_elem);
			}
			breed_elem.addContent(levels_elem);
		}
		
		Element spells_elem = new Element("spells");
		for (SpellBreed spell : breed.getSpells().values()) {
			Element spell_elem = new Element("spell");
			
			spell_elem.setAttribute("id", String.valueOf(spell.getTemplate().getId()));
			spell_elem.setAttribute("minLevel", String.valueOf(spell.getMinLevel()));
			spell_elem.setAttribute("startPosition", String.valueOf(spell.getPosition()));
			
			spells_elem.addContent(spell_elem);
		}
		breed_elem.addContent(spells_elem);
		
		root_elem.addContent(breed_elem);

		out.output(root_elem, new BufferedWriter(new FileWriter(fileName + EXTENSION, false)));
	}

	@Override
	public void outputBreeds(Collection<Breed> breeds, String directory) throws IOException {
		for (Breed breed : breeds) {
			outputBreed(breed, directory + breed.getId());
		}
	}

	@Override
	public void outputExperiences(Collection<Experience> exps, String fileName) throws IOException {
		Element root_elem = new Element("experiences");
		
		for (Experience exp : exps) {
			Element exp_elem = new Element("experience");
			exp_elem.setAttribute("level", String.valueOf(exp.getLevel()));
			exp_elem.setAttribute("player", String.valueOf(exp.getPlayer()));
            exp_elem.setAttribute("guild", String.valueOf(exp.getGuild()));
			exp_elem.setAttribute("job", String.valueOf(exp.getJob()));
			exp_elem.setAttribute("mount", String.valueOf(exp.getMount()));
			exp_elem.setAttribute("alignment", String.valueOf(exp.getAlignment()));
			
			root_elem.addContent(exp_elem);
		}
		
		out.output(root_elem, new BufferedWriter(new FileWriter(fileName + EXTENSION, false)));
	}

	@Override
	public void outputMaps(Collection<MapData> maps, String fileName) throws IOException {
		Element root_elem = new Element("maps");
		
		for (MapData map : maps) {
			Element map_elem = new Element("map");
			map_elem.setAttribute("id", String.valueOf(map.getId()));
			map_elem.setAttribute("abscissa", String.valueOf(map.getPosition().abscissa()));
			map_elem.setAttribute("ordinate", String.valueOf(map.getPosition().ordinate()));
			map_elem.setAttribute("width", String.valueOf(map.getWidth()));
			map_elem.setAttribute("height", String.valueOf(map.getHeight()));
			map_elem.setAttribute("date", String.valueOf(map.getDate()));
			map_elem.setAttribute("subscriber", map.isSubscriber() ? "1" : "0");
			
			Element data_elem = new Element("data");
			data_elem.setAttribute("value", map.getData());
			map_elem.addContent(data_elem);
			
			Element key_elem = new Element("key");
			key_elem.setAttribute("value", map.getKey());
			map_elem.addContent(key_elem);
			
			for (MapTrigger trigger : map.getTrigger().values()) {
				Element trigger_elem = new Element("trigger");
				trigger_elem.setAttribute("id", String.valueOf(trigger.getId()));
				trigger_elem.setAttribute("cell", String.valueOf(trigger.getCell()));
				trigger_elem.setAttribute("next_map", trigger.getNextMap() != null ? String.valueOf(trigger.getNextMap().getId()) : "");
				trigger_elem.setAttribute("next_cell", String.valueOf(trigger.getNextCell()));
				
				map_elem.addContent(trigger_elem);
			}
			
			root_elem.addContent(map_elem);
		}
		
		out.output(root_elem, new BufferedWriter(new FileWriter(fileName + EXTENSION, false)));
	}

	@Override
	public void outputItemSets(Collection<ItemSet> itemsets, String fileName) throws IOException {
		Element root_elem = new Element("itemsets");
		
		for (ItemSet itemset : itemsets) {
			Element itemset_elem = new Element("itemset");
			
			itemset_elem.setAttribute("id", String.valueOf(itemset.getId()));
			
			for (ItemTemplate item : itemset.getItems()) {
				Element item_elem = new Element("item");
				item_elem.setAttribute("id", String.valueOf(item.getId()));
				
				itemset_elem.addContent(item_elem);
			}
			
			for (Map.Entry<Integer, Collection<ConstantItemEffect>> entry : itemset.getEffects().asMap().entrySet()) {
				Element effects_elem = new Element("effects");
				
				effects_elem.setAttribute("level", entry.getKey().toString());
				
				for (ConstantItemEffect effect : entry.getValue()) {
					Element effect_elem = new Element("effect");
					
					effect_elem.setAttribute("type", String.valueOf(effect.getType().value()));
					effect_elem.setAttribute("bonus", String.valueOf(effect.getBonus()));
					
					effects_elem.addContent(effect_elem);
				}
				
				itemset_elem.addContent(effects_elem);
			}
			
			root_elem.addContent(itemset_elem);
		}

		out.output(root_elem, new BufferedWriter(new FileWriter(fileName + EXTENSION, false)));
	}

	@Override
	public void outputItems(Collection<ItemTemplate> items, String fileName) throws IOException {
		Element root_elem = new Element("items");
		
		for (ItemTemplate item : items) {
			Element item_elem = new Element("item");
			
			item_elem.setAttribute("id", String.valueOf(item.getId()));
			item_elem.setAttribute("type", item.getType() != null ? String.valueOf(item.getType().value()) : "-1");
			item_elem.setAttribute("level", String.valueOf(item.getLevel()));
			item_elem.setAttribute("weight", String.valueOf(item.getWeight()));
			item_elem.setAttribute("forgemageable", String.valueOf(item.isForgemageable()));
			item_elem.setAttribute("price", String.valueOf(item.getPrice()));

			if (item.getType().isWeapon()) {
				WeaponTemplate weapon = (WeaponTemplate) item;
				item_elem.setAttribute("twoHands", String.valueOf(weapon.isTwoHands()));
				item_elem.setAttribute("ethereal", String.valueOf(weapon.isEthereal()));
			} else if (item.getType().isUsable()) {
				// TODO usable items
			}
			
			item_elem.addContent(new Element("conditions").setText(item.getConditions()));
			
			for (ItemEffectTemplate effect : item.getEffects()) {
				Element effect_elem = new Element("effect");
				
				effect_elem.setAttribute("type", String.valueOf(effect.getEffect().value()));
				effect_elem.setAttribute("bonus", effect.getBonus().toString());
				
				item_elem.addContent(effect_elem);
			}
			
			root_elem.addContent(item_elem);
		}

		out.output(root_elem, new BufferedWriter(new FileWriter(fileName + EXTENSION, false)));
	}
	
	private Element toElement(SpellEffect effect) {
		Element effect_elem = new Element("effect");
		effect_elem.setAttribute("type", String.valueOf(effect.getType()));
		effect_elem.setAttribute("first", String.valueOf(effect.getFirst()));
		effect_elem.setAttribute("second", String.valueOf(effect.getSecond()));
		effect_elem.setAttribute("third", String.valueOf(effect.getThird()));
		if (effect.getTurns() >= 0) effect_elem.setAttribute("turns", String.valueOf(effect.getTurns()));
		if (effect.getChance() >= 0) effect_elem.setAttribute("chance", String.valueOf(effect.getChance()));
		if (!effect.getDice().equals(Dofus1Dice.ZERO)) effect_elem.setAttribute("dice", String.valueOf(effect.getDice().toString()));
		if (!effect.getTarget().isEmpty()) effect_elem.setAttribute("target", String.valueOf(effect.getTarget()));
		
		return effect_elem;
	}

	@Override
	public void outputSpells(Collection<SpellTemplate> spells, String fileName) throws IOException {
		Element root_elem = new Element("spells");
		
		for (SpellTemplate spell : spells) {
			Element spell_elem = new Element("spell");
			spell_elem.setAttribute("id", String.valueOf(spell.getId()));
			
			Element sprite_elem = new Element("sprite");
			sprite_elem.setAttribute("id", String.valueOf(spell.getSprite()));
			sprite_elem.setAttribute("infos", spell.getSpriteInfos());
			spell_elem.addContent(sprite_elem);
			
			for (SpellLevel level : spell.getLevels()) {
				if (level == null) continue;
				
				Element level_elem = new Element("level");
				level_elem.setAttribute("id", String.valueOf(level.getId()));
				level_elem.setAttribute("costAP", String.valueOf(level.getCostAP()));
				level_elem.setAttribute("minRange", String.valueOf(level.getMinRange()));
				level_elem.setAttribute("maxRange", String.valueOf(level.getMaxRange()));
				level_elem.setAttribute("criticalRate", String.valueOf(level.getCriticalRate()));
				level_elem.setAttribute("failureRate", String.valueOf(level.getFailureRate()));
				level_elem.setAttribute("inline", String.valueOf(level.isInline()));
				level_elem.setAttribute("los", String.valueOf(level.isLos()));
				level_elem.setAttribute("emptyCell", String.valueOf(level.isEmptyCell()));
				level_elem.setAttribute("adjustableRange", String.valueOf(level.isAdjustableRange()));
				level_elem.setAttribute("endsTurnOnFailure", String.valueOf(level.isEndsTurnOnFailure()));
				level_elem.setAttribute("maxPerTurn", String.valueOf(level.getMaxPerTurn()));
				level_elem.setAttribute("maxPerPlayer", String.valueOf(level.getMaxPerPlayer()));
				level_elem.setAttribute("turns", String.valueOf(level.getTurns()));
				level_elem.setAttribute("rangeType", String.valueOf(level.getRangeType()));
				
				for (SpellEffect effect : level.getEffects()) {
					level_elem.addContent(toElement(effect));
				}
				for (SpellEffect effect : level.getCriticalEffects()) {
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
			
			waypoint_elem.setAttribute("id", String.valueOf(waypoint.getId()));
			waypoint_elem.setAttribute("map", String.valueOf(waypoint.getMap().getId()));
			waypoint_elem.setAttribute("cell", String.valueOf(waypoint.getCell()));
			
			root_elem.addContent(waypoint_elem);
		}

		out.output(root_elem, new BufferedWriter(new FileWriter(fileName + EXTENSION, false)));
	}

    @Override
    public void outputNpcTemplates(Collection<NpcTemplate> npcTemplates, String fileName) throws IOException {
        Element root_elem = new Element("npcTemplates");

        for (NpcTemplate npcTemplate : npcTemplates) {
            Element npcTemplate_elem = new Element("npcTemplate");
            npcTemplate_elem.setAttribute("id", String.valueOf(npcTemplate.getId()));
            npcTemplate_elem.setAttribute("type", npcTemplate.getType().name());
            npcTemplate_elem.setAttribute("gender", npcTemplate.getGender().name());
            npcTemplate_elem.setAttribute("skin", String.valueOf(npcTemplate.getSkin()));
            npcTemplate_elem.setAttribute("size", String.valueOf(npcTemplate.getSize()));
            npcTemplate_elem.setAttribute("extraClip", String.valueOf(npcTemplate.getExtraClip()));
            npcTemplate_elem.setAttribute("customArtwork", String.valueOf(npcTemplate.getCustomArtwork()));

            Element colors_elem = new Element("colors");
            colors_elem.setAttribute("first",  String.valueOf(npcTemplate.getColor1()));
            colors_elem.setAttribute("second", String.valueOf(npcTemplate.getColor2()));
            colors_elem.setAttribute("third",  String.valueOf(npcTemplate.getColor3()));
            npcTemplate_elem.addContent(colors_elem);

            Element accessories_elem = new Element("accessories");
            if (npcTemplate.getAccessory(0) != null) {
                accessories_elem.setAttribute("weapon", String.valueOf(npcTemplate.getAccessory(0).getId()));
            }
            if (npcTemplate.getAccessory(1) != null) {
                accessories_elem.setAttribute("hat", String.valueOf(npcTemplate.getAccessory(1).getId()));
            }
            if (npcTemplate.getAccessory(2) != null) {
                accessories_elem.setAttribute("cloak", String.valueOf(npcTemplate.getAccessory(2).getId()));
            }
            if (npcTemplate.getAccessory(3) != null) {
                accessories_elem.setAttribute("pet", String.valueOf(npcTemplate.getAccessory(3).getId()));
            }
            if (npcTemplate.getAccessory(4) != null) {
                accessories_elem.setAttribute("shield", String.valueOf(npcTemplate.getAccessory(4).getId()));
            }
            npcTemplate_elem.addContent(accessories_elem);

            root_elem.addContent(npcTemplate_elem);
        }

        out.output(root_elem, new BufferedWriter(new FileWriter(fileName + EXTENSION, false)));
    }

}
