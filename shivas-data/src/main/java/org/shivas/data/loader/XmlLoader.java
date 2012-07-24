package org.shivas.data.loader;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.shivas.common.maths.Point;
import org.shivas.common.maths.Range;
import org.shivas.common.random.Dofus1Dice;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.data.EntityFactory;
import org.shivas.data.entity.*;
import org.shivas.data.repository.BaseRepository;
import org.shivas.protocol.client.enums.ItemEffectEnum;
import org.shivas.protocol.client.enums.ItemTypeEnum;
import org.shivas.protocol.client.enums.SpellEffectsEnum;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public class XmlLoader extends AbstractLoader {
	
	private final SAXBuilder builder = new SAXBuilder();
	
	public XmlLoader(EntityFactory factory) {
		super(factory);
		
		loaders.put(Breed.class, new FileLoader<Breed>() {
			public void load(BaseRepository<Breed> repo, File file) throws Exception {
				loadBreed(repo, file);
			}
		});
		
		loaders.put(Experience.class, new FileLoader<Experience>() {
			public void load(BaseRepository<Experience> repo, File file) throws Exception {
				loadExperience(repo, file);
			}
		});
		
		loaders.put(MapTemplate.class, new FileLoader<MapTemplate>() {
			public void load(BaseRepository<MapTemplate> repo, File file) throws Exception {
				loadMap(repo, file);
			}
		});
		
		loaders.put(ItemSet.class, new FileLoader<ItemSet>() {
			public void load(BaseRepository<ItemSet> repo, File file) throws Exception {
				loadItemSet(repo, file);
			}
		});
		
		loaders.put(ItemTemplate.class, new FileLoader<ItemTemplate>() {
			public void load(BaseRepository<ItemTemplate> repo, File file) throws Exception {
				loadItemTemplate(repo, file);
			}
		});
		
		loaders.put(SpellTemplate.class, new FileLoader<SpellTemplate>() {
			public void load(BaseRepository<SpellTemplate> repo, File file) throws Exception {
				loadSpellTemplate(repo, file);
			}
		});
	}
	
	private void loadBreed(BaseRepository<Breed> repo, File file) throws Exception {
		Document doc = builder.build(file);
		Element root = doc.getDescendants(new ElementFilter("breeds")).next();
		for (Element element : root.getChildren("breed")) {
			Breed breed = factory.newBreed();
			
			breed.setId(element.getAttribute("id").getIntValue());
			breed.setStartActionPoints((short) element.getAttribute("startActionPoints").getIntValue());
			breed.setStartMovementPoints((short) element.getAttribute("startMovementPoints").getIntValue());
			breed.setStartLife((short) element.getAttribute("startLife").getIntValue());
			breed.setStartProspection((short) element.getAttribute("startProspection").getIntValue());
			
			Map<CharacteristicType, Map<Range, Breed.Level>> levels = Maps.newHashMap();
			for (Element child : element.getChildren("levels")) {
				Map<Range, Breed.Level> level = Maps.newHashMap();
				
				CharacteristicType type = CharacteristicType.valueOf(child.getAttributeValue("type"));
				for (Element child2 : child.getChildren("level")) {
					Range range = Range.parseRange(child2.getAttributeValue("range"));
					int bonus = child2.getAttribute("bonus").getIntValue(),
						cost  = child2.getAttribute("cost").getIntValue();
					
					level.put(range, new Breed.Level(cost, bonus));
				}
				
				levels.put(type, level);
			}
			breed.setLevels(levels);
			
			Map<Short, SpellBreed> spells = Maps.newHashMap();
			for (Element spell_elem : element.getChild("spells").getChildren()) {
				SpellBreed spell = new SpellBreed(
						breed,
						ctner.get(SpellTemplate.class).byId(spell_elem.getAttribute("id").getIntValue()),
						spell_elem.getAttribute("minLevel").getIntValue(),
						spell_elem.getAttribute("startPosition").getIntValue()
				);
				
				spells.put(spell.getTemplate().getId(), spell);
			}
			breed.setSpells(spells);
			
			repo.put(breed.getId(), breed);
		}
	}
	
	private void loadExperience(BaseRepository<Experience> repo, File file) throws Exception {		
		Document doc = builder.build(file);
		Element root = doc.getDescendants(new ElementFilter("experiences")).next();
		for (Element element : root.getChildren("experience")) {
			Experience experience = factory.newExperience();
			experience.setLevel((short) element.getAttribute("level").getIntValue());
			experience.setPlayer(element.getAttribute("player").getLongValue());
			experience.setJob(element.getAttribute("job").getIntValue());
			experience.setMount(element.getAttribute("mount").getIntValue());
			experience.setAlignment((short) element.getAttribute("alignment").getIntValue());
			
			repo.put(experience.getLevel(), experience);
		}

		for (int i = 2; i <= repo.count(); ++i) {
			Experience previous = repo.byId(i - 1);
			Experience current = repo.byId(i);
			
			previous.setNext(current);
			current.setPrevious(previous);
		}
	}
	
	private void loadMap(BaseRepository<MapTemplate> repo, File file) throws Exception {
		Document doc = builder.build(file);
		
		Element root = doc.getDescendants(new ElementFilter("maps")).next();
		for (Element element : root.getChildren("map")) {
			MapTemplate map = factory.newMapTemplate();
			map.setId(element.getAttribute("id").getIntValue());
			map.setPosition(new Point(
					element.getAttribute("abscissa").getIntValue(),
					element.getAttribute("ordinate").getIntValue()
			));
			map.setWidth(element.getAttribute("width").getIntValue());
			map.setHeight(element.getAttribute("height").getIntValue());
			map.setCells(CellLoader.parse(element.getChild("data").getAttributeValue("value"), factory));
			map.setDate(element.getAttribute("date").getValue());
			map.setKey(element.getChild("key").getAttributeValue("value"));
			map.setSubscriber(element.getAttributeValue("subscriber") == "1");
			
			repo.put(map.getId(), map);
		}
		
		for (Element element : root.getChildren("map")) {
			MapTemplate map = repo.byId(element.getAttribute("id").getIntValue());
			
			Map<Short, MapTrigger> triggers = Maps.newHashMap();
			for (Element trigger_elem : element.getChildren("trigger")) {
				MapTrigger trigger = factory.newMapTrigger();
				//trigger.setId(trigger_elem.getAttribute("id").getIntValue()); <- useless
				trigger.setMap(map);
				trigger.setCell((short) trigger_elem.getAttribute("cell").getIntValue());
				if (!trigger_elem.getAttribute("next_map").getValue().isEmpty()) {
					trigger.setNextMap(repo.byId(trigger_elem.getAttribute("next_map").getIntValue()));
				}
				trigger.setNextCell((short) trigger_elem.getAttribute("next_cell").getIntValue());
				
				triggers.put(trigger.getCell(), trigger);
			}
			map.setTrigger(triggers);
		}
	}
	
	private void loadItemSet(BaseRepository<ItemSet> repo, File file) throws Exception {
		Document doc = builder.build(file);
		
		Element root = doc.getDescendants(new ElementFilter("itemsets")).next();
		
		for (Element elem : root.getChildren("itemset")) {
			ItemSet itemset = factory.newItemSet();
			itemset.setId((short) elem.getAttribute("id").getIntValue());
			
			List<ItemTemplate> items = Lists.newArrayList();
			for (Element item_elem : elem.getChildren("item")) {
				int itemid = item_elem.getAttribute("id").getIntValue();
				
				ItemTemplate item = get(ItemTemplate.class).byId(itemid);
				if (item != null) {
					item.setItemSet(itemset);
					items.add(item);
				}
			}
			itemset.setItems(items);
			
			Multimap<Integer, ItemEffect> effects = ArrayListMultimap.create();
			
			for (Element effects_elem : elem.getChildren("effects")) {
				int level = effects_elem.getAttribute("level").getIntValue();
				
				for (Element effect_elem : effects_elem.getChildren("effect")) {
					ItemEffect effect = factory.newItemEffect();
					effect.setEffect(ItemEffectEnum.valueOf(effect_elem.getAttribute("type").getIntValue()));
					effect.setBonus((short) effect_elem.getAttribute("bonus").getIntValue());
					
					effects.put(level, effect);
				}
			}
			
			itemset.setEffects(effects);
			
			repo.put(itemset.getId(), itemset);
		}
	}
	
	private void loadItemTemplate(BaseRepository<ItemTemplate> repo, File file) throws Exception {
		Document doc = builder.build(file);
		
		Element root = doc.getDescendants(new ElementFilter("items")).next();
		for (Element item_elem : root.getChildren("item")) {
			ItemTypeEnum type = ItemTypeEnum.valueOf(item_elem.getAttribute("type").getIntValue());
			
			ItemTemplate item;
			
			if (type.isWeapon()) {
				WeaponTemplate weapon = factory.newWeaponTemplate();
				weapon.setTwoHands(item_elem.getAttribute("twoHands").getBooleanValue());
				weapon.setEthereal(item_elem.getAttribute("ethereal").getBooleanValue());
				
				item = weapon;
			} else if (type.isUsable()) {
				item = factory.newItemTemplate(); // TODO usable items
			} else {
				item = factory.newItemTemplate();
			}
			
			item.setId((short) item_elem.getAttribute("id").getIntValue());			
			item.setType(type);
			item.setLevel((short) item_elem.getAttribute("level").getIntValue());
			item.setWeight((short) item_elem.getAttribute("weight").getIntValue());
			item.setForgemageable(item_elem.getAttribute("forgemageable").getBooleanValue());
			item.setPrice((short) item_elem.getAttribute("price").getIntValue());
			item.setConditions(item_elem.getChildText("conditions"));
			
			List<ItemEffectTemplate> effects = Lists.newArrayList();
			for (Element effect_elem : item_elem.getChildren("effect")) {
				ItemEffectTemplate effect = factory.newItemEffectTemplate();
				
				effect.setEffect(ItemEffectEnum.valueOf(effect_elem.getAttribute("type").getIntValue()));
				effect.setBonus(Dofus1Dice.parseDice(effect_elem.getAttributeValue("bonus")));
				
				effects.add(effect);
			}
			item.setEffects(effects);
			
			repo.put(item.getId(), item);
		}
	}
	
	private void loadSpellTemplate(BaseRepository<SpellTemplate> repo, File file) throws Exception {
		Document doc = builder.build(file);
		
		Element root = doc.getDescendants(new ElementFilter("spells")).next();
		for (Element spell_elem : root.getChildren("spell")) {
			Element sprite_elem = spell_elem.getChild("sprite");
			
			SpellTemplate spell = factory.newSpellTemplate();
			spell.setId((short) spell_elem.getAttribute("id").getIntValue());
			spell.setSprite((short) sprite_elem.getAttribute("id").getIntValue());
			spell.setSpriteInfos(sprite_elem.getAttributeValue("infos"));
			
			SpellLevel[] levels = new SpellLevel[SpellTemplate.MAX_LEVELS];
			for (Element level_elem : spell_elem.getChildren("level")) {
				SpellLevel level = factory.newSpellLevel();
				level.setId((byte) level_elem.getAttribute("id").getIntValue());
				level.setSpell(spell);
				level.setMinRange((byte) level_elem.getAttribute("minRange").getIntValue());
				level.setMaxRange((byte) level_elem.getAttribute("maxRange").getIntValue());
				level.setCriticalRate((short) level_elem.getAttribute("criticalRate").getIntValue());
				level.setFailureRate((short) level_elem.getAttribute("failureRate").getIntValue());
				level.setInline(level_elem.getAttribute("inline").getBooleanValue());
				level.setLos(level_elem.getAttribute("los").getBooleanValue());
				level.setEmptyCell(level_elem.getAttribute("emptyCell").getBooleanValue());
				level.setAdjustableRange(level_elem.getAttribute("adjustableRange").getBooleanValue());
				level.setEndsTurnOnFailure(level_elem.getAttribute("endsTurnOnFailure").getBooleanValue());
				level.setMaxPerTurn((byte) level_elem.getAttribute("maxPerTurn").getIntValue());
				level.setMaxPerPlayer((byte) level_elem.getAttribute("maxPerPlayer").getIntValue());
				level.setTurns((byte) level_elem.getAttribute("turns").getIntValue());
				level.setRangeType(level_elem.getAttributeValue("rangeType"));
				
				List<SpellEffect> effects = Lists.newArrayList(), criticalEffects = Lists.newArrayList();
				for (Element effect_elem : level_elem.getChildren("effect")) {
					SpellEffect effect = factory.newSpellEffect();
					effect.setLevel(level);
					effect.setType(SpellEffectsEnum.valueOf(effect_elem.getAttribute("type").getIntValue()));
					effect.setFirst((short) effect_elem.getAttribute("first").getIntValue());
					effect.setSecond((short) effect_elem.getAttribute("second").getIntValue());
					effect.setThird((short) effect_elem.getAttribute("third").getIntValue());
					
					if (effect_elem.getAttribute("turns") != null)
						effect.setTurns((short) effect_elem.getAttribute("turns").getIntValue());
					
					if (effect_elem.getAttribute("chance") != null)
						effect.setChance((short) effect_elem.getAttribute("chance").getIntValue());
					
					if (effect_elem.getAttribute("dice") != null)
						effect.setDice(Dofus1Dice.parseDice(effect_elem.getAttributeValue("dice")));
					else
						effect.setDice(Dofus1Dice.ZERO);
					
					if (effect_elem.getAttribute("target") != null)
						effect.setTarget(effect_elem.getAttributeValue("target"));
					
					if (effect_elem.getAttribute("critical") != null) {
						criticalEffects.add(effect);
					} else {
						effects.add(effect);
					}
				}
				level.setEffects(effects);
				level.setCriticalEffects(criticalEffects);
				
				levels[level.getId() - 1] = level;
			}
			spell.setLevels(levels);
			
			repo.put(spell.getId(), spell);
		}
	}

}
