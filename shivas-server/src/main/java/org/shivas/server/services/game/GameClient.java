package org.shivas.server.services.game;

import org.shivas.server.database.models.Account;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.Client;

public interface GameClient extends Client<GameService> {

	Account account();
	void setAccount(Account account);
	
	Player player();
	void setPlayer(Player player);
	
}
