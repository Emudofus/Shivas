package org.shivas.game.services;

import org.shivas.common.Account;

public interface LoginService {
	void start();
	void stop();
	
	Account getAccount(String ticket);
}
