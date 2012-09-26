package org.shivas.protocol.client.enums;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 26/09/12
 * Time: 12:32
 */
public enum GuildRankEnum {
    TESTING(0),
    LEADER(1),
    SECOND_LEADER(2);

    private int value;
    GuildRankEnum(int value) { this.value = value; }
    public int value() { return value; }

    public static GuildRankEnum valueOf(int value) {
        for (GuildRankEnum rank : values()) if (rank.value() == value) return rank;
        return null;
    }
}
