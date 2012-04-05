package org.shivas.protocol.client.enums;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 13/01/12
 * Time: 19:16
 */
public enum TradeTypeEnum {
    NPC(0),
    PLAYER(1),
    STORE(4),
    STORE_MANAGEMENT(6),
    COLLECTOR(8),
    MARKET_PLACE_BUY(10),
    MARKET_PLACE_SELL(11);

    private int value;

    TradeTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    private static HashMap<Integer, TradeTypeEnum> values = new HashMap<Integer, TradeTypeEnum>();

    static {
        for (TradeTypeEnum e : values()) {
            values.put(e.value, e);
        }
    }

    public static TradeTypeEnum valueOf(int value) {
        return values.get(value);
    }
}
