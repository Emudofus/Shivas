package org.shivas.server.database;

import javax.inject.Singleton;

import org.shivas.data.entity.Item;
import org.shivas.data.entity.factory.AbstractEntityFactory;
import org.shivas.server.core.maps.GameMap;

@Singleton
public class ShivasEntityFactory extends AbstractEntityFactory {

	public GameMap newMapTemplate() {
		return new GameMap();
	}

	@Override
	public Item newItem() {
		return null; // TODO create Item model
	}
	
}
