package org.shivas.server.database;

import javax.inject.Singleton;

import org.shivas.data.entity.factory.BaseEntityFactory;
import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.maps.GMap;

import com.google.inject.Provider;

@Singleton
public class ShivasEntityFactory extends BaseEntityFactory {

	private Provider<EventDispatcher> eventDispatchers;

	public GMap newGameMap() {
		return new GMap(eventDispatchers.get());
	}
	
}
