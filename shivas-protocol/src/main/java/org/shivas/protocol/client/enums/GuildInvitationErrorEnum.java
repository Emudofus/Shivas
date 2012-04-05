package org.shivas.protocol.client.enums;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 04/02/12
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */
public enum GuildInvitationErrorEnum {
    UNKNOWN('u'),
    ALREADY_IN_GUILD('a'),
    AWAY('o'),
    NOT_ENOUGH_RIGHTS('d');

    private char value;
    GuildInvitationErrorEnum(char value) {
        this.value = value;
    }
    public char value() {
        return value;
    }
}
