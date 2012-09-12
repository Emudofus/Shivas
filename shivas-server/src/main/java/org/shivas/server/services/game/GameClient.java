package org.shivas.server.services.game;

import org.shivas.server.core.events.EventListener;
import org.shivas.server.core.interactions.InteractionList;
import org.shivas.server.core.logging.DofusLogger;
import org.shivas.server.core.parties.Party;
import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.Client;

public interface GameClient extends Client<GameService> {
	
	void kick(String message);

	Account account();
	void setAccount(Account account);
	
	Player player();
	void setPlayer(Player player);

	boolean hasParty();
	Party party();
	void setParty(Party party);
	
	EventListener eventListener();
	
	InteractionList interactions();
	boolean isBusy();
	
	DofusLogger tchat();
	
}
