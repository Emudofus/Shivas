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
	
	private static String getError(Account account) {
		return account == null || account.isBanned() || account.isConnected() ? 
				ApproachGameMessageFormatter.authenticationFailureMessage() : 
				null;
	}

	public void handle(String message) throws Exception {
		if (!message.startsWith("AT")) {
			throw new Exception(String.format("invalid incoming data [%s]", message));
		}
		
		String ticket = message.substring(2);
		Account account = client.service().login().getAccount(ticket);
		String error = getError(account);
		
		if (error != null) {
			session.write(error);
			kick();
		} else {
			session.write(ApproachGameMessageFormatter.authenticationSuccessMessage(account.getCommunity()));

			client.setAccount(account);
			client.newHandler(new PlayerSelectionHandler(client, session));
		}
	}

	public void onClosed() {
	}

}
