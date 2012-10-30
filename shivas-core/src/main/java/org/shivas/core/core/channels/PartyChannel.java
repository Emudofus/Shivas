package org.shivas.core.core.channels;

import org.shivas.core.core.GameActor;
import org.shivas.core.core.events.EventListener;
import org.shivas.core.core.parties.Party;
import org.shivas.core.database.models.Player;

public class PartyChannel implements Channel {

	@Override
	public void subscribe(EventListener listener) { }

	@Override
	public void unsubscribe(EventListener listener) { }

	@Override
	public void send(GameActor author, String message) {
		if (author instanceof Player) {
			Player player = (Player) author;
			Party party = player.getClient().party();
			
			party.speak(player, message);
		}
	}

}
