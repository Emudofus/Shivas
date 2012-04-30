package org.shivas.data.loader;

import java.io.File;
import java.util.Iterator;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.shivas.data.entity.Breed;
import org.shivas.data.entity.Experience;
import org.shivas.data.repository.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlLoader extends AbstractLoader {
	
	private static final Logger log = LoggerFactory.getLogger(XmlLoader.class);
	
	private final SAXBuilder builder = new SAXBuilder();
	
	public XmlLoader() {
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
	}

	@Override
	protected Logger log() {
		return log;
	}
	
	private void loadBreed(BaseRepository<Breed> repo, File file) throws Exception {		
		Document doc = builder.build(file);
		Element root = doc.getDescendants(new ElementFilter("breeds")).next();
		for (Element element : root.getChildren("breed")) {
			int id = element.getAttribute("id").getIntValue();
			
			repo.put(id, new Breed(id));
		}
	}
	
	private void loadExperience(BaseRepository<Experience> repo, File file) throws Exception {		
		Document doc = builder.build(file);
		Element root = doc.getDescendants(new ElementFilter("experiences")).next();
		for (Element element : root.getChildren("experience")) {
			short level = (short) element.getAttribute("level").getIntValue();
			long player = element.getAttribute("player").getLongValue();
			
			repo.put(level, new Experience(level, player));
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

}
