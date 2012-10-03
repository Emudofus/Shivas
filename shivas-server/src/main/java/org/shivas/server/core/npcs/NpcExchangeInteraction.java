package org.shivas.server.core.npcs;

import com.google.common.collect.Collections2;
import org.shivas.data.entity.NpcSale;
import org.shivas.protocol.client.enums.TradeTypeEnum;
import org.shivas.protocol.client.formatters.InfoGameMessageFormatter;
import org.shivas.protocol.client.formatters.ItemGameMessageFormatter;
import org.shivas.protocol.client.formatters.TradeGameMessageFormatter;
import org.shivas.server.core.exchanges.Purchasable;
import org.shivas.server.core.exchanges.Salable;
import org.shivas.server.core.interactions.AbstractInteraction;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.interactions.InteractionType;
import org.shivas.server.core.items.PlayerBag;
import org.shivas.server.database.models.GameItem;
import org.shivas.server.services.game.GameClient;
import org.shivas.server.utils.Converters;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 02/10/12
 * Time: 23:48
 */
public class NpcExchangeInteraction extends AbstractInteraction implements Purchasable, Salable {
    private final GameClient client;
    private final GameNpc npc;

    public NpcExchangeInteraction(GameClient client, GameNpc npc) {
        this.client = client;
        this.npc = npc;
    }

    @Override
    public InteractionType getInteractionType() {
        return InteractionType.NPC_EXCHANGE;
    }

    @Override
    public void begin() throws InteractionException {
        client.write(TradeGameMessageFormatter.startTradeMessage(TradeTypeEnum.NPC));
        client.write(TradeGameMessageFormatter.itemListMessage(Collections2.transform(npc.getSales().values(), Converters.NPCSALE_TO_BASEITEMTEMPLATETYPE)));
    }

    @Override
    public void cancel() throws InteractionException {
        end();
    }

    @Override
    protected void internalEnd() throws InteractionException {
        client.write(TradeGameMessageFormatter.tradeQuitMessage());
    }

    @Override
    public void purchase(long saleId, int quantity) throws InteractionException {
        PlayerBag bag = client.player().getBag();

        NpcSale sale = npc.getSales().get((int) saleId);
        if (sale == null) {
            throw new InteractionException("unknown sale " + saleId);
        }

        long price = sale.getPrice() * quantity;
        if (price > bag.getKamas()) {
            client.write(InfoGameMessageFormatter.notEnoughKamasMessage());
        } else if (bag.full()) {
            client.write(InfoGameMessageFormatter.fullBagMessage());
        } else {
            bag.minusKamas(price);

            GameItem item = (GameItem) sale.getItem().generate(),
                     same = bag.sameAs(item);

            if (same != null) {
                same.plusQuantity(quantity);
                client.service().repositories().items().saveLater(same);

                client.write(ItemGameMessageFormatter.quantityMessage(same.getId(), same.getQuantity()));
            } else {
                item.setQuantity(quantity);
                bag.persist(item);

                client.write(ItemGameMessageFormatter.addItemMessage(item.toBaseItemType()));
            }

            client.write(client.player().getStats().packet());
            client.write(ItemGameMessageFormatter.inventoryStatsMessage(client.player().getStats().pods()));
            client.write(TradeGameMessageFormatter.buySuccessMessage());
        }
    }

    @Override
    public void sell(long itemId, int quantity) throws InteractionException {
        PlayerBag bag = client.player().getBag();

        GameItem item = bag.get(itemId);
        if (item == null) {
            throw new InteractionException("unknown item " + itemId);
        }
        if (item.getQuantity() < quantity) {
            throw new InteractionException("not enough quantity");
        }

        long price = item.getTemplate().getPrice() * quantity / client.service().config().npcBuyCoefficient();
        bag.plusKamas(price);

        item.minusQuantity(quantity);

        if (item.getQuantity() <= 0) {
            bag.delete(item);

            client.write(ItemGameMessageFormatter.deleteMessage(item.getId()));
        } else {
            client.write(ItemGameMessageFormatter.quantityMessage(item.getId(), item.getQuantity()));
        }

        client.write(client.player().getStats().packet());
        client.write(ItemGameMessageFormatter.inventoryStatsMessage(client.player().getStats().pods()));
        client.write(TradeGameMessageFormatter.sellSuccessMessage());
    }
}
