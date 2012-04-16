package org.shivas.server.services.game.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.StringUtils;
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
		
		String[] args;
		
		switch (message.charAt(1)) {
		case 'V':
			parseRegionalVersionMessage();
			break;
			
		case 'L':
			parsePlayersListMessage();
			break;
			
		case 'P':
			parseRandomNameMessage();
			break;
			
		case 'A':
			args = message.split("\\|");
			parsePlayerCreationMessage(
					args[0],
					Integer.parseInt(args[1]), 
					args[2].equals("1"),
					Integer.parseInt(args[3]),
					Integer.parseInt(args[4]),
					Integer.parseInt(args[5])
			);
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

	private void parseRandomNameMessage() {
		session.write(ApproachGameMessageFormatter.
				characterNameSuggestionSuccessMessage(StringUtils.randomPseudo()));
	}

	private void parsePlayerCreationMessage(String name, int breed, boolean gender, int color1, int color2, int color3) {
		if (client.account().getPlayers().size() >= client.service().config().maxPlayersPerAccount()) {
			session.write(ApproachGameMessageFormatter.accountFullMessage());
		} else {
			Player player = client.service().repositories().players().createDefault(client.account(), name, breed, gender, color1, color2, color3);
			
			try {
				client.service().repositories().players().persist(player);
				
				session.write(ApproachGameMessageFormatter.characterCreationSuccessMessage());
				parsePlayersListMessage();
			} catch (Exception e) {
				session.write(ApproachGameMessageFormatter.characterNameAlreadyExistsMessage());
			}
		}
	}

}
