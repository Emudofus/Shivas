package org.shivas.server.core.events;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class EventDispatchers {
	private EventDispatchers(){}
	
	private static final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	public static EventDispatcher create(){
		return new ThreadedEventDispatcher(executor);
	}
}
