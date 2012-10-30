package org.shivas.core.core.events.events;

import org.shivas.core.core.events.Event;
import org.shivas.core.core.events.EventType;
import org.shivas.core.database.models.Player;

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
