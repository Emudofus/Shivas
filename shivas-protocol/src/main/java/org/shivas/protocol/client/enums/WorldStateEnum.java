package org.shivas.protocol.client.enums;

import java.util.HashMap;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 10:29
 * IDE : IntelliJ IDEA
 */
public enum WorldStateEnum {
    OFFLINE,
    ONLINE,
    SAVING;

    private static HashMap<Integer, WorldStateEnum> values = new HashMap<Integer, WorldStateEnum>();
    static{
        for (WorldStateEnum value : values()){
            values.put(value.ordinal(), value);
        }
    }

    public static WorldStateEnum valueOf(Integer value){
        return values.get(value);
    }
}
