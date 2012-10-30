package org.shivas.core.core.interactions;

import org.shivas.protocol.client.enums.TradeTypeEnum;
import org.shivas.protocol.client.formatters.TradeGameMessageFormatter;
import org.shivas.core.core.exchanges.PlayerExchange;
import org.shivas.core.services.game.GameClient;

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
		writeToAll(TradeGameMessageFormatter.tradeRequestMessage(
                source.player().getId(),
                target.player().getId(),
                TradeTypeEnum.PLAYER
        ));
	}

	@Override
	public void accept() throws InteractionException {
		source.interactions().push(new PlayerExchange(source, target)).begin();
	}

	@Override
	public void decline() throws InteractionException {
        writeToAll(TradeGameMessageFormatter.tradeQuitMessage());
	}

}
