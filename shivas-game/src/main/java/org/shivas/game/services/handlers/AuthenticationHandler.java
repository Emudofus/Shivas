package org.shivas.game.services.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.Account;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.game.services.GameService;
import org.shivas.protocol.client.formatters.ApproachGameMessageFormatter;

import static org.shivas.game.services.SessionTokens.*;

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
		if (!message.startsWith("AT")) {
			throw new Exception(String.format("invalid incoming data [%s]", message));
		}
		
		parseAuthenticationRequestMessage(message.substring(2));
	}

	private void parseAuthenticationRequestMessage(String ticket) throws Exception {
		Account account = service.getLoginService().getAccount(ticket);
		
		if (account == null) {
			session.write(ApproachGameMessageFormatter.authenticationFailureMessage());
			session.close(false);
		} else {
			session.write(ApproachGameMessageFormatter.authenticationSuccessMessage(account.getCommunity()));
			
			handler(session, new CharacterSelectionHandler(session, service, account)).init();
		}
	}

	public void onClosed() {
	}

}
