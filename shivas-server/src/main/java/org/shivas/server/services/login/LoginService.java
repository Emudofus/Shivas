package org.shivas.server.services.login;

import org.shivas.common.crypto.Cipher;
import org.shivas.server.services.Service;

public interface LoginService extends Service {

	Cipher makeCipher(String ticket);
	
}
