package org.shivas.protocol.client.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 17/10/12
 * Time: 18:55
 */
public enum FightSideEnum {
    BLUE(1),
    RED(2);

    int value;
    FightSideEnum(int value) { this.value = value; }
    public int value() { return value; }

    public static FightSideEnum valueOf(int value) {
        for (FightSideEnum side : values()) if (side.value == value) return side;
        return null;
    }
}
