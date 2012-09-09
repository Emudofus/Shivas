package org.shivas.protocol.client.formatters;

import org.shivas.protocol.client.types.BasePartyMemberType;

import java.util.Collection;

/**
 * User: Blackrush
 * Date: 22/12/11
 * Time: 20:23
 * IDE : IntelliJ IDEA
 */
public class PartyGameMessageFormatter {
    public static String targetAlreadyInPartyMessage(String targetName){
        return "PIEa" + targetName;
    }

    public static String partyFullMessage(String targetName){
        return "PIEf";
    }

    public static String targetNotFoundMessage(String targetName){
        return "PIEn" + targetName;
    }

    public static String invitationSuccessMessage(String sourceName, String targetName){
        return "PIK" + sourceName + "|" + targetName;
    }

    public static String declineInvitationMessage(){
        return "PR";
    }

    public static String createPartyMessage(String leaderName){
        return "PCK" + leaderName;
    }

    public static String leaderInformationMessage(long leaderId){
        return "PL" + leaderId;
    }

    public static String addMembersMessage(Collection<BasePartyMemberType> members){
        StringBuilder sb = new StringBuilder(3 + 15 * members.size()).append("PM+");

        boolean first = true;
        for (BasePartyMemberType member : members){
            if (first) first = false;
            else sb.append('|');

            formatAddMemberMessage(sb, member);
        }

        return sb.toString();
    }

    private static void formatAddMemberMessage(StringBuilder sb, BasePartyMemberType member){
        sb.append(member.getId()).append(';');
        sb.append(member.getName()).append(';');
        sb.append(member.getSkin()).append(';');
        sb.append(member.getColor1()).append(';');
        sb.append(member.getColor2()).append(';');
        sb.append(member.getColor3()).append(';');
        ItemGameMessageFormatter.parseAccessories(sb, member.getItems());
        sb.append(';');
        sb.append(member.getLife()).append(',')
          .append(member.getMaxLife()).append(';');
        sb.append(member.getLevel()).append(';');
        sb.append(member.getInitiative()).append(';');
        sb.append(member.getProspection()).append(';');
        sb.append(member.getSide());
    }

    public static String addMemberMessage(BasePartyMemberType member) {
        StringBuilder sb = new StringBuilder(20).append("PM+");
        formatAddMemberMessage(sb, member);
        return sb.toString();
    }

    public static String removeMemberMessage(long memberId) {
        return "PM-" + memberId;
    }

    public static String refreshMemberMessage(BasePartyMemberType member){
        StringBuilder sb = new StringBuilder(20).append("PM~");
        formatAddMemberMessage(sb, member);
        return sb.toString();
    }

    public static String quitMessage() {
        return "PV";
    }
}
