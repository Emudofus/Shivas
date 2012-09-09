package org.shivas.server.core.parties;

import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventType;
import org.shivas.server.database.models.Player;

public class PartyEvent implements Event {
	
	private final PartyEventType partyEventType;
	private final Player player;

	public PartyEvent(PartyEventType partyEventType, Player player) {
		this.partyEventType = partyEventType;
		this.player = player;
	}

	@Override
	public EventType type() {
		return EventType.PARTY;
	}

	public PartyEventType getPartyEventType() {
		return partyEventType;
	}

	public Player getPlayer() {
		return player;
	}

}
