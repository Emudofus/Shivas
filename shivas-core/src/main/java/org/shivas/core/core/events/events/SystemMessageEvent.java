package org.shivas.core.core.events.events;

import org.shivas.core.core.events.Event;
import org.shivas.core.core.events.EventType;

public class SystemMessageEvent implements Event {
	
	private String message;

	public SystemMessageEvent(String message) {
		this.message = message;
	}

	@Override
	public EventType type() {
		return EventType.SYSTEM_MESSAGE;
	}

	/**
	 * @return the message
	 */
	public String message() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
