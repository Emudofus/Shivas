package org.shivas.server.core.interactions;

import org.shivas.protocol.client.enums.TradeTypeEnum;
import org.shivas.protocol.client.formatters.TradeGameMessageFormatter;
import org.shivas.server.services.game.GameClient;

public class PlayerExchangeInvitation extends Invitation {

	public PlayerExchangeInvitation(GameClient source, GameClient target) {
		super(source, target);
	}

	@Override
	public InteractionType getInteractionType() {
		return InteractionType.PLAYER_EXCHANGE_INVITATION;
	}

	@Override
	public void begin() throws InteractionException {
		String message = TradeGameMessageFormatter.tradeRequestMessage(
				source.player().getId(), 
				target.player().getId(), 
				TradeTypeEnum.PLAYER
		);
		
		source.write(message);
		target.write(message);
	}

	@Override
	public void accept() throws InteractionException {
		// TODO exchanges
	}

	@Override
	public void decline() throws InteractionException {
		source.write(TradeGameMessageFormatter.tradeQuitMessage());
		target.write(TradeGameMessageFormatter.tradeQuitMessage());
	}

}
