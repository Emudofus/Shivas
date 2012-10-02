package org.shivas.server.core.npcs;

import org.shivas.protocol.client.enums.TradeTypeEnum;
import org.shivas.protocol.client.formatters.TradeGameMessageFormatter;
import org.shivas.server.core.exchanges.Purchasable;
import org.shivas.server.core.exchanges.Salable;
import org.shivas.server.core.interactions.AbstractInteraction;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.interactions.InteractionType;
import org.shivas.server.services.game.GameClient;

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
    public void purchase(long itemId, int quantity) throws InteractionException {
    }

    @Override
    public void sell(long itemId, int quantity) throws InteractionException {
    }
}
