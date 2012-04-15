package org.shivas.server.services.login;

import org.shivas.common.crypto.Cipher;
import org.shivas.server.config.Config;
import org.shivas.server.services.Service;
import org.shivas.server.services.game.GameService;

public interface LoginService extends Service {

	Config config();
	GameService game();
	
	Cipher makeCipher(String ticket);
	
}
