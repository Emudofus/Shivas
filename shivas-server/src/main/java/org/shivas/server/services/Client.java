package org.shivas.server.services;

import org.shivas.common.services.IoSessionHandler;
import org.shivas.server.database.models.Account;

public interface Client<S extends Service> {
	
	S service();
	
	Account account();
	void setAccount(Account account);
	
	void kick();
	void newHandler(IoSessionHandler<String> handler) throws Exception;
	
}
