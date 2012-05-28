package org.shivas.server.services.game.handlers;

import org.shivas.common.statistics.Characteristic;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.data.entity.Breed;
import org.shivas.protocol.client.formatters.ApproachGameMessageFormatter;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;

public class ApproachHandler extends AbstractBaseHandler<GameClient> {

	public ApproachHandler(GameClient client) {
		super(client);
	}

	public void init() throws Exception {
	}

	public void handle(String message) throws Exception {
		switch (message.charAt(1)){
		case 'B':
			parseBoostCharacteristicMessage(CharacteristicType.valueOf(Integer.valueOf(message.substring(2)) - 6));
			break;
		}
	}

	public void onClosed() {
	}

	private void parseBoostCharacteristicMessage(CharacteristicType charac) {
		Characteristic characteristic = client.player().getStats().get(charac);
		
		Breed.Level level = client.player().getBreed().getLevel(characteristic);
		
		if (client.player().getStats().statPoints() < level.cost()) {
			client.write(ApproachGameMessageFormatter.boostCharacteristicErrorMessage());
		} else {
			characteristic.plusBase((short) level.bonus());
			client.player().getStats().addStatPoints((short) -level.cost());
			
			client.write(client.player().getStats().packet());
		}
	}

}
