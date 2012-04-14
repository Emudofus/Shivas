package org.shivas.login.services;

import org.shivas.common.Account;
import org.shivas.common.services.IoSessionHandler;

public class DefaultLoginClient implements LoginClient {
	
	protected final LoginService service;
	
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

	public void account(Account account) {
		this.account = account;
	}

	public String ticket() {
		return ticket;
	}

	public void ticket(String ticket) {
		this.ticket = ticket;
	}
	
	public IoSessionHandler<String> handler() {
		return handler;
	}

	public void newHandler(IoSessionHandler<String> handler) throws Exception {
		this.handler = handler;
		this.handler.init();
	}

}
