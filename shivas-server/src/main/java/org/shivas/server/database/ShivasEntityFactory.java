package org.shivas.server.database;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.data.entity.factory.BaseEntityFactory;
import org.shivas.server.core.maps.GMap;
import org.shivas.server.services.game.GameService;

@Singleton
public class ShivasEntityFactory extends BaseEntityFactory {
	
	@Inject
	private GameService gs;

	@Override
	public GMap newGameMap() {
		return new GMap(gs);
	}
	
}
