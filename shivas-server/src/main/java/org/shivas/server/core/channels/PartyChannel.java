package org.shivas.server.core.channels;

import org.shivas.server.core.GameActor;
import org.shivas.server.core.events.EventListener;
import org.shivas.server.core.parties.Party;
import org.shivas.server.database.models.Player;

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
