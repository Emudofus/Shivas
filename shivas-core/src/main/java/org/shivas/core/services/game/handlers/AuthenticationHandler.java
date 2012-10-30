package org.shivas.core.services.game.handlers;

import org.shivas.protocol.client.formatters.ApproachGameMessageFormatter;
import org.shivas.core.database.models.Account;
import org.shivas.core.services.AbstractBaseHandler;
import org.shivas.core.services.game.GameClient;

public class AuthenticationHandler extends AbstractBaseHandler<GameClient> {

	public AuthenticationHandler(GameClient client) {
		super(client);
	}

	public void init() throws Exception {
		client.write(ApproachGameMessageFormatter.helloGameMessage());
	}

	public void handle(String message) throws Exception {
		assertTrue(message.startsWith("AT"), "invalid incoming data [%s]", message);
		
		String ticket = message.substring(2);
		Account account = client.service().login().getAccount(ticket);
		
		if (account == null) {
			client.write(ApproachGameMessageFormatter.authenticationFailureMessage());
			client.kick();
		} else {
			client.write(ApproachGameMessageFormatter.
					authenticationSuccessMessage(account.getCommunity()));
			
			client.setAccount(account);
			client.newHandler(new PlayerSelectionHandler(client));
		}
	}

	public void onClosed() {
	}

}
