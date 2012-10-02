package org.shivas.server.utils;

import com.google.inject.Provider;
import org.shivas.data.Container;
import org.shivas.data.EntityFactory;
import org.shivas.data.Loader;
import org.shivas.data.Loaders;
import org.shivas.data.container.ProxyContainer;
import org.shivas.data.entity.*;
import org.shivas.server.config.Config;
import org.shivas.server.core.maps.GameMap;
import org.shivas.server.core.npcs.GameNpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ContainerProvider implements Provider<Container> {
	
	private static final Logger log = LoggerFactory.getLogger(ContainerProvider.class);

	@Inject
	private Config config;
	
	@Inject
	private EntityFactory factory;
	
	private final ProxyContainer proxy = new ProxyContainer();
	
	public void load() {
		Loader loader = Loaders.byExtension(config.dataExtension(), factory);
		if (loader == null) {
			log.error("format \"{}\" isn't handled", config.dataExtension());
		}
		else {
			loader.load(SpellTemplate.class, config.dataPath() + "spells/");
			loader.load(Breed.class, config.dataPath() + "breeds/");
			loader.load(Experience.class, config.dataPath() + "experiences/");
			loader.load(GameMap.class, config.dataPath() + "maps/");
			loader.load(ItemTemplate.class, config.dataPath() + "items/");
			loader.load(ItemSet.class, config.dataPath() + "itemsets/");
			loader.load(Action.class, config.dataPath() + "actions/");
            loader.load(NpcTemplate.class, config.dataPath() + "npcTemplates/");
            loader.load(GameNpc.class, config.dataPath() + "npcs/");
			loader.load(Waypoint.class, config.dataPath() + "waypoints/");

			proxy.setParent(loader.create());
		}
	}

	@Override
	public Container get() {
		return proxy;
	}

}
