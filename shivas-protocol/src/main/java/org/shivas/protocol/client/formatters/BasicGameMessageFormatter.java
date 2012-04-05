package org.shivas.protocol.client.formatters;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Blackrush
 * Date: 12/11/11
 * Time: 19:32
 * IDE : IntelliJ IDEA
 */
public class BasicGameMessageFormatter {
    public static final SimpleDateFormat CURRENT_DATE_FORMATTER = new SimpleDateFormat("yyyy|MM|dd");

    public static String noOperationMessage(){
        return "BN";
    }

    public static String currentDateMessage(Date now){
        return "BD" + CURRENT_DATE_FORMATTER.format(now);
    }

    public static String consoleMessage(String message, int color){
        return "BAT" + color + message;
    }

    public static String consoleMessage(String message){
        return consoleMessage(message, 2);
    }

    public static String alertMessage(String message){
        return "BAIO<b>" + message + "</b>";
    }

    public static String whoisMessage(String accountNickname, String characterName){
        return "BWK" + accountNickname + "|1|" + characterName + "|-1";
    }

    public static String whoisErrorMessage(String pseudo){
        return "BWE" + pseudo;
    }
}
