package org.shivas.server.database;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.data.entity.Item;
import org.shivas.data.entity.factory.AbstractEntityFactory;
import org.shivas.data.entity.factory.ItemActionFactory;
import org.shivas.server.core.maps.GameMap;
import org.shivas.server.database.models.GameItem;

@Singleton
public class ShivasEntityFactory extends AbstractEntityFactory {
	
	@Inject
	private ItemActionFactory itemActions;
	
	@Override
	public GameMap newMapTemplate() {
		return new GameMap();
	}

	@Override
	public Item newItem() {
		return new GameItem();
	}

	@Override
	public ItemActionFactory newItemActionFactory() {
		return itemActions;
	}
	
}
