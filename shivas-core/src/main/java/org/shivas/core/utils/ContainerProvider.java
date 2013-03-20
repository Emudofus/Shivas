package org.shivas.core.utils;

import com.google.inject.Provider;
import org.shivas.core.InjectConfig;
import org.shivas.core.core.maps.GameMap;
import org.shivas.core.core.npcs.GameNpc;
import org.shivas.data.Container;
import org.shivas.data.EntityFactory;
import org.shivas.data.Loader;
import org.shivas.data.Loaders;
import org.shivas.data.container.ProxyContainer;
import org.shivas.data.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ContainerProvider implements Provider<Container> {
	
	private static final Logger log = LoggerFactory.getLogger(ContainerProvider.class);

    @InjectConfig(key="shivas.data.extension")
    private String extension;
    
    @InjectConfig(key="shivas.data.path")
    private String path;
	
	@Inject
	private EntityFactory factory;
	
	private final ProxyContainer proxy = new ProxyContainer();
	
	public void load() {
		Loader loader = Loaders.byExtension(extension, factory);
		if (loader == null) {
			log.error("format \"{}\" isn't handled", extension);
		}
		else {
			loader.load(SpellTemplate.class, path + "spells/");
			loader.load(Breed.class, path + "breeds/");
			loader.load(Experience.class, path + "experiences/");
			loader.load(GameMap.class, path + "maps/");
			loader.load(ItemTemplate.class, path + "items/");
			loader.load(ItemSet.class, path + "itemsets/");
			loader.load(Action.class, path + "actions/");
            loader.load(NpcTemplate.class, path + "npcTemplates/");
            loader.load(GameNpc.class, path + "npcs/");
			loader.load(Waypoint.class, path + "waypoints/");

			proxy.setParent(loader.create());
		}
	}

	@Override
	public Container get() {
		return proxy;
	}

}
