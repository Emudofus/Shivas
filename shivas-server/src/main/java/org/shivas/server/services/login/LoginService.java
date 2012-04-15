package org.shivas.server.services.login;

import org.shivas.common.crypto.Cipher;
import org.shivas.server.config.Config;
import org.shivas.server.services.Service;

public interface LoginService extends Service {

	Config config();
	
	Cipher makeCipher(String ticket);
	
}
