package org.shivas.protocol.client.formatters;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * User: Blackrush
 * Date: 12/11/11
 * Time: 19:32
 * IDE : IntelliJ IDEA
 */
public class BasicGameMessageFormatter {
	
	private static final String PATTERN_FORMATTER = "yyyy|MM|dd";
    public static final SimpleDateFormat CURRENT_DATE_FORMATTER = new SimpleDateFormat(PATTERN_FORMATTER);
    public static final DateTimeFormatter CURRENT_DATE_TIME_FORMATTER = DateTimeFormat.forPattern(PATTERN_FORMATTER);

    public static String noOperationMessage(){
        return "BN";
    }

    public static String currentDateMessage(Date now){
        return "BD" + CURRENT_DATE_FORMATTER.format(now);
    }

    public static String currentDateMessage(DateTime now){
        return "BD" + CURRENT_DATE_TIME_FORMATTER.print(now);
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
