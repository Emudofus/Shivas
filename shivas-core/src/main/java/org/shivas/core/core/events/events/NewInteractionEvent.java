package org.shivas.core.core.events.events;

import org.shivas.core.core.events.Event;
import org.shivas.core.core.events.EventType;
import org.shivas.core.core.interactions.Interaction;
import org.shivas.core.services.game.GameClient;

public class NewInteractionEvent implements Event {
	
	private final GameClient client;
	private final Interaction action;

	public NewInteractionEvent(GameClient client, Interaction action) {
		this.client = client;
		this.action = action;
	}

	@Override
	public EventType type() {
		return EventType.NEW_INTERACTION;
	}

	/**
	 * @return the client
	 */
	public GameClient getClient() {
		return client;
	}

	/**
	 * @return the action
	 */
	public Interaction getAction() {
		return action;
	}

}
