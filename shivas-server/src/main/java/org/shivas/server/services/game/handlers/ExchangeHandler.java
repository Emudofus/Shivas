package org.shivas.server.services.game.handlers;

import org.shivas.protocol.client.enums.TradeErrorEnum;
import org.shivas.protocol.client.enums.TradeTypeEnum;
import org.shivas.protocol.client.formatters.TradeGameMessageFormatter;
import org.shivas.server.core.interactions.Action;
import org.shivas.server.core.interactions.PlayerExchangeInvitation;
import org.shivas.server.database.models.Player;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.CriticalException;
import org.shivas.server.services.game.GameClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangeHandler extends AbstractBaseHandler<GameClient> {
	
	private static final Logger log = LoggerFactory.getLogger(ExchangeHandler.class);

	public ExchangeHandler(GameClient client) {
		super(client);
	}

	@Override
	public void init() throws Exception { }

	@Override
	public void onClosed() { }

	@Override
	public void handle(String message) throws Exception {
		String[] args;
		switch (message.charAt(1)) {
		case 'A':
			parseAcceptMessage();
			break;
		
		case 'R':
			args = message.substring(2).split("\\|");
			parseRequestMessage(
					TradeTypeEnum.valueOf(Integer.parseInt(args[0])),
					args[1].length() > 0 ? Integer.parseInt(args[1]) : null,
					args.length > 2 && args[2].length() > 0 ? Short.parseShort(args[2]) : null
			);
			break;
			
		case 'V':
			parseQuitMessage();
			break;
		}
	}

	private void parseRequestMessage(TradeTypeEnum type, Integer targetId, Short cellId) throws Exception {
		switch (type) {
		case PLAYER:
			assertTrue(targetId != null, "no id has been given");
			parsePlayerInvitationMessage(targetId.intValue());
			break;
			
		default:
			log.warn("unknown trade type {}", type.name());
			break;
		}
	}

	private void parsePlayerInvitationMessage(int targetId) throws Exception {
		Player target = client.service().repositories().players().find(targetId);
		assertTrue(target != null, "unknown player {}", targetId);
		
		if (target.getClient() == null) {
			client.write(TradeGameMessageFormatter.tradeRequestErrorMessage(TradeErrorEnum.UnknownOrOffline));
		} else if (target.getClient().isBusy()) {
			client.write(TradeGameMessageFormatter.tradeRequestErrorMessage(TradeErrorEnum.Busy));
		} else {
			client.actions().push(new PlayerExchangeInvitation(client, target.getClient())).begin();
		}
	}

	private void parseAcceptMessage() {
		// TODO exchanges
	}

	private void parseQuitMessage() throws Exception {
		Action interaction = client.actions().current();
		
		switch (interaction.actionType()) {
		case PLAYER_EXCHANGE_INVITATION:
			break;
		default:
			throw new CriticalException("you are not in a trade");
		}
		
		client.actions().remove();
		interaction.cancel();
	}

}
