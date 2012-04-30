package org.shivas.server.services.game.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.protocol.client.formatters.ApproachGameMessageFormatter;
import org.shivas.server.database.models.Account;
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
		assertTrue(message.startsWith("AT"), "invalid incoming data [%s]", message);
		
		String ticket = message.substring(2);
		Account account = client.service().login().getAccount(ticket);
		
		if (account == null) {
			session.write(ApproachGameMessageFormatter.authenticationFailureMessage());
			kick();
		} else {
			session.write(ApproachGameMessageFormatter.
					authenticationSuccessMessage(account.getCommunity()));
			
			client.setAccount(account);
			client.newHandler(new PlayerSelectionHandler(client, session));
		}
	}

	public void onClosed() {
	}

}
