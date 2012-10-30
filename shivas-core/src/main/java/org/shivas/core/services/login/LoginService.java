package org.shivas.core.services.login;

import org.shivas.common.crypto.Cipher;
import org.shivas.core.database.models.Account;
import org.shivas.core.services.Service;
import org.shivas.core.services.game.GameService;

public interface LoginService extends Service<LoginClient> {

	GameService game();
	
	Cipher makeCipher(String ticket, Account account);
	
	void putAccount(String ticket, Account account);
	Account getAccount(String ticket);
	
}
