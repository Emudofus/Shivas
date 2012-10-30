package org.shivas.core.services.game;

import org.shivas.core.core.events.EventListenerContainer;
import org.shivas.core.core.interactions.InteractionList;
import org.shivas.core.core.logging.DofusLogger;
import org.shivas.core.core.parties.Party;
import org.shivas.core.database.models.Account;
import org.shivas.core.database.models.Player;
import org.shivas.core.services.Client;

public interface GameClient extends Client<GameService> {
	
	void kick(String message);

	Account account();
	void setAccount(Account account);
	
	Player player();
	void setPlayer(Player player);

	boolean hasParty();
	Party party();
	void setParty(Party party);
	
	EventListenerContainer eventListener();
	
	InteractionList interactions();
	boolean isBusy();
	
	DofusLogger tchat();
	
}
