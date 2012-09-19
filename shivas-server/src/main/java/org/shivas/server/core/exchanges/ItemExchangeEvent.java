package org.shivas.server.core.exchanges;

import org.shivas.server.database.models.GameItem;
import org.shivas.server.services.game.GameClient;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 19/09/12
 * Time: 15:24
 */
public final class ItemExchangeEvent extends ExchangeEvent {

    private final ExchangeEventType type;
    private final GameItem item;

    public ItemExchangeEvent(ExchangeEventType type, PlayerExchange exchange, GameClient source, GameItem item) {
        super(exchange, source);
        this.type = type;
        this.item = item;
    }

    @Override
    public ExchangeEventType getExchangeEventType() {
        return type;
    }

    public GameItem getItem() {
        return item;
    }
}
