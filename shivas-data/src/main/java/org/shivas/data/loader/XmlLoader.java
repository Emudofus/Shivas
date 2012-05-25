package org.shivas.data.loader;

import java.io.File;
import java.util.ArrayList;
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
import org.shivas.data.entity.Breed;
import org.shivas.data.entity.Experience;
import org.shivas.data.entity.ItemEffect;
import org.shivas.data.entity.ItemEffectTemplate;
import org.shivas.data.entity.ItemSet;
import org.shivas.data.entity.ItemTemplate;
import org.shivas.data.entity.MapTemplate;
import org.shivas.data.entity.MapTrigger;
import org.shivas.data.repository.BaseRepository;
import org.shivas.protocol.client.enums.ItemEffectEnum;
import org.shivas.protocol.client.enums.ItemTypeEnum;

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
		
		for (Element elem : root.getChildren()) {
			ItemSet itemset = factory.newItemSet();
			itemset.setId((short) elem.getAttribute("id").getIntValue());
			itemset.setItems(new ArrayList<ItemTemplate>());
			
			Multimap<Integer, ItemEffect> effects = ArrayListMultimap.create();
			
			for (Element effects_elem : elem.getChildren("effects")) {
				int level = effects_elem.getAttribute("level").getIntValue();
				
				for (Element effect_elem : effects_elem.getChildren("effect")) {
					ItemEffect effect = factory.newItemEffect();
					effect.setEffect(ItemEffectEnum.valueOf(effect_elem.getAttribute("type").getIntValue()));
					effect.setBonus((short) effect_elem.getAttribute("bonus").getIntValue());
					
					itemset.getEffects().put(level, effect);
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
			ItemTemplate item = factory.newItemTemplate();
			
			item.setId((short) item_elem.getAttribute("id").getIntValue());
			
			item.setItemSet(get(ItemSet.class).byId(item_elem.getAttribute("set").getIntValue()));
			item.getItemSet().getItems().add(item);
			
			item.setType(ItemTypeEnum.valueOf(item_elem.getAttribute("type").getIntValue()));
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
			}
			item.setEffects(effects);
			
			repo.put(item.getId(), item);
		}
	}

}
