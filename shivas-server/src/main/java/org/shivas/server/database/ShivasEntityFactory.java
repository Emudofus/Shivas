package org.shivas.server.database;

import javax.inject.Singleton;

import org.shivas.data.entity.factory.BaseEntityFactory;
import org.shivas.server.core.maps.GameMap;

@Singleton
public class ShivasEntityFactory extends BaseEntityFactory {

	public GameMap newMapTemplate() {
		return new GameMap();
	}
	
}
