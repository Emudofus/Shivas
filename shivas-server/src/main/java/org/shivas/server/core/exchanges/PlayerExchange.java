package org.shivas.server.core.exchanges;

import org.shivas.protocol.client.enums.TradeTypeEnum;
import org.shivas.protocol.client.formatters.TradeGameMessageFormatter;
import org.shivas.server.core.interactions.AbstractInteraction;
import org.shivas.server.core.interactions.Acceptable;
import org.shivas.server.core.interactions.Declinable;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.interactions.InteractionType;
import org.shivas.server.core.interactions.LinkedInteraction;
import org.shivas.server.services.game.GameClient;

public class PlayerExchange extends AbstractInteraction implements LinkedInteraction, Acceptable, Declinable {
	
	private final GameClient source, target;

	public PlayerExchange(GameClient source, GameClient target) {
		this.source = source;
		this.target = target;
	}

	@Override
	public GameClient getSource() {
		return source;
	}

	@Override
	public GameClient getTarget() {
		return target;
	}

	@Override
	public InteractionType getInteractionType() {
		return InteractionType.PLAYER_EXCHANGE;
	}

	@Override
	public void cancel() throws InteractionException {
		decline();
	}

	@Override
	protected void internalEnd() throws InteractionException {
		// TODO exchanges
	}
	
	protected void writeToAll(String message) {
		source.write(message);
		target.write(message);
	}

	@Override
	public void begin() throws InteractionException {
		writeToAll(TradeGameMessageFormatter.startTradeMessage(TradeTypeEnum.PLAYER));
	}

	@Override
	public void decline() throws InteractionException {
		writeToAll(TradeGameMessageFormatter.tradeQuitMessage());
	}

	@Override
	public void accept() throws InteractionException {
	}

}
