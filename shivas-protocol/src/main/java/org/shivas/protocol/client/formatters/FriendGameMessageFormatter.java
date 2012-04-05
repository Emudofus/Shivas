package org.shivas.protocol.client.formatters;

import java.util.Collection;

import org.shivas.protocol.client.enums.FriendAddErrorEnum;
import org.shivas.protocol.client.types.BaseFriendType;

/**
 * User: Blackrush
 * Date: 06/11/11
 * Time: 10:17
 * IDE:  IntelliJ IDEA
 */
public class FriendGameMessageFormatter {
    public static String notifyFriendOnConnectMessage(boolean notifyFriendOnConnect){
        return "FO" + (notifyFriendOnConnect ? "+": "-");
    }

    public static String friendListMessage(Collection<BaseFriendType> friends){
        StringBuilder sb = new StringBuilder().append("FL");

        for (BaseFriendType friend : friends){
            sb.append('|').append(friend.getNickname());
            if (friend.isConnected()) {
                parseFriendCharacter(sb, friend);
            }
        }

        return sb.toString();
    }

    private static void parseFriendCharacter(StringBuilder sb, BaseFriendType friend){
        sb.append(';').append('?');
        sb.append(';').append(friend.getName());
        sb.append(';').append(friend.isReciprocal() ? friend.getLevel() : "?");
        sb.append(';').append(friend.isReciprocal() ? friend.getAlignmentId() : "0");
        sb.append(';').append(friend.getBreedId());
        sb.append(';').append(friend.getGender() ? '1' : '0');
        sb.append(';').append(friend.getSkin());
    }

    public static String addFriendMessage(BaseFriendType friend){
        StringBuilder sb = new StringBuilder().append("FAK");
        sb.append(friend.getNickname());
        if (friend.isConnected()){
            parseFriendCharacter(sb, friend);
        }
        return sb.toString();
    }

    public static String addFriendErrorMessage(FriendAddErrorEnum error){
        return "FAE" + String.valueOf(error.getValue());
    }

    public static String deleteFriendMessage(){
        return "FDK";
    }

    public static String deleteFriendErrorMessage(){
        return "FDEf";
    }
}
