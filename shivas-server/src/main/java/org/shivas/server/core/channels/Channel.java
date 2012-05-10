package org.shivas.server.core.channels;

import org.shivas.server.core.GameActor;
import org.shivas.server.core.events.EventListener;

public interface Channel {

	void subscribe(EventListener listener);
	void unsubscribe(EventListener listener);
	
	void send(GameActor author, String message);
	
}
