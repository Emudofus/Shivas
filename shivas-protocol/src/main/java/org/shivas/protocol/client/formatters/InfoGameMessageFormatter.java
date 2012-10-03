package org.shivas.protocol.client.formatters;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.shivas.protocol.client.enums.FightAttributeType;
import org.shivas.protocol.client.enums.InfoTypeEnum;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Blackrush
 * Date: 06/11/11
 * Time: 10:22
 * IDE:  IntelliJ IDEA
 */
public class InfoGameMessageFormatter {
    private static final String LAST_CONNECTION_PATTERN = "yyyy~MM~dd~HH~mm";
	public static final SimpleDateFormat LAST_CONNECTION_FORMATTER = new SimpleDateFormat(LAST_CONNECTION_PATTERN);
	public static final DateTimeFormatter LAST_CONNECTION_FORMATTER_JODA = DateTimeFormat.forPattern(LAST_CONNECTION_PATTERN);

    public static String welcomeMessage(){
        return "Im189";
    }

    public static String currentAddressInformationMessage(String currentAddress){
        return "Im153;" + currentAddress;
    }

    public static String lastConnectionInformationMessage(Date lastConnection, String lastAddress){
        return "Im0152;" + LAST_CONNECTION_FORMATTER.format(lastConnection) + "~" + lastAddress;
    }

    public static String lastConnectionInformationMessage(DateTime lastConnection, String lastAddress){
        return "Im0152;" + LAST_CONNECTION_FORMATTER_JODA.print(lastConnection) + "~" + lastAddress;
    }

    public static String accountMutedMessage(){
        return "Im1124;";
    }

    public static String friendConnectedMessage(String friendNickname, String friendCharacterName){
    	return "Im0143;" + friendNickname + " (" + urlize(friendCharacterName) + ")";
    }

    public static String urlize(String title){
        return "<b><a href='asfunction:onHref,ShowPlayerPopupMenu," + title + "'>" + title + "</a></b>";
    }

    public static String floodMessage(int remaining){
        return "Im0115;" + remaining;
    }

    public static String youReAwayMessage(){
        return "Im072";
    }

    public static String startSaveMessage(){
        return "Im1164";
    }

    public static String endSaveMessage() {
        return "Im1165";
    }

    public static String cantWearItemMessage(){
        return "Im119|43";
    }

    public static String fullGuildMessage(int availablePlaces){
        return "Im155;" + availablePlaces;
    }

    public static String fightAttributeActivationMessage(FightAttributeType attribute, boolean active){
        switch (attribute) {
            case DENY_ALL:
                if (active) return "Im095";
                return "Im096";

            case NEED_HELP:
                if (active) return "Im103";
                return "Im104";

            case ALLOW_PARTY:
                if (active) return "Im093";
                return "Im094";

            case DENY_SPECTATORS:
                if (active) return "Im040";
                return "Im039";

            default:
                return null;
        }
    }

    public static String newZaapMessage(){
        return "Im024";
    }

    public static String notEnoughStorePlacesMessage(byte maxStores){
        return "Im125;" + maxStores;
    }

    public static String emptyStoreMessage(){
        return "Im123";
    }

    public static String waypointSavedMessage(){
        return "Im06";
    }

    public static String notEnoughKamasMessage(){
        return "Im063";
    }

    public static String fullBagMessage() {
        return "Im062";
    }

    public static String genericInfoMessage(InfoTypeEnum type){
        return "Im" + type.value();
    }

    public static String earnedKamasMessage(long kamas) {
        return "Im045;" + kamas;
    }
}
