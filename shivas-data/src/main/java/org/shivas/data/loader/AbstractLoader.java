package org.shivas.data.loader;

import java.io.File;
import java.util.Map;

import org.shivas.common.io.FileExtensions;
import org.shivas.data.Container;
import org.shivas.data.Containers;
import org.shivas.data.Loader;
import org.shivas.data.container.SimpleContainer;
import org.shivas.data.repository.BaseRepository;
import org.slf4j.Logger;

import com.google.common.collect.Maps;

public abstract class AbstractLoader implements Loader {
	
	protected final SimpleContainer ctner = new SimpleContainer();
	
	protected final Map<Class, FileLoader> loaders = Maps.newHashMap();
	
	protected abstract Logger log();

	@Override
	public <T> void load(Class<T> entity, String path) {
		FileLoader<T> loader = loaders.get(entity);
		if (loader != null)  {
			loadEntities(
					new BaseRepository<T>(entity),
					new File(path),
					loader
			);
		} else {
			log().error("unknown class \"{}\"", entity.getName());
		}
	}

	@Override
	public final Container create() {
		Containers.setInstance(ctner);
		return ctner;		
	}
	
	private <T> void loadEntities(BaseRepository<T> repo, File directory, FileLoader<T> loader) {
		log().debug("start load {}", repo.getEntityClass().getSimpleName());
		
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				loadEntities(repo, directory, loader);
			} else if (FileExtensions.match(file, "xml")) {
				try {
					loader.load(repo, file);
				} catch (Exception e) {
					log().error(String.format("can't load \"%s\" because of %s : %s",
							file.getName(),
							e.getClass().getSimpleName(),
							e.getMessage()
					));
				}
			}
		}
		
		log().debug("{} {} loaded", repo.count(), repo.getEntityClass().getSimpleName());
	}

}
