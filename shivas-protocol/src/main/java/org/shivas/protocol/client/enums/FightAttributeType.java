package org.shivas.protocol.client.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 19/02/12
 * Time: 15:06
 */
public enum FightAttributeType {
    NEED_HELP('H'),
    DENY_ALL('N'),
    ALLOW_PARTY('P'),
    DENY_SPECTATORS('S');

    private char value;
    FightAttributeType(char value) {
        this.value = value;
    }
    public char value() {
        return value;
    }
    public boolean equals(FightAttributeType e) {
        return this.value == e.value;
    }
    public String toString(){
        return String.valueOf(value);
    }

    private static HashMap<Character, FightAttributeType> values = new HashMap<Character, FightAttributeType>();
    static {
        for (FightAttributeType e : values()) {
            values.put(e.value, e);
        }
    }
    public static FightAttributeType valueOf(char value) {
        return values.get(value);
    }
    public static Map<FightAttributeType, Boolean> emptyMap(boolean defaultz){
        Map<FightAttributeType, Boolean> result = new HashMap<FightAttributeType, Boolean>(values().length);
        for (FightAttributeType attribute : values()){
            result.put(attribute, defaultz);
        }
        return result;
    }
}
