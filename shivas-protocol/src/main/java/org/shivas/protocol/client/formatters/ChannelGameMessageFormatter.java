package org.shivas.protocol.client.formatters;

/**
 * User: Blackrush
 * Date: 05/11/11
 * Time: 13:05
 * IDE:  IntelliJ IDEA
 */
public class ChannelGameMessageFormatter {
    public static String addChannelMessage(String channels){
        return "cC+" + channels;
    }

    public static String enabledEmotesMessage(String emotes){
        return enabledEmotesMessage(emotes, "0");
    }

    public static String enabledEmotesMessage(String emotes, String suffix){
        return "eL" + emotes + "|" + suffix;
    }

    public static String clientMultiMessage(char channel, long actorId, String playerName, String message){
        return "cMK" + Character.toString(channel) + "|" + actorId + "|" + playerName + "|" + message;
    }

    public static String clientPrivateMessage(boolean copy, long senderId, String senderName, String message){
        return "cMK" + (copy ? "F" : "T") + "|" + senderId + "|" + senderName + "|" + message;
    }

    public static String clientMultiErrorMessage(){
        return "cMEf";
    }

    public static String informationMessage(String message){
        return "cs" + message;
    }
}
