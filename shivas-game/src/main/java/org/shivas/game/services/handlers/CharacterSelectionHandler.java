package org.shivas.game.services.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.Account;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.game.services.GameService;
import org.shivas.protocol.client.formatters.ApproachGameMessageFormatter;

public class CharacterSelectionHandler implements IoSessionHandler<String> {
	
	private final IoSession session;
	private final GameService service;
	private final Account account;

	public CharacterSelectionHandler(IoSession session, GameService service, Account account) {
		this.session = session;
		this.service = service;
		this.account = account;
	}

	public IoSessionHandler<String> init() throws Exception {

		return this;
	}

	public void handle(String message) throws Exception {
		if (message.charAt(0) != 'A') {
			throw new Exception(String.format("invalid incoming data [%s]", message));
		}
		
		switch (message.charAt(1)) {
		case 'V':
			parseRegionalVersionRequestMessage();
			break;
		}
	}

	public void onClosed() {
		service.getLoginService().deconnection(account);
	}

	private void parseRegionalVersionRequestMessage() {
		session.write(ApproachGameMessageFormatter.regionalVersionResponseMessage(account.getCommunity()));
	}

}
