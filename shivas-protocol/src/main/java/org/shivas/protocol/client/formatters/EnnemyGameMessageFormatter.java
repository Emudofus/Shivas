package org.shivas.protocol.client.formatters;

import java.util.Collection;

import org.shivas.protocol.client.enums.ContactAddErrorEnum;
import org.shivas.protocol.client.enums.ContactStateEnum;
import org.shivas.protocol.client.enums.Gender;
import org.shivas.protocol.client.types.BaseContactType;

public class EnnemyGameMessageFormatter {
	public static String ennemyListMessage(Collection<BaseContactType> ennemies){
        StringBuilder sb = new StringBuilder().append("iL");

        for (BaseContactType ennemy : ennemies){
            sb.append('|').append(ennemy.getNickname());
            if (ennemy.isConnected()) {
                parseEnnemyCharacter(sb, ennemy);
            }
        }

        return sb.toString();
    }

    private static void parseEnnemyCharacter(StringBuilder sb, BaseContactType ennemy){
        sb.append(';').append(ennemy.isReciprocal() && ennemy.getState() != ContactStateEnum.UNKNOWN ? ennemy.getState().value() : "?");
        sb.append(';').append(ennemy.getName());
        sb.append(';').append(ennemy.isReciprocal() ? ennemy.getLevel() : "?");
        sb.append(';').append(ennemy.isReciprocal() ? ennemy.getAlignmentId() : "0");
        sb.append(';').append(ennemy.getBreedId());
        sb.append(';').append(ennemy.getGender() == Gender.FEMALE ? '1' : '0');
        sb.append(';').append(ennemy.getSkin());
    }

    public static String addEnnemyMessage(BaseContactType ennemy){
        StringBuilder sb = new StringBuilder().append("iAK");
        sb.append(ennemy.getNickname());
        if (ennemy.isConnected()){
            parseEnnemyCharacter(sb, ennemy);
        }
        return sb.toString();
    }

    public static String addEnnemyErrorMessage(ContactAddErrorEnum error){
        return "iAE" + String.valueOf(error.getValue());
    }

    public static String deleteEnnemyMessage(){
        return "iDK";
    }

    public static String deleteEnnemyErrorMessage(){
        return "iDEf";
    }
}
