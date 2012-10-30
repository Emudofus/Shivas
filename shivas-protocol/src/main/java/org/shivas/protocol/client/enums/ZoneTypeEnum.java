package org.shivas.protocol.client.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 08:24
 */
public enum ZoneTypeEnum {
    CROSS('X'),
    LINE('L'),
    CIRCLE('C'),
    SINGLE_CELL('P');

    char value;
    ZoneTypeEnum(char value) { this.value = value; }
    public char value() { return value; }

    public static ZoneTypeEnum valueOf(char value) {
        for (ZoneTypeEnum val : values()) if (val.value == value) return val;
        return SINGLE_CELL;
    }
}
