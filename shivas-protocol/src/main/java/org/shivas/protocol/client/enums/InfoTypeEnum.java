package org.shivas.protocol.client.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 29/01/12
 * Time: 10:37
 * To change this template use File | Settings | File Templates.
 */
public enum InfoTypeEnum {
    START_SAVE(1164),
    END_SAVE(1165);

    private int value;
    InfoTypeEnum(int value) {
        this.value = value;
    }
    public int value() {
        return value;
    }

    private static final Map<Integer, InfoTypeEnum> values = new HashMap<Integer, InfoTypeEnum>();
    static {
        for (InfoTypeEnum e : values()) {
            values.put(e.value(), e);
        }
    }
    public static InfoTypeEnum valueOf(int value) {
        return values.get(value);
    }
}
