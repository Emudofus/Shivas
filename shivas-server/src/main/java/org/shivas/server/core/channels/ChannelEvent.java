package org.shivas.server.core.channels;

import org.shivas.protocol.client.enums.ChannelEnum;
import org.shivas.server.core.GameActor;
import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventType;

public class ChannelEvent implements Event {
	
	private ChannelEnum channel;
	private GameActor author;
	private String message;
	
	public ChannelEvent() {
	}

	public ChannelEvent(ChannelEnum channel, GameActor author, String message) {
		this.channel = channel;
		this.author = author;
		this.message = message;
	}

	public EventType type() {
		return EventType.CHANNEL;
	}

	public ChannelEnum channel() {
		return channel;
	}

	public void setChannel(ChannelEnum channel) {
		this.channel = channel;
	}

	public GameActor author() {
		return author;
	}

	public void setAuthor(GameActor author) {
		this.author = author;
	}

	public String message() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
