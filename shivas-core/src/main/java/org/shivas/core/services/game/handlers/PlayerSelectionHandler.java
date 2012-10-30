package org.shivas.core.services.game.handlers;

import org.shivas.common.StringUtils;
import org.shivas.protocol.client.enums.Gender;
import org.shivas.protocol.client.formatters.ApproachGameMessageFormatter;
import org.shivas.protocol.client.formatters.ItemGameMessageFormatter;
import org.shivas.core.database.models.Gift;
import org.shivas.core.database.models.Player;
import org.shivas.core.services.AbstractBaseHandler;
import org.shivas.core.services.CriticalException;
import org.shivas.core.services.game.GameClient;

public class PlayerSelectionHandler extends AbstractBaseHandler<GameClient> {

	public PlayerSelectionHandler(GameClient client) {
		super(client);
	}

	public void init() throws Exception {
		client.account().setConnected(true);
		client.service().repositories().accounts().save(client.account());
	}

	public void onClosed() {
		client.account().setConnected(false);
		client.service().repositories().accounts().save(client.account());
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
			
		case 'g':
			parseGiftListMessage(message.substring(2));
			break;
			
		case 'G':
			args = message.substring(2).split("\\|");
			parseChooseGiftMessage(
					Integer.parseInt(args[0]),
					Integer.parseInt(args[1])
			);
			break;
		}
	}

	private void parseRegionalVersionMessage() {
		client.write(ApproachGameMessageFormatter.
				regionalVersionResponseMessage(client.account().getCommunity()));
	}
	
	private void parsePlayersListMessage() {
		client.write(ApproachGameMessageFormatter.charactersListMessage(
				client.service().informations().getId(),
				client.account().getRemainingSubscription().getMillis(),
				client.account().getPlayers().toBaseCharacterType()
		));
	}

	private void parseRandomNameMessage() {
		client.write(ApproachGameMessageFormatter.
				characterNameSuggestionSuccessMessage(StringUtils.randomPseudo()));
	}

	private void parsePlayerCreationMessage(String name, int breed, Gender gender, int color1, int color2, int color3) {
		if (client.account().getPlayers().count() >= client.service().config().maxPlayersPerAccount()) {
			client.write(ApproachGameMessageFormatter.accountFullMessage());
		} else if (client.service().repositories().players().nameExists(name)) {
			client.write(ApproachGameMessageFormatter.characterNameAlreadyExistsMessage());
		} else {
			Player player = client.service().repositories().players().createDefault(
					name,
					breed,
					gender,
					color1,
					color2,
					color3
			);
			client.account().getPlayers().persist(player);
			
			client.write(ApproachGameMessageFormatter.characterCreationSuccessMessage());
			parsePlayersListMessage();
		}
	}

	private void parsePlayerDeleteMessage(int playerId, String secretAnswer) throws CriticalException {
		Player player = client.account().getPlayers().get(playerId);
		assertFalse(player == null, "unknown player %d !", playerId);
		assertFalse(
				secretAnswer.isEmpty() && player.getExperience().level() > client.service().config().deleteAnswerLevelNeeded(),
				"the secret answer is required"
		);
		
		if (player.getExperience().level() > client.service().config().deleteAnswerLevelNeeded() &&
				   !client.account().getSecretAnswer().equals(secretAnswer))
		{
			client.write(ApproachGameMessageFormatter.characterDeletionFailureMessage());
		} else {
			client.account().getPlayers().remove(playerId);
			client.service().repositories().players().delete(player);
			
			parsePlayersListMessage();
		}
	}

	private void parsePlayerSelectionMessage(int playerId) throws Exception {
		Player player = client.account().getPlayers().get(playerId);
		assertFalse(player == null, "unknown player %d !", playerId);
		
		client.write(ApproachGameMessageFormatter.characterSelectionSucessMessage(
				player.getPublicId(),
				player.getName(),
				player.getExperience().level(),
				player.getBreed().getId(),
				player.getGender(),
				player.getLook().skin(),
				player.getLook().colors().first(), 
				player.getLook().colors().second(),
				player.getLook().colors().third(),
				player.getBag().toBaseItemType()
		));

		client.account().setCurrentPlayer(player);
		client.setPlayer(player);
		player.setClient(client);
		
		client.newHandler(new RolePlayHandler(client));
	}

	private void parseGiftListMessage(String language) {
		for (Gift gift : client.account().getGifts().refresh()) {
			client.write(ItemGameMessageFormatter.giftMessage(gift.toBaseGiftType()));
		}
	}

	private void parseChooseGiftMessage(int giftId, int playerId) throws Exception {
		Player player = client.account().getPlayers().get(playerId);
		assertFalse(player == null, "unknown player %d !", playerId);
		Gift gift = client.account().getGifts().get(giftId);
		assertFalse(gift == null, "unknown gift %d !", giftId);
		
		player.getBag().persist(gift.getItem());
		client.account().getGifts().delete(gift);
		
		client.write(ItemGameMessageFormatter.storeGiftMessage(true));
	}

}
