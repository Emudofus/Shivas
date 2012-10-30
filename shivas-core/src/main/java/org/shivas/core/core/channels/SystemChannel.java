package org.shivas.core.core.channels;

import org.shivas.core.core.GameActor;
import org.shivas.core.core.events.EventDispatcher;
import org.shivas.core.core.events.EventDispatchers;
import org.shivas.core.core.events.EventListener;
import org.shivas.core.core.events.events.SystemMessageEvent;

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
		event.publish(new SystemMessageEvent(String.format(
				messageFormatter,
				author != null ? author.getName() : "Admin",
				message
		)));
	}

}
