package org.shivas.protocol.client.enums; /**
 * User: Blackrush
 * Date: 22/12/11
 * Time: 12:34
 * IDE : IntelliJ IDEA
 */

import java.util.HashMap;
import java.util.Map;

public enum ChannelEnum {
    Alignment(33),
    Team(35),
    Party(36),
    Guild(37),
    General(42),
    Trade(58),
    Recruitment(63),
    Admin(64),
    Information(105),
    UNKNOWN0(112),
    UNKNOWN1(194),
    UNKNOWN2(207);

    private int value;
    private ChannelEnum(int value) {
        this.value = value;
    }
    public int value() {
        return value;
    }
    public char toChar() {
        return (char)value;
    }
    @Override
    public String toString() {
        return Integer.toString(value);
    }

    private static final Map<Integer, ChannelEnum> values = new HashMap<Integer, ChannelEnum>();
    static {
        for (ChannelEnum e : values()) {
            values.put(e.value(), e);
        }
    }
    public static ChannelEnum valueOf(int value) {
        return values.get(value);
    }
    public static ChannelEnum valueOf(char value) {
        return valueOf((int)value);
    }
}
