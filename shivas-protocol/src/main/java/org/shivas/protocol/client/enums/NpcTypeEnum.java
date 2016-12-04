package org.shivas.protocol.client.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 30/01/12
 * Time: 20:03
 */
public enum NpcTypeEnum {
    BUY_SELL(1),
    TRADE(2),
    SPEAK(3),
    PET(4),
    SELL(5),
    BUY(6),
    RESURECT_PET(7),
    MOUNT(8);

    private int value;

    NpcTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    private static final Map<Integer, NpcTypeEnum> values = new HashMap<Integer, NpcTypeEnum>();

    static {
        for (NpcTypeEnum e : values()) {
            values.put(e.value(), e);
        }
    }

    public static NpcTypeEnum valueOf(int value) {
        return values.get(value);
    }
}
