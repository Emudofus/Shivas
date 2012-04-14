package org.shivas.login.services;

import org.shivas.common.Account;
import org.shivas.common.services.IoSessionHandler;

public interface LoginClient {
	LoginService service();
	
	Account account();
	void account(Account account);
	
	String ticket();
	void ticket(String ticket);
	
	void newHandler(IoSessionHandler<String> handler) throws Exception;
}
