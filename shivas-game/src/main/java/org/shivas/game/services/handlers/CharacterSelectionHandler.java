package org.shivas.game.services.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.game.services.GameClient;
import org.shivas.protocol.client.formatters.ApproachGameMessageFormatter;

public class CharacterSelectionHandler extends AbstractBaseHandler {

	public CharacterSelectionHandler(GameClient client, IoSession session) {
		super(client, session);
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
		client.service().getLoginService().deconnection(client.account());
	}

	private void parseRegionalVersionRequestMessage() {
		session.write(ApproachGameMessageFormatter.regionalVersionResponseMessage(client.account().getCommunity()));
	}

}
