package org.shivas.server.core.stores;

import org.shivas.protocol.client.enums.TradeTypeEnum;
import org.shivas.protocol.client.formatters.InfoGameMessageFormatter;
import org.shivas.protocol.client.formatters.ItemGameMessageFormatter;
import org.shivas.protocol.client.formatters.TradeGameMessageFormatter;
import org.shivas.server.core.interactions.AbstractInteraction;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.interactions.InteractionType;
import org.shivas.server.database.models.GameItem;
import org.shivas.server.database.models.StoredItem;
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

    public PlayerStore getStore() {
        return store;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.STORE;
    }

    @Override
    public void begin() throws InteractionException {
        store.subscribe(client.eventListener());

        client.write(TradeGameMessageFormatter.startTradeMessage(TradeTypeEnum.STORE));
        client.write(TradeGameMessageFormatter.storedItemsListMessage(store.toStoreItemType()));
    }

    @Override
    protected void internalEnd() throws InteractionException {
        store.unsubscribe(client.eventListener());

        client.write(TradeGameMessageFormatter.tradeQuitMessage());
    }

    @Override
    public void cancel() throws InteractionException {
        internalEnd();
    }

    public void buy(long itemId, int quantity) throws InteractionException {
        StoredItem stored = store.get(itemId);
        if (stored == null) {
            throw new InteractionException("unknown item " + itemId);
        }
        if (stored.getQuantity() < quantity) {
            throw new InteractionException("not enough quantity");
        }

        long price = stored.getPrice() * quantity;

        if (price > client.player().getBag().getKamas()) {
            client.write(InfoGameMessageFormatter.notEnoughKamasMessage());
        } else {
            synchronized (store.getLock()) {
                client.player().getBag().minusKamas(price);
                store.plusEarnedKamas(price);
                stored.minusQuantity(quantity);

                GameItem same = client.player().getBag().sameAs(stored.getItem());
                if (same != null) {
                    same.plusQuantity(quantity);
                    client.write(ItemGameMessageFormatter.quantityMessage(same.getId(), same.getQuantity()));
                } else {
                    GameItem copy = stored.getItem().copy();
                    copy.setQuantity(quantity);

                    client.player().getBag().persist(copy);
                    client.write(ItemGameMessageFormatter.addItemMessage(copy.toBaseItemType()));
                }

                client.write(TradeGameMessageFormatter.buySuccessMessage());
                client.write(client.player().getStats().packet());

                store.refresh(); // will send storedItemsListMessage to client
            }
        }
    }
}
