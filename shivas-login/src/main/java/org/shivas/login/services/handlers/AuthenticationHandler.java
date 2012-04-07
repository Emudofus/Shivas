package org.shivas.login.services.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.login.services.LoginService;

public class AuthenticationHandler implements IoSessionHandler<String> {
	
	private final IoSession session;
	private final LoginService service;

	public AuthenticationHandler(IoSession session, LoginService service) {
		this.session = session;
		this.service = service;
	}

	public void init() throws Exception {
	}

	public void handle(String message) throws Exception {
	}

	public void onClosed() {
	}

}
