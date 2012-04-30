package org.shivas.server.utils;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.data.Container;
import org.shivas.data.Loader;
import org.shivas.data.Loaders;
import org.shivas.data.entity.Breed;
import org.shivas.server.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Provider;

@Singleton
public class ContainerProvider implements Provider<Container> {
	
	private static final Logger log = LoggerFactory.getLogger(ContainerProvider.class);
	
	@Inject
	private Config config;

	@Override
	public Container get() {
		Container container;
		Loader loader = Loaders.byExtension(config.dataExtension());
		
		if (loader == null) {
			container = null;
			log.error("can't load \"{}\" data", config.dataExtension());
		} else {
			loader.load(Breed.class, config.dataPath() + "breeds/");
			
			container = loader.create();
		}
		
		return container;
	}

}
