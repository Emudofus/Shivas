package org.shivas.data.loader;

import java.io.File;
import java.util.Iterator;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.shivas.common.maths.Interval;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.data.EntityFactory;
import org.shivas.data.entity.Breed;
import org.shivas.data.entity.Experience;
import org.shivas.data.entity.GameMap;
import org.shivas.data.repository.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			
			/**** TODO ****/
			for (Element levels : element.getChildren("levels")) {
				CharacteristicType type = CharacteristicType.valueOf(levels.getAttributeValue("type"));
				for (Element level : levels.getChildren("level")) {
					Interval range = Interval.parseInterval(level.getAttributeValue("range"));
					int bonus = level.getAttribute("bonus").getIntValue(),
						cost  = level.getAttribute("cost").getIntValue();
				}
			}
			
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
			
			repo.put(experience.getLevel(), experience);
		}
		
		Iterator<Experience> it = repo.all().iterator();
		Experience previous = null;
		while (it.hasNext()) {
			Experience current = it.next();
			
			if (previous != null) previous.setNext(current);
			current.setPrevious(previous);
			
			previous = current;
		}
	}
	
	private void loadMap(BaseRepository<GameMap> repo, File file) throws Exception {
		Document doc = builder.build(file);
		
		Element root = doc.getDescendants(new ElementFilter("maps")).next();
		for (Element element : root.getChildren("map")) {
			GameMap map = factory.newGameMap();
			map.setId(element.getAttribute("id").getIntValue());
			map.setDate(element.getAttribute("date").getValue());
			map.setKey(element.getAttribute("key").getValue());
			
			repo.put(map.getId(), map);
		}
	}

}
