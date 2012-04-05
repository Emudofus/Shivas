package org.shivas.protocol.client.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 22/02/12
 * Time: 20:03
 * To change this template use File | Settings | File Templates.
 */
public enum InteractiveObjectTypeEnum {
    SAVE_WAYPOINT(44),
    WAYPOINT(114);

    private int value;
    InteractiveObjectTypeEnum(int value) {
        this.value = value;
    }
    public int value() {
        return value;
    }

    private static final Map<Integer, InteractiveObjectTypeEnum> values = new HashMap<Integer, InteractiveObjectTypeEnum>();
    static {
        for (InteractiveObjectTypeEnum e : values()) {
            values.put(e.value(), e);
        }
    }
    public static InteractiveObjectTypeEnum valueOf(int value) {
        return values.get(value);
    }
}
