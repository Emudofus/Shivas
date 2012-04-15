package org.shivas.server.services.game.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.protocol.client.formatters.ApproachGameMessageFormatter;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;

public class AuthenticationHandler extends AbstractBaseHandler<GameClient> {

	public AuthenticationHandler(GameClient client, IoSession session) {
		super(client, session);
	}

	public IoSessionHandler<String> init() throws Exception {
		session.write(ApproachGameMessageFormatter.helloGameMessage());

		return this;
	}

	public void handle(String message) throws Exception {
		if (!message.startsWith("AT")) {
			throw new Exception(String.format("invalid incoming data [%s]", message));
		}
		
		String ticket = message.substring(2);
	}

	public void onClosed() {
	}

}
