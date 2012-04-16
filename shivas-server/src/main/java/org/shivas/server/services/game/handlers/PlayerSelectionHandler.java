package org.shivas.server.services.game.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.protocol.client.formatters.ApproachGameMessageFormatter;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;

public class PlayerSelectionHandler extends AbstractBaseHandler<GameClient> {

	public PlayerSelectionHandler(GameClient client, IoSession session) {
		super(client, session);
	}

	public IoSessionHandler<String> init() throws Exception {
		client.account().setConnected(true);
		client.service().repositories().accounts().update(client.account());
		
		return this;
	}

	public void handle(String message) throws Exception {
		if (message.charAt(0) != 'A') {
			throw new Exception(String.format("invalid incoming data [%s]", message));
		}
		
		switch (message.charAt(1)) {
		case 'V':
			parseRegionalVersionMessage();
			break;
			
		case 'L':
			parsePlayersListMessage();
			break;
		}
	}

	public void onClosed() {
		client.account().setConnected(false);
		client.service().repositories().accounts().update(client.account());
	}

	private void parseRegionalVersionMessage() {
		session.write(ApproachGameMessageFormatter.
				regionalVersionResponseMessage(client.account().getCommunity()));
	}
	
	private void parsePlayersListMessage() {
		session.write(ApproachGameMessageFormatter.charactersListMessage(
				client.service().informations().getId(),
				client.account().getRemainingSubscription().getMillis(),
				Player.toBaseCharacterType(client.account().getPlayers())
		));
	}

}
