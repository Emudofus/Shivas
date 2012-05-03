package org.shivas.server.utils;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.data.Container;
import org.shivas.data.EntityFactory;
import org.shivas.data.Loader;
import org.shivas.data.Loaders;
import org.shivas.data.entity.Breed;
import org.shivas.data.entity.Experience;
import org.shivas.data.entity.GameMap;
import org.shivas.server.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Provider;

@Singleton
public class ContainerProvider implements Provider<Container> {
	
	private static final Logger log = LoggerFactory.getLogger(ContainerProvider.class);
	
	private Container ctner;
	private boolean loaded;

	@Inject
	private Config config;
	
	@Inject
	private EntityFactory factory;
	
	private void load() {
		Loader loader = Loaders.byExtension(config.dataExtension(), factory);
		if (loader == null) {
			log.error("format \"{}\" isn't handled", config.dataExtension());
		}
		else {
			loader.load(Breed.class, config.dataPath() + "breeds/");
			loader.load(Experience.class, config.dataPath() + "experiences/");
			loader.load(GameMap.class, config.dataPath() + "maps/");
			
			ctner = loader.create();
		}
		loaded = true;
	}

	@Override
	public Container get() {
		if (!loaded) load();
		return ctner;
	}

}
