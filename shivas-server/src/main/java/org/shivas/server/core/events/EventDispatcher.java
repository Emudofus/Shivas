package org.shivas.server.core.events;

public interface EventDispatcher {
	void subscribe(EventListener listener);
	void unsubscribe(EventListener listener);
	
	void publish(Event event);
}
