package org.shivas.server.services.game.handlers;

import org.shivas.protocol.client.enums.TradeErrorEnum;
import org.shivas.protocol.client.enums.TradeTypeEnum;
import org.shivas.protocol.client.formatters.TradeGameMessageFormatter;
import org.shivas.server.core.exchanges.PlayerExchange;
import org.shivas.server.core.interactions.*;
import org.shivas.server.core.stores.StoreManagementInteraction;
import org.shivas.server.database.models.GameItem;
import org.shivas.server.database.models.Player;
import org.shivas.server.database.models.StoredItem;
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

        case 'K':
            parseReadyMessage();
            break;
			
		case 'M':
			switch (message.charAt(2)) {
			case 'G':
				parseSetKamasMessage(Math.max(Long.parseLong(message.substring(3)), 0));
				break;
				
			case 'O':
                args = message.substring(4).split("\\|");
                long itemId = Long.parseLong(args[0]);
                int quantity = Math.max(Integer.parseInt(args[1]), 0);

                switch (message.charAt(3)) {
                case '+':
                    switch (client.interactions().current().getInteractionType()) {
                    case PLAYER_EXCHANGE:
                        parsePlayerExchangeAddItemMessage(itemId, quantity);
                        break;
                    case STORE_MANAGEMENT:
                        if (quantity > 0) parseStoreManagementAddMessage(itemId, quantity, Long.parseLong(args[2]));
                        else parseStoreManagementUpdateMessage(itemId, Long.parseLong(args[2]));
                        break;
                    }
                    break;
                case '-':
                    switch (client.interactions().current().getInteractionType()) {
                    case PLAYER_EXCHANGE:
                        parsePlayerExchangeRemoveItemMessage(itemId, quantity);
                        break;
                    case STORE_MANAGEMENT:
                        parseStoreManagementRemoveMessage(itemId);
                        break;
                    }
                    break;
                }
				break;
			}
			break;
		
		case 'R':
			args = message.substring(2).split("\\|");
			parseRequestMessage(
					TradeTypeEnum.valueOf(Integer.parseInt(args[0])),
					args.length > 1 && args[1].length() > 0 ? Integer.parseInt(args[1]) : null,
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
			parsePlayerInvitationMessage(targetId);
			break;

        case STORE_MANAGEMENT:
            parseStoreManagementMessage();
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
			client.interactions().push(new PlayerExchangeInvitation(client, target.getClient())).begin();
		}
	}

	private void parseAcceptMessage() throws Exception {
        client.interactions().remove(PlayerExchangeInvitation.class).accept();
	}

	private void parseQuitMessage() throws Exception {
        client.interactions().remove().cancel();
	}

	private void parseSetKamasMessage(long kamas) throws Exception {
		client.interactions().current(PlayerExchange.class).setKamas(client, kamas);
	}

    private void parsePlayerExchangeAddItemMessage(long itemId, int quantity) throws CriticalException, InteractionException {
        GameItem item = client.player().getBag().get(itemId);
        assertFalse(item == null, "unknown item %d", itemId);

        client.interactions().current(PlayerExchange.class).addItem(client, item, quantity);
    }

    private void parsePlayerExchangeRemoveItemMessage(long itemId, int quantity) throws CriticalException, InteractionException {
        GameItem item = client.player().getBag().get(itemId);
        assertFalse(item == null, "unknown item %d", itemId);

        client.interactions().current(PlayerExchange.class).removeItem(client, item, quantity);
    }

    private void parseReadyMessage() throws InteractionException {
        client.interactions().current(PlayerExchange.class).setReady(client);
    }

    private void parseStoreManagementMessage() throws InteractionException {
        client.interactions().push(new StoreManagementInteraction(client)).begin();
    }

    private void parseStoreManagementAddMessage(long itemId, int quantity, long price) throws CriticalException, InteractionException {
        GameItem item = client.player().getBag().get(itemId);
        assertFalse(item == null, "unknown item %d", itemId);

        client.interactions().current(StoreManagementInteraction.class).add(item,  quantity, price);
    }

    private void parseStoreManagementUpdateMessage(long itemId, long price) throws CriticalException, InteractionException {
        StoredItem stored = client.player().getStore().get(itemId);
        assertFalse(stored == null, "unknown item %d", itemId);

        client.interactions().current(StoreManagementInteraction.class).update(stored, price);
    }

    private void parseStoreManagementRemoveMessage(long itemId) throws CriticalException, InteractionException {
        StoredItem stored = client.player().getStore().get(itemId);
        assertFalse(stored == null, "unknown item %d", itemId);

        client.interactions().current(StoreManagementInteraction.class).remove(stored);
    }

}
