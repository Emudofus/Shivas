package org.shivas.protocol.client.formatters;

import org.shivas.protocol.client.enums.GuildInvitationErrorEnum;
import org.shivas.protocol.client.types.BaseGuildMemberType;
import org.shivas.protocol.client.types.GuildEmblem;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 07/02/12
 * Time: 21:53
 */
public class GuildGameMessageFormatter {
    public static String startCreationMessage(){
        return "gn";
    }

    public static String quitCreationMessage(){
        return "gV";
    }

    public static String nameExistsMessage(){
        return "Ean";
    }

    public static String emblemExistsMessage(){
        return "Eae";
    }

    public static String alreadyHaveGuildMessage(){
        return "gCEa";
    }

    public static String statsMessage(String name, GuildEmblem emblem, int rank){
        return "gS" + name                                  + "|" +
                      Integer.toString(emblem.getBackgroundId(), 36)    + "|" +
                      Integer.toString(emblem.getBackgroundColor(), 36) + "|" +
                      Integer.toString(emblem.getForegroundId(), 36)    + "|" +
                      Integer.toString(emblem.getForegroundColor(), 36) + "|" +
                      Integer.toString(rank);
    }

    public static String creationSuccessMessage(){
        return "gCK";
    }

    public static String invitationErrorMessage(GuildInvitationErrorEnum error){
        return "gCE" + String.valueOf(error.value());
    }

    public static String invitationLocalMessage(String name){
        return "gJR" + name;
    }

    public static String invitationRemoteMessage(long sourceId, String sourceName, String guildName){
        return "gJr" + sourceId + "|" + sourceName + "|" + guildName;
    }

    public static String invitationLocalSuccessMessage(){
        return "gJKj";
    }

    public static String invitationRemoteSuccessMessage(String name){
        return "gJKa" + name;
    }

    public static String invitationRemoteFailureMessage(){
        return "gJEc";
    }

    public static String informationsGeneralMessage(boolean valid, short level, long experience, long expMin, long expMax){
        return "gIG" + (valid ? "1" : "0") + "|" +
                       level + "|" +
                       expMin + "|" +
                       experience + "|" +
                       expMax;
    }

    public static String membersListMessage(Collection<BaseGuildMemberType> members){
        StringBuilder sb = new StringBuilder(4 + 15 * members.size()).append("gIM+");

        boolean first = true;
        for (BaseGuildMemberType member : members) {
            if (first) first = false;
            else sb.append('|');

            sb.append(member.getId()).append(';');
            sb.append(member.getName()).append(';');
            sb.append(member.getLevel()).append(';');
            sb.append(member.getSkin()).append(';');
            sb.append(member.getRank()).append(';');
            sb.append(member.getExperienceRate()).append(';');
            sb.append(member.getExperienceGiven()).append(';');
            sb.append(member.getRights()).append(';');
            sb.append(member.isOnline() ? '1' : '0').append(';');
            sb.append(member.getAlignment()).append(';');
            sb.append(member.isOnline() ? 0 : new Duration(member.getLastConnection(), DateTime.now()).getStandardHours());
        }

        return sb.toString();
    }

    public static String kickLocalSuccessMessage(String kickerName, String targetName){
        return "gKK" + kickerName + "|" + targetName;
    }

    public static String kickRemoteSuccessMessage(String kickerName){
        return "gKK" + kickerName;
    }
}
