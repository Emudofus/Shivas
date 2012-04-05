package org.shivas.protocol.client.enums;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 19/02/12
 * Time: 15:51
 */
public enum FightJoinErrorEnum {
    UNAVAILABLE('t'),
    DENIED('f');

    private char value;
    FightJoinErrorEnum(char value) {
        this.value = value;
    }
    public char value() {
        return value;
    }
    public boolean equals(FightJoinErrorEnum e) {
        return this.value == e.value;
    }
    public String toString(){
        return String.valueOf(value);
    }

    private static HashMap<Character, FightJoinErrorEnum> values = new HashMap<Character, FightJoinErrorEnum>();
    static {
        for (FightJoinErrorEnum e : values()) {
            values.put(e.value, e);
        }
    }
    public static FightJoinErrorEnum valueOf(char value) {
        return values.get(value);
    }
}
