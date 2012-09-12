package org.shivas.server.core.events.events;

import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventType;
import org.shivas.server.core.interactions.Interaction;
import org.shivas.server.services.game.GameClient;

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
