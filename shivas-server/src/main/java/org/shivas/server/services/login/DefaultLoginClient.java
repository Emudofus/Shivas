package org.shivas.server.services.login;

import org.shivas.common.services.IoSessionHandler;
import org.shivas.server.database.models.Account;

public final class DefaultLoginClient implements LoginClient {
	
	private final LoginService service;
	
	private Account account;
	private String ticket;
	private IoSessionHandler<String> handler;

	public DefaultLoginClient(LoginService service) {
		this.service = service;
	}

	public LoginService service() {
		return service;
	}

	public Account account() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String ticket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public void kick() {
		// TODO
	}

	public void newHandler(IoSessionHandler<String> handler) throws Exception {
		this.handler = handler;
		this.handler.init();
	}

}
