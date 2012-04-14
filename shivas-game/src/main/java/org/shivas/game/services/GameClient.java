package org.shivas.game.services;

import org.shivas.common.Account;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.game.database.models.Player;

public interface GameClient {
	GameService service();
	
	Account account();
	void account(Account account);
	
	Player player();
	void player(Player player);
	
	void newHandler(IoSessionHandler<String> handler) throws Exception;
}
