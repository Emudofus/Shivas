package org.shivas.server.core.events.events;

import org.shivas.server.core.actions.Action;
import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventType;
import org.shivas.server.services.game.GameClient;

public class NewActionEvent implements Event {
	
	private final GameClient client;
	private final Action action;

	public NewActionEvent(GameClient client, Action action) {
		this.client = client;
		this.action = action;
	}

	@Override
	public EventType type() {
		return EventType.NEW_ACTION;
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
	public Action getAction() {
		return action;
	}

}
