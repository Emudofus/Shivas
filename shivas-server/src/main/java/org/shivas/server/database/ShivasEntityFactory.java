package org.shivas.server.database;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.data.Container;
import org.shivas.data.entity.Item;
import org.shivas.data.entity.factory.AbstractEntityFactory;
import org.shivas.data.entity.factory.ActionFactory;
import org.shivas.server.config.Config;
import org.shivas.server.core.actions.ShivasActionFactory;
import org.shivas.server.core.maps.GameMap;
import org.shivas.server.database.models.GameItem;

@Singleton
public class ShivasEntityFactory extends AbstractEntityFactory {
	
	@Inject
	private Config config;
	
	@Override
	public GameMap newMapTemplate() {
		return new GameMap();
	}

	@Override
	public Item newItem() {
		return new GameItem();
	}

	@Override
	public ActionFactory newItemActionFactory(Container ctner) {
		return new ShivasActionFactory(ctner, config);
	}
	
}
