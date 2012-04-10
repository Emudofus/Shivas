package org.shivas.game.services.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.game.services.GameService;
import org.shivas.protocol.client.formatters.ApproachGameMessageFormatter;

public class AuthenticationHandler implements IoSessionHandler<String> {
	
	private final IoSession session;
	private final GameService service;

	public AuthenticationHandler(IoSession session, GameService service) {
		this.session = session;
		this.service = service;
	}

	public IoSessionHandler<String> init() throws Exception {
		session.write(ApproachGameMessageFormatter.helloGameMessage());
		
		return this;
	}

	public void handle(String message) throws Exception {
	}

	public void onClosed() {
	}

}
