package org.shivas.server.core.stores;

import org.shivas.protocol.client.enums.TradeTypeEnum;
import org.shivas.protocol.client.formatters.TradeGameMessageFormatter;
import org.shivas.server.core.interactions.AbstractInteraction;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.interactions.InteractionType;
import org.shivas.server.services.game.GameClient;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 22/09/12
 * Time: 18:59
 */
public class StoreInteraction extends AbstractInteraction {
    private final GameClient client;
    private final PlayerStore store;

    public StoreInteraction(GameClient client, PlayerStore store) {
        this.client = client;
        this.store = store;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.STORE;
    }

    @Override
    protected void internalEnd() throws InteractionException {
    }

    @Override
    public void begin() throws InteractionException {
        client.write(TradeGameMessageFormatter.startTradeMessage(TradeTypeEnum.STORE));
        client.write(TradeGameMessageFormatter.storedItemsListMessage(store.toStoreItemType()));
    }

    @Override
    public void cancel() throws InteractionException {
    }
}
