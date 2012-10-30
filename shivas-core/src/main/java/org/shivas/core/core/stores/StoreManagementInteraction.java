package org.shivas.core.core.stores;

import org.shivas.protocol.client.enums.TradeTypeEnum;
import org.shivas.protocol.client.formatters.ItemGameMessageFormatter;
import org.shivas.protocol.client.formatters.TradeGameMessageFormatter;
import org.shivas.core.core.interactions.AbstractInteraction;
import org.shivas.core.core.interactions.InteractionException;
import org.shivas.core.core.interactions.InteractionType;
import org.shivas.core.database.models.GameItem;
import org.shivas.core.database.models.StoredItem;
import org.shivas.core.services.game.GameClient;

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

    protected void refresh() {
        client.write(TradeGameMessageFormatter.storedItemsListMessage(client.player().getStore().toStoreItemType()));
        client.write(ItemGameMessageFormatter.inventoryStatsMessage(client.player().getStats().pods()));
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
        client.write(TradeGameMessageFormatter.tradeQuitMessage());
    }

    @Override
    protected void internalEnd() throws InteractionException {
        cancel();
    }

    public void add(GameItem item, int quantity, long price) throws InteractionException {
        if (item.getQuantity() < quantity) throw new InteractionException("not enough quantity");

        StoredItem stored = client.player().getStore().get(item.getId());
        if (stored == null) {
            stored = new StoredItem();
            stored.setItem(item);
            stored.setQuantity(quantity);
            stored.setPrice(price);

            client.player().getStore().persist(stored);
        } else {
            stored.plusQuantity(quantity);
            stored.setPrice(price);
        }

        item.minusQuantity(quantity);

        if (item.getQuantity() <= 0) {
            client.player().getBag().remove(item);

            client.write(ItemGameMessageFormatter.deleteMessage(item.getId()));
        } else {
            client.write(ItemGameMessageFormatter.quantityMessage(item.getId(), item.getQuantity()));
        }

        refresh();
    }

    public void update(StoredItem stored, long price) throws InteractionException {
        stored.setPrice(price);
        client.service().repositories().storedItems().saveLater(stored);

        refresh();
    }

    public void remove(StoredItem stored) throws InteractionException {
        GameItem item = stored.getItem();
        boolean empty = item.getQuantity() <= 0;

        item.plusQuantity(stored.getQuantity());
        client.player().getStore().delete(stored);

        if (empty) {
            client.player().getBag().add(item);

            client.write(ItemGameMessageFormatter.addItemMessage(item.toBaseItemType()));
        } else {
            client.write(ItemGameMessageFormatter.quantityMessage(item.getId(), item.getQuantity()));
        }

        refresh();
    }
}
