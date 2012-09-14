package org.shivas.server.core.events;

import java.util.List;


import com.google.common.collect.Lists;

public class EventListenerContainer implements EventListener {
	
	private final List<EventListener> listeners = Lists.newArrayList();
	
	public void add(EventListener listener) {
		listeners.add(listener);
	}

	@Override
	public void listen(Event event) {
		for (EventListener listener : listeners) {
			listener.listen(event);
		}
	}

}
