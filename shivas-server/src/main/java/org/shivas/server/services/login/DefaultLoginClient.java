package org.shivas.server.services.login;

import org.apache.mina.core.session.IoSession;
import org.shivas.server.database.models.Account;
import org.shivas.server.services.BaseHandler;
import org.shivas.server.services.IoSessionDecorator;

public final class DefaultLoginClient extends IoSessionDecorator implements LoginClient {
	
	private final LoginService service;
	
	private Account account;
	private String ticket;
	private BaseHandler handler;

	public DefaultLoginClient(IoSession session, LoginService service) {
		super(session);
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
		close(false);
	}
	
	public BaseHandler handler() {
		return handler;
	}

	public void newHandler(BaseHandler handler) throws Exception {
		this.handler = handler;
		this.handler.init();
	}

}
