package org.shivas.server.core.events.events;

import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventType;
import org.shivas.server.database.models.Account;

public class FriendConnectionEvent implements Event {
	
	private final Account friend;

	public FriendConnectionEvent(Account friend) {
		this.friend = friend;
	}

	@Override
	public EventType type() {
		return EventType.FRIEND_CONNECTION;
	}

	public Account getFriend() {
		return friend;
	}

}
