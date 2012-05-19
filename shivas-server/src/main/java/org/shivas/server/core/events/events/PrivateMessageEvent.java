package org.shivas.server.core.events.events;

import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventType;
import org.shivas.server.database.models.Player;

public class PrivateMessageEvent implements Event {

	private Player source, target;
	private String message;
	
	public EventType type() {
		return EventType.PRIVATE_MESSAGE;
	}

	public PrivateMessageEvent(Player source, Player target, String message) {
		this.source = source;
		this.target = target;
		this.message = message;
	}

	/**
	 * @return the source
	 */
	public Player source() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(Player source) {
		this.source = source;
	}

	/**
	 * @return the target
	 */
	public Player target() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(Player target) {
		this.target = target;
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
