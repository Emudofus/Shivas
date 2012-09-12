package org.shivas.server.core.events.events;

import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventType;
import org.shivas.server.database.models.Player;

public class ChangeMapEvent implements Event {
	
	private final Player player;

	public ChangeMapEvent(Player player) {
		this.player = player;
	}

	@Override
	public EventType type() {
		return EventType.CHANGE_MAP;
	}

	public Player getPlayer() {
		return player;
	}

}
