package org.shivas.server.database;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import org.shivas.data.entity.factory.BaseEntityFactory;
import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.ThreadedEventDispatcher;
import org.shivas.server.core.maps.GMap;

@Singleton
public class ShivasEntityFactory extends BaseEntityFactory {
	
	private ExecutorService worker = Executors.newSingleThreadExecutor();
	
	private EventDispatcher newEventDispatcher() {
		return new ThreadedEventDispatcher(worker);
	}

	public GMap newGameMap() {
		return new GMap(newEventDispatcher());
	}
	
}
