package org.shivas.server.core.channels;

import org.shivas.protocol.client.enums.ChannelEnum;
import org.shivas.server.core.GameActor;
import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.EventListener;

public class PublicChannel implements Channel {

	private final ChannelEnum channel;
	private final EventDispatcher event;

	public PublicChannel(ChannelEnum channel, EventDispatcher event) {
		this.channel = channel;
		this.event = event;
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
