package org.shivas.core.core.events.events;

import org.shivas.core.core.events.Event;
import org.shivas.core.core.events.EventType;
import org.shivas.core.database.models.Account;

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
