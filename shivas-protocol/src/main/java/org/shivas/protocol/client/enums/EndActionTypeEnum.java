package org.shivas.protocol.client.enums; /**
 * User: Blackrush
 * Date: 25/11/11
 * Time: 18:46
 * IDE : IntelliJ IDEA
 */

import java.util.HashMap;
import java.util.Map;

public enum EndActionTypeEnum {
    SPELL(0),
    MOVEMENT(2);

    private int value;

    EndActionTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    private static final Map<Integer, EndActionTypeEnum> values = new HashMap<Integer, EndActionTypeEnum>();

    static {
        for (EndActionTypeEnum e : values()) {
            values.put(e.value(), e);
        }
    }

    public static EndActionTypeEnum valueOf(int value) {
        return values.get(value);
    }
}
