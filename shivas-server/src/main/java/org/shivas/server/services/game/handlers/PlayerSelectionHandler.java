package org.shivas.server.services.game.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.StringUtils;
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

	public void init() throws Exception {
		client.account().setConnected(true);
		client.service().repositories().accounts().saveLater(client.account());
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
		client.service().repositories().accounts().saveLater(client.account());
	}

	private void parseRegionalVersionMessage() {
		session.write(ApproachGameMessageFormatter.
				regionalVersionResponseMessage(client.account().getCommunity()));
	}
	
	private void parsePlayersListMessage() {
		session.write(ApproachGameMessageFormatter.charactersListMessage(
				client.service().informations().getId(),
				client.account().getRemainingSubscription().getMillis(),
				Player.toBaseCharacterType(client.account().getPlayers().values())
		));
	}

	private void parseRandomNameMessage() {
		session.write(ApproachGameMessageFormatter.
				characterNameSuggestionSuccessMessage(StringUtils.randomPseudo()));
	}

	private void parsePlayerCreationMessage(String name, int breed, Gender gender, int color1, int color2, int color3) {
		if (client.account().getPlayers().size() >= client.service().config().maxPlayersPerAccount()) {
			session.write(ApproachGameMessageFormatter.accountFullMessage());
		} else if (client.service().repositories().players().nameExists(name)) {
			session.write(ApproachGameMessageFormatter.characterNameAlreadyExistsMessage());
		} else {
			client.service().repositories().players().createDefault(
					client.account(),
					name,
					breed,
					gender,
					color1,
					color2,
					color3
			);
			
			session.write(ApproachGameMessageFormatter.characterCreationSuccessMessage());
			parsePlayersListMessage();
		}
	}

	private void parsePlayerDeleteMessage(int playerId, String secretAnswer) throws CriticalException {
		Player player = client.account().getPlayers().get(playerId);
		
		if (player == null) {
			throw new CriticalException("unknown player #%d !", playerId);
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
		Player player = client.account().getPlayers().get(playerId);
		
		if (player == null) {
			throw new CriticalException("unknown player #%d !", playerId);
		} else {
			session.write(ApproachGameMessageFormatter.characterSelectionSucessMessage(
					player.id(),
					player.getName(),
					player.getExperience().level(),
					player.getBreed().getId(),
					player.getGender(),
					player.getLook().getSkin(),
					player.getLook().getColors().first(), 
					player.getLook().getColors().second(),
					player.getLook().getColors().third(),
					null // TODO items
			));
			
			client.setPlayer(player);
			
			client.newHandler(new RolePlayHandler(client, session));
		}
	}

}
