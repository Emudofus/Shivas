package org.shivas.server.services.game.handlers;

import javax.persistence.PersistenceException;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.StringUtils;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.protocol.client.enums.Gender;
import org.shivas.protocol.client.formatters.ApproachGameMessageFormatter;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.CriticalException;
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
		assertTrue(message.charAt(0) == 'A', "invalid incoming data [%s]", message);
		
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
			args = message.substring(2).split("\\|");
			parsePlayerCreationMessage(
					args[0],
					Integer.parseInt(args[1]),
					args[2].equals("1") ? Gender.FEMALE : Gender.MALE,
					Integer.parseInt(args[3]),
					Integer.parseInt(args[4]),
					Integer.parseInt(args[5])
			);
			break;
			
		case 'D':
			args = message.substring(2).split("\\|");
			parsePlayerDeleteMessage(
					Integer.parseInt(args[0]),
					args.length > 1 ? args[1] : ""
			);
			break;
			
		case 'S':
			parsePlayerSelectionMessage(Integer.parseInt(message.substring(2)));
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

	private void parsePlayerCreationMessage(String name, int breed, Gender gender, int color1, int color2, int color3) {
		if (client.account().getPlayers().size() >= client.service().config().maxPlayersPerAccount()) {
			session.write(ApproachGameMessageFormatter.accountFullMessage());
		} else {
			Player player = client.service().repositories().players().createDefault(client.account(), name, breed, gender, color1, color2, color3);
			
			try {
				client.service().repositories().players().persist(player);
				client.service().repositories().accounts().update(client.account());
				
				session.write(ApproachGameMessageFormatter.characterCreationSuccessMessage());
				parsePlayersListMessage();
			} catch (PersistenceException e) {
				session.write(ApproachGameMessageFormatter.characterNameAlreadyExistsMessage());
			}
		}
	}

	private void parsePlayerDeleteMessage(int playerId, String secretAnswer) throws CriticalException {
		Player player = client.service().repositories().players().find(playerId);
		
		if (player == null) {
			throw new CriticalException("unknown player #%d !", playerId);
		} else if (!player.getOwner().equals(client.account())) {
			throw new CriticalException("you don't own player #%d !", playerId);
		} else if (secretAnswer.isEmpty() &&
				  player.getExperience().level() > client.service().config().deleteAnswerLevelNeeded())
		{
			throw new CriticalException("the secret answer is required");
		} else if (player.getExperience().level() > client.service().config().deleteAnswerLevelNeeded() &&
				   !client.account().getSecretAnswer().equals(secretAnswer))
		{
			session.write(ApproachGameMessageFormatter.characterDeletionFailureMessage());
		} else {
			client.account().getPlayers().remove(player);
			client.service().repositories().players().delete(player);
			
			parsePlayersListMessage();
		}
	}

	private void parsePlayerSelectionMessage(int playerId) throws Exception {
		Player player = client.service().repositories().players().find(playerId);
		
		if (player == null) {
			throw new CriticalException("unknown player #%d !", playerId);
		} else if (!player.getOwner().equals(client.account())) {
			throw new CriticalException("you don't own player #%d !", playerId);
		} else {
			session.write(ApproachGameMessageFormatter.characterSelectionSucessMessage(
					player.id(),
					player.getName(),
					player.getExperience().level(),
					player.getBreed().getId(),
					player.getGender(),
					player.getSkin(),
					player.getColors().first(), 
					player.getColors().second(),
					player.getColors().third(),
					null // TODO items
			));
			
			client.setPlayer(player);
			
			client.newHandler(new RolePlayHandler(client, session));
		}
	}

}
