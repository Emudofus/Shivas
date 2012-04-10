package org.shivas.login.services.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.StringUtils;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.login.services.LoginService;
import org.shivas.protocol.client.formatters.LoginMessageFormatter;

import static org.shivas.login.services.SessionTokens.*;

public class VersionHandler implements IoSessionHandler<String> {
	
	private final IoSession session;
	private final LoginService service;
	
	public VersionHandler(IoSession session, LoginService service) {
		this.session = session;
		this.service = service;
	}

	public IoSessionHandler<String> init() throws Exception {
		String ticket = ticket(session, StringUtils.random(32));
		
		session.write(LoginMessageFormatter.helloConnect(ticket));
		
		return this;
	}

	public void handle(String message) throws Exception {
		if (!message.equals(service.getConfig().getClientVersion())) {
			session.write(LoginMessageFormatter.badClientVersion(service.getConfig().getClientVersion()));
			session.close(true);
		} else {
			handler(session, new AuthenticationHandler(session, service)).init();
		}
	}

	public void onClosed() {
	}

}
