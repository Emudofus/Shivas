package org.shivas.server.database;

import javax.inject.Singleton;

import org.shivas.data.entity.factory.BaseEntityFactory;
import org.shivas.server.core.maps.GMap;

@Singleton
public class ShivasEntityFactory extends BaseEntityFactory {

	@Override
	public GMap newGameMap() {
		return new GMap();
	}
	
}
