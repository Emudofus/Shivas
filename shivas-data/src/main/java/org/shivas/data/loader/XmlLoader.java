package org.shivas.data.loader;

import java.io.File;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.shivas.common.maths.Point;
import org.shivas.common.maths.Range;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.data.EntityFactory;
import org.shivas.data.entity.Breed;
import org.shivas.data.entity.Experience;
import org.shivas.data.entity.GameMap;
import org.shivas.data.entity.MapTrigger;
import org.shivas.data.repository.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class XmlLoader extends AbstractLoader {
	
	private static final Logger log = LoggerFactory.getLogger(XmlLoader.class);
	
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
		
		loaders.put(GameMap.class, new FileLoader<GameMap>() {
			public void load(BaseRepository<GameMap> repo, File file) throws Exception {
				loadMap(repo, file);
			}
		});
	}

	@Override
	protected Logger log() {
		return log;
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
	
	private void loadMap(BaseRepository<GameMap> repo, File file) throws Exception {
		Document doc = builder.build(file);
		
		Element root = doc.getDescendants(new ElementFilter("maps")).next();
		for (Element element : root.getChildren("map")) {
			GameMap map = factory.newGameMap();
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
			GameMap map = repo.byId(element.getAttribute("id").getIntValue());
			
			Map<Short, MapTrigger> triggers = Maps.newHashMap();
			for (Element trigger_elem : element.getChildren("trigger")) {
				MapTrigger trigger = factory.newMapTrigger();
				//trigger.setId(trigger_elem.getAttribute("id").getIntValue()); <- useless
				trigger.setMap(map);
				trigger.setCell((short) trigger_elem.getAttribute("cell").getIntValue());
				trigger.setNextMap(repo.byId(trigger_elem.getAttribute("next_map").getIntValue()));
				trigger.setNextCell((short) trigger_elem.getAttribute("next_cell").getIntValue());
				
				triggers.put(trigger.getCell(), trigger);
			}
			map.setTrigger(triggers);
		}
	}

}
