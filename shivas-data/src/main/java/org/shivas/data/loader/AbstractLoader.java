package org.shivas.data.loader;

import java.io.File;
import java.util.Map;

import org.shivas.common.io.FileExtensions;
import org.shivas.data.Container;
import org.shivas.data.Containers;
import org.shivas.data.EntityFactory;
import org.shivas.data.Loader;
import org.shivas.data.Repository;
import org.shivas.data.container.SimpleContainer;
import org.shivas.data.repository.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public abstract class AbstractLoader implements Loader {
	
	private static final Logger log = LoggerFactory.getLogger(AbstractLoader.class);
	
	protected final SimpleContainer ctner = new SimpleContainer();
	protected final EntityFactory factory;
	
	@SuppressWarnings("rawtypes")
	protected final Map<Class, FileLoader> loaders = Maps.newHashMap();
	
	public AbstractLoader(EntityFactory factory) {
		this.factory = factory;
	}
	
	public abstract String getFileExtension();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> FileLoader<T> getLoader(Class<T> clazz) {
		for (Map.Entry<Class, FileLoader> entry : loaders.entrySet()) {
			if (entry.getKey().isAssignableFrom(clazz)) {
				return entry.getValue();
			}
		}
		return null;
	}

	@Override
	public <T> void load(Class<T> entity, String path) {
		FileLoader<T> loader = getLoader(entity);
		if (loader != null)  {
			BaseRepository<T> repo = new BaseRepository<T>(entity);
			log.debug("start load {}", repo.getEntityClass().getSimpleName());
			
			int count = loadEntities(
					repo,
					new File(path),
					loader
			);
			ctner.add(repo);
			
			log.debug("{} {} loaded", count, repo.getEntityClass().getSimpleName());
		} else {
			log.error("unknown class \"{}\"", entity.getName());
		}
	}

	@Override
	public final Container create() {
		Containers.setInstance(ctner);
		return ctner;		
	}
	
	private <T> int loadEntities(BaseRepository<T> repo, File directory, FileLoader<T> loader) {
		int total = 0;
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				total += loadEntities(repo, file, loader);
			} else if (FileExtensions.match(file, getFileExtension())) {
				try {
					total += loader.load(repo, file);
					
					log.trace("{} loaded", file.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return total;
	}
	
	protected <T> Repository<T> get(Class<T> clazz) {
		return ctner.get(clazz);
	}

}
