package org.shivas.protocol.client.enums; /**
 * User: Blackrush
 * Date: 22/12/11
 * Time: 12:34
 * IDE : IntelliJ IDEA
 */

import java.util.HashMap;
import java.util.Map;

public enum ChannelEnum {
    Alignment('!'),
    Team('#'),
    Party('$'),
    Guild('%'),
    General('*'),
    Trade(':'),
    Recruitment('?'),
    Admin('@'),
    Information('i'),
    UNKNOWN0('p'),
    UNKNOWN1((char) 194),
    UNKNOWN2((char) 207);

    private char value;
    private ChannelEnum(char value) {
        this.value = value;
    }
    public char value() {
        return value;
    }

    private static final Map<Character, ChannelEnum> values = new HashMap<Character, ChannelEnum>();
    static {
        for (ChannelEnum e : values()) {
            values.put(e.value(), e);
        }
    }
    public static ChannelEnum valueOf(char value) {
        return values.get(value);
    }
}
