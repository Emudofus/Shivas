package org.shivas.server.core.channels;

import org.shivas.server.core.GameActor;
import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.EventDispatchers;
import org.shivas.server.core.events.EventListener;
import org.shivas.server.core.events.events.SystemMessageEvent;

public class SystemChannel implements Channel {
	
	private final EventDispatcher event = EventDispatchers.create();
	private final String messageFormatter;

	public SystemChannel(String messageFormatter) {
		this.messageFormatter = messageFormatter;
	}

	@Override
	public void subscribe(EventListener listener) {
		event.subscribe(listener);
	}

	@Override
	public void unsubscribe(EventListener listener) {
		event.subscribe(listener);
	}

	@Override
	public void send(GameActor author, String message) {
		event.publish(new SystemMessageEvent(String.format(messageFormatter, author.getName(), message)));
	}

}
