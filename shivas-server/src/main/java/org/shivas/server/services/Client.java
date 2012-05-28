package org.shivas.server.services;

import org.apache.mina.core.session.IoSession;
import org.shivas.server.database.models.Account;

public interface Client<S extends Service> extends IoSession {
	
	S service();
	
	Account account();
	void setAccount(Account account);
	
	void kick();
	void newHandler(BaseHandler handler) throws Exception;
	
}
