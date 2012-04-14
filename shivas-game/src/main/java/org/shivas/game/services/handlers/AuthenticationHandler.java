package org.shivas.game.services.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.Account;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.game.services.GameClient;
import org.shivas.protocol.client.formatters.ApproachGameMessageFormatter;

public class AuthenticationHandler extends AbstractBaseHandler {

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
		
		parseAuthenticationRequestMessage(message.substring(2));
	}

	private void parseAuthenticationRequestMessage(String ticket) throws Exception {
		Account account = client.service().getLoginService().getAccount(ticket);
		
		if (account == null) {
			session.write(ApproachGameMessageFormatter.authenticationFailureMessage());
			session.close(false);
		} else {
			session.write(ApproachGameMessageFormatter.authenticationSuccessMessage(account.getCommunity()));
			
			client.newHandler(new CharacterSelectionHandler(client, session));
		}
	}

	public void onClosed() {
	}

}
