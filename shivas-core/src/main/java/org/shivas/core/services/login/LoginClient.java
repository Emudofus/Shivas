package org.shivas.core.services.login;

import org.shivas.core.services.Client;

public interface LoginClient extends Client<LoginService> {

	String ticket();
	void setTicket(String ticket);
	
}
