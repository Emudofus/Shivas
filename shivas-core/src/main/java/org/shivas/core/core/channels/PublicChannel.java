package org.shivas.core.core.channels;

import org.shivas.protocol.client.enums.ChannelEnum;
import org.shivas.core.core.GameActor;
import org.shivas.core.core.events.EventDispatcher;
import org.shivas.core.core.events.EventDispatchers;
import org.shivas.core.core.events.EventListener;

public class PublicChannel implements Channel {

	private final ChannelEnum channel;
	private final EventDispatcher event = EventDispatchers.create();

	public PublicChannel(ChannelEnum channel) {
		this.channel = channel;
	}

	public void subscribe(EventListener listener) {
		event.subscribe(listener);
	}

	public void unsubscribe(EventListener listener) {
		event.unsubscribe(listener);
	}

	public void send(GameActor author, String message) {
		event.publish(new ChannelEvent(channel, author, message));
	}

}
