package org.shivas.protocol.client.formatters;

import java.util.Collection;

import org.shivas.protocol.client.enums.ChannelEnum;

/**
 * User: Blackrush
 * Date: 05/11/11
 * Time: 13:05
 * IDE:  IntelliJ IDEA
 */
public class ChannelGameMessageFormatter {
    public static String addChannelsMessage(Collection<ChannelEnum> channels){
    	StringBuilder sb = new StringBuilder().append("cC+");
    	for (ChannelEnum channel : channels) {
    		sb.append(channel.value());
    	}
    	return sb.toString();
    }
    
    public static String addChannelMessage(ChannelEnum channel) {
    	return String.format("cC+%s", channel.value());
    }
    
    public static String removeChannelMessage(ChannelEnum channel) {
    	return String.format("cC-%s", channel.value());
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
