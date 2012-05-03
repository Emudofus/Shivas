package org.shivas.protocol.client.enums; /**
 * User: Blackrush
 * Date: 22/12/11
 * Time: 12:34
 * IDE : IntelliJ IDEA
 */

import java.util.HashMap;
import java.util.Map;

public enum Channel {
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
    private Channel(int value) {
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

    private static final Map<Integer, Channel> values = new HashMap<Integer, Channel>();
    static {
        for (Channel e : values()) {
            values.put(e.value(), e);
        }
    }
    public static Channel valueOf(int value) {
        return values.get(value);
    }
    public static Channel valueOf(char value) {
        return valueOf((int)value);
    }
}
