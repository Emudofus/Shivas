package org.shivas.protocol.client.formatters;

import org.shivas.common.StringUtils;
import org.shivas.protocol.client.types.BaseCharacterType;
import org.shivas.protocol.client.types.BaseItemType;

import java.util.Collection;

import static org.shivas.common.StringUtils.toHexOrNegative;

/**
 * User: Blackrush
 * Date: 01/11/11
 * Time: 18:08
 * IDE : IntelliJ IDEA
 */
public class ApproachGameMessageFormatter {
    public static String helloGameMessage(){
        return "HG";
    }

    public static String authenticationSuccessMessage(int community){
        return "ATK" + community;
    }

    public static String authenticationFailureMessage(){
        return "ATE";
    }

    public static String regionalVersionResponseMessage(int community){
        return "AV" + community;
    }

    public static String charactersListMessage(int serverId, long remainingSubscriptionTime, Collection<BaseCharacterType> characters){
        StringBuilder sb = new StringBuilder(50).append("ALK");

        sb.append(remainingSubscriptionTime).append("|");
        sb.append(characters.size());

        for (BaseCharacterType character : characters){
            sb.append("|");

            sb.append(character.getId()).append(";")
              .append(character.getName()).append(";")
              .append(character.getLevel()).append(";")
              .append(character.getSkin()).append(";")
              .append(StringUtils.toHexOrNegative(character.getColor1())).append(";")
              .append(StringUtils.toHexOrNegative(character.getColor2())).append(";")
              .append(StringUtils.toHexOrNegative(character.getColor3())).append(";");

            ItemGameMessageFormatter.parseAccessories(sb, character.getAccessories());
            sb.append(';');
            
            sb.append(character.isStoreActive() ? '1' : '0').append(';')
              .append(serverId).append(';')
              .append(';') // is dead ?  (heroic)
              .append(';') // nb deathes (heroic)
              .append(""); // level max
        }
        return sb.toString();
    }

    public static String characterNameSuggestionSuccessMessage(String name){
        return "APK" + name;
    }

    public static String characterNameSuggestionFailureMessage(){
        return "APE";
    }

    public static String accountFullMessage() {
        return "AAEf";
    }

    public static String characterNameAlreadyExistsMessage() {
        return "AAEa";
    }

    public static String characterCreationSuccessMessage(){
        return "AAK";
    }

    public static String characterDeletionFailureMessage(){
        return "ADE";
    }

    public static String characterSelectionFailureMessage(){
        return "ASE";
    }

    public static String characterSelectionSucessMessage(long id, String name, int level, int breedId, boolean gender,
                                                         short skin, int color1, int color2, int color3, Collection<BaseItemType> items)
    {
        StringBuilder sb = new StringBuilder().append("ASK|");

        sb.append(id).append('|');
        sb.append(name).append('|');
        sb.append(level).append('|');
        sb.append(breedId).append('|');
        sb.append(gender ? '1' : '0').append('|');
        sb.append(skin).append('|');
        sb.append(toHexOrNegative(color1)).append('|');
        sb.append(toHexOrNegative(color2)).append('|');
        sb.append(toHexOrNegative(color3)).append('|');
        ItemGameMessageFormatter.formatItems(sb, items);

        return sb.toString();
    }

    public static String setRestrictionsMessage(){
        return "AR6bk"; // 6bk(base 36) todo
    }

    public static String levelUpMessage(short level){
        return "AN" + level;
    }

    public static String boostCharacteristicErrorMessage(){
        return "ABE";
    }
}
