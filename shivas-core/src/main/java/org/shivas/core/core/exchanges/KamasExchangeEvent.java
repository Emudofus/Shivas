package org.shivas.core.core.exchanges;

import org.shivas.core.services.game.GameClient;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 19/09/12
 * Time: 15:22
 */
public final class KamasExchangeEvent extends ExchangeEvent {

    private final long kamas;

    public KamasExchangeEvent(PlayerExchange exchange, GameClient source, long kamas) {
        super(exchange, source);
        this.kamas = kamas;
    }

    @Override
    public ExchangeEventType getExchangeEventType() {
        return ExchangeEventType.KAMAS;
    }

    public long getKamas() {
        return kamas;
    }
}
