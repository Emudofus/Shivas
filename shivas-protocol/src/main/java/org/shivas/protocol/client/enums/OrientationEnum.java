package org.shivas.protocol.client.enums; /**
 * User: Blackrush
 * Date: 23/11/11
 * Time: 16:41
 * IDE : IntelliJ IDEA
 */

import java.util.HashMap;
import java.util.Map;

public enum OrientationEnum {
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST,
    NORTH,
    NORTH_EAST;

    private static final Map<Integer, OrientationEnum> values = new HashMap<Integer, OrientationEnum>();

    static {
        for (OrientationEnum e : values()) {
            values.put(e.ordinal(), e);
        }
    }

    public static OrientationEnum valueOf(int ordinal) {
        return values.get(ordinal);
    }
}
