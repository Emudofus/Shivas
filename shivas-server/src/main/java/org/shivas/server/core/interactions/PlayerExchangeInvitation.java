package org.shivas.server.core.interactions;

import org.shivas.protocol.client.enums.TradeTypeEnum;
import org.shivas.protocol.client.formatters.TradeGameMessageFormatter;
import org.shivas.server.services.game.GameClient;

public class PlayerExchangeInvitation extends Invitation {

	public PlayerExchangeInvitation(GameClient source, GameClient target) {
		super(source, target);
	}

	@Override
	public ActionType actionType() {
		return ActionType.PLAYER_EXCHANGE_INVITATION;
	}

	@Override
	public void begin() throws ActionException {
		String message = TradeGameMessageFormatter.tradeRequestMessage(
				source.player().getId(), 
				target.player().getId(), 
				TradeTypeEnum.PLAYER
		);
		
		source.write(message);
		target.write(message);
	}

	@Override
	public void accept() throws ActionException {
		// TODO exchanges
	}

	@Override
	public void decline() throws ActionException {
		source.write(TradeGameMessageFormatter.tradeQuitMessage());
		target.write(TradeGameMessageFormatter.tradeQuitMessage());
	}

}
