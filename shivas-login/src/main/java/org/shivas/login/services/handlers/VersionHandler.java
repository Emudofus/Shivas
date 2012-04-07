package org.shivas.login.services.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.StringUtils;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.login.services.LoginService;
import org.shivas.login.services.SessionTokens;
import org.shivas.protocol.client.formatters.LoginMessageFormatter;

public class VersionHandler implements IoSessionHandler<String> {
	
	private final IoSession session;
	private final LoginService service;
	
	public VersionHandler(IoSession session, LoginService service) {
		this.session = session;
		this.service = service;
	}

	public void init() throws Exception {
		String ticket = StringUtils.random(32);
		session.setAttribute(SessionTokens.TICKET, ticket);
		
		session.write(LoginMessageFormatter.helloConnect(ticket));
	}

	public void handle(String message) throws Exception {
		if (!message.equals(service.getConfig().getClientVersion())) {
			session.write(LoginMessageFormatter.badClientVersion(service.getConfig().getClientVersion()));
			session.close(false);
		} else {
			session.setAttribute(SessionTokens.HANDLER, new AuthenticationHandler(session, service));
		}
	}

	public void onClosed() {
	}

}
