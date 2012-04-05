package org.shivas.protocol.client.enums;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 01/01/12
 * Time: 12:12
 * To change this template use File | Settings | File Templates.
 */
public enum FriendAddErrorEnum {
    NOT_FOUND('f'),
    EGOCENTRIC('y'),
    ALREADY_ADDED('a'),
    FULL_LIST('m');

    private char value;
    FriendAddErrorEnum(char value){
        this.value = value;
    }
    public char getValue(){
        return value;
    }
}
