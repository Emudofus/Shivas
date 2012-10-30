package org.shivas.core.core.channels;

import org.shivas.core.core.GameActor;
import org.shivas.core.core.events.EventListener;

public interface Channel {

	void subscribe(EventListener listener);
	void unsubscribe(EventListener listener);
	
	void send(GameActor author, String message);
	
}
