package org.shivas.core.core.parties;

import org.shivas.core.core.events.Event;
import org.shivas.core.core.events.EventType;
import org.shivas.core.database.models.Player;

public class PartyEvent implements Event {
	
	private final PartyEventType partyEventType;
	private final Player player;

	public PartyEvent(PartyEventType partyEventType) {
		this(partyEventType, null);
	}

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
