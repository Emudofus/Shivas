package org.shivas.server.services.login;

import org.shivas.server.services.Client;

public interface LoginClient extends Client<LoginService> {

	String ticket();
	void setTicket(String ticket);
	
}
