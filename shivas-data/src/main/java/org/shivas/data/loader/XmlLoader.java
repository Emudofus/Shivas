package org.shivas.data.loader;

import java.io.File;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.SAXBuilder;
import org.shivas.common.io.FileExtensions;
import org.shivas.data.Container;
import org.shivas.data.Loader;
import org.shivas.data.container.SimpleContainer;
import org.shivas.data.entity.Breed;
import org.shivas.data.repository.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlLoader implements Loader {
	
	private static final Logger log = LoggerFactory.getLogger(XmlLoader.class);
	
	private SimpleContainer ctner;
	private SAXBuilder builder = new SAXBuilder();

	@Override
	public <T> void load(Class<T> entity, String path) {
		if (entity.isAssignableFrom(Breed.class)) {
			loadBreeds(new BaseRepository<Breed>(Breed.class), new File(path));
		}
		else {
			log.error("unknown class \"{}\"", entity.getName());
		}
	}

	@Override
	public Container create() {
		return ctner;
	}
	
	private void loadBreeds(BaseRepository<Breed> repo, File directory) {		
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				loadBreeds(repo, file);
			} else if (FileExtensions.match(file, "xml")) {
				try {
					loadBreed(repo, file);
				} catch (Exception e) {
					log.error(String.format("can't load \"%s\" because of %s : %s",
							file.getName(),
							e.getClass().getSimpleName(),
							e.getMessage()
					));
				}
			}
		}
		
		ctner.add(repo);
	}
	
	private void loadBreed(BaseRepository<Breed> repo, File file) throws Exception {		
		Document doc = builder.build(file);
		Element root = doc.getDescendants(new ElementFilter("breeds")).next();
		for (Element element : root.getChildren("breed")) {
			int id = element.getAttribute("id").getIntValue();
			
			repo.put(id, new Breed(id));
		}
	}

}
