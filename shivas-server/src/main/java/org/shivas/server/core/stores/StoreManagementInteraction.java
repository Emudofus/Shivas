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
 * Date: 20/09/12
 * Time: 18:16
 */
public class StoreManagementInteraction extends AbstractInteraction {

    private final GameClient client;

    public StoreManagementInteraction(GameClient client) {
        this.client = client;
    }

    @Override
    public InteractionType getInteractionType() {
            return InteractionType.STORE_MANAGEMENT;
    }

    @Override
    public void begin() throws InteractionException {
        client.write(TradeGameMessageFormatter.startTradeMessage(TradeTypeEnum.STORE_MANAGEMENT));
        client.write(TradeGameMessageFormatter.storedItemsListMessage(client.player().getStore().toStoreItemType()));
    }

    @Override
    public void cancel() throws InteractionException {
    }

    @Override
    protected void internalEnd() throws InteractionException {
    }
}
