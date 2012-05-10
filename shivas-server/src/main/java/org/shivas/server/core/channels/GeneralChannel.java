package org.shivas.server.core.channels;

import org.shivas.protocol.client.enums.ChannelEnum;
import org.shivas.server.core.GameActor;
import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.EventListener;

public class GeneralChannel implements Channel {
	
	public static final ChannelEnum CHANNEL_ENUM = ChannelEnum.General;

	public void subscribe(EventListener listener) {
	}

	public void unsubscribe(EventListener listener) {
	}

	public void send(GameActor author, String message) {
		EventDispatcher event = author.getLocation().getMap().event();

		event.publish(new ChannelEvent(CHANNEL_ENUM, author, message));
	}

}
