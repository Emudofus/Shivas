package org.shivas.login.services.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.StringUtils;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.login.services.LoginService;
import org.shivas.protocol.client.formatters.LoginMessageFormatter;

public class VersionHandler implements IoSessionHandler<String> {
	
	public static final String SESSION_TICKET_TOKEN = "ticket";
	
	private final IoSession session;
	private final LoginService service;
	
	public VersionHandler(IoSession session, LoginService service) {
		this.session = session;
		this.service = service;
	}

	public void init() throws Exception {
		String ticket = StringUtils.random(32);
		session.setAttribute(SESSION_TICKET_TOKEN, ticket);
		
		session.write(LoginMessageFormatter.helloConnect(ticket));
	}

	public void handle(String message) throws Exception {
		if (!message.equals(service.getConfig().getClientVersion())) {
			session.write(LoginMessageFormatter.badClientVersion(service.getConfig().getClientVersion()));
			session.close(false);
		} else {
			// TODO set new handler
		}
	}

	public void onClosed() {
	}

}
