package org.shivas.server.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.ThreadedEventDispatcher;

import com.google.inject.Provider;

@Singleton
public class EventDispatcherProvider implements Provider<EventDispatcher> {
	
	private ExecutorService worker = Executors.newSingleThreadExecutor();

	public EventDispatcher get() {
		return new ThreadedEventDispatcher(worker);
	}

}
