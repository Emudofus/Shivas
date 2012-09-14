package org.shivas.server.services.login;

import org.shivas.common.crypto.Cipher;
import org.shivas.server.database.models.Account;
import org.shivas.server.services.Service;
import org.shivas.server.services.game.GameService;

public interface LoginService extends Service<LoginClient> {

	GameService game();
	
	Cipher makeCipher(String ticket, Account account);
	
	void putAccount(String ticket, Account account);
	Account getAccount(String ticket);
	
}
