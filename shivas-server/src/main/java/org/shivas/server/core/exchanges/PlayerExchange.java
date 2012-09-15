package org.shivas.server.core.exchanges;

import java.security.InvalidParameterException;
import java.util.Map;

import org.shivas.protocol.client.enums.TradeTypeEnum;
import org.shivas.protocol.client.formatters.TradeGameMessageFormatter;
import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.EventDispatchers;
import org.shivas.server.core.interactions.AbstractInteraction;
import org.shivas.server.core.interactions.Acceptable;
import org.shivas.server.core.interactions.Declinable;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.interactions.InteractionType;
import org.shivas.server.core.interactions.LinkedInteraction;
import org.shivas.server.core.items.ExchangeBag;
import org.shivas.server.services.game.GameClient;

import com.google.common.collect.Maps;

public class PlayerExchange extends AbstractInteraction implements LinkedInteraction, Acceptable, Declinable {
	
	private final GameClient source, target;
	private final Map<GameClient, ExchangeBag> bags = Maps.newIdentityHashMap();
	
	private final EventDispatcher event = EventDispatchers.create();

	public PlayerExchange(GameClient source, GameClient target) {
		this.source = source;
		this.target = target;
		
		bags.put(source, new ExchangeBag(source));
		bags.put(target, new ExchangeBag(target));
	}
	
	protected GameClient other(GameClient client) {
		if (client == source) return target;
		else if (client == target) return source;
		throw new InvalidParameterException("client is neither source nor target");
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
		
		event.subscribe(source.eventListener());
		event.subscribe(target.eventListener());
	}

	@Override
	public void decline() throws InteractionException {
		writeToAll(TradeGameMessageFormatter.tradeQuitMessage());
		
		event.unsubscribe(source.eventListener());
		event.unsubscribe(target.eventListener());
	}

	@Override
	public void accept() throws InteractionException {
	}
	
	public void setKamas(GameClient owner, long kamas) throws InteractionException {
		ExchangeBag bag = bags.get(owner);
		if (bag == null) throw new InteractionException("unknown GameClient");
		
		if (kamas > owner.player().getBag().getKamas()) {
			throw new InteractionException("you have not enough kamas to perform this request");
		}
		
		bag.setKamas(kamas);
		
		owner.write(TradeGameMessageFormatter.tradeLocalSetKamasMessage(kamas));
		other(owner).write(TradeGameMessageFormatter.tradeRemoteSetKamasMessage(kamas));
	}

}
