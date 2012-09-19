package org.shivas.server.core.exchanges;

import org.shivas.server.services.game.GameClient;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 19/09/12
 * Time: 15:32
 */
public class ReadyExchangeEvent extends ExchangeEvent {
    private final boolean ready;

    public ReadyExchangeEvent(PlayerExchange exchange, GameClient source, boolean ready) {
        super(exchange, source);
        this.ready = ready;
    }

    @Override
    public ExchangeEventType getExchangeEventType() {
        return ExchangeEventType.READY;
    }

    public boolean isReady() {
        return ready;
    }
}
