package org.shivas.protocol.client.formatters;

import org.shivas.common.StringUtils;
import org.shivas.protocol.client.types.BaseRolePlayActorType;
import org.shivas.protocol.client.types.RolePlayCharacterType;
import org.shivas.protocol.client.types.RolePlayNpcType;
import org.shivas.protocol.client.types.RolePlaySellerType;
import org.d2j.game.game.statistics.CharacteristicType;
import org.d2j.game.game.statistics.ICharacteristic;
import org.d2j.game.game.statistics.IStatistics;

import java.util.Collection;

import static org.shivas.common.StringUtils.toHexOrNegative;

/**
 * User: Blackrush
 * Date: 12/11/11
 * Time: 09:32
 * IDE : IntelliJ IDEA
 */
public class GameMessageFormatter {
    public static String gameCreationSuccessMessage(){
        return "GCK|1|";
    }

    public static String mapDataMessage(int id, String date, String key){
        return "GDM|" + id + "|" + date + "|" + key;
    }

    public static String fightCountMessage(int fights){
        return "fC" + fights;
    }

    public static String mapLoadedMessage(){
        return "GDK";
    }

    public static String statisticsMessage
            (long currentExperience, long minExp, long maxExp,
             long kamas,
             short statsPoints, short spellsPoints,
             int alignId, short alignLevel, short alignGrade, int honor, int dishonor, boolean pvpEnabled,
             short lifePoints, short maxLifePoints,
             short energy, short maxEnergy,
             short initiative,
             short prospection,
             IStatistics statistics)
    {
        CharacteristicType[] characs = CharacteristicType.values();
        ICharacteristic actionPoints = statistics.get(CharacteristicType.ActionPoints),
                        movementPoints = statistics.get(CharacteristicType.MovementPoints);

        StringBuilder sb = new StringBuilder(500).append("As");

        sb.append(currentExperience).append(',').append(minExp).append(',').append(maxExp).append('|');
        sb.append(kamas).append('|');
        sb.append(statsPoints).append('|').append(spellsPoints).append('|');
        sb.append(alignId).append('~').append(alignId).append(',')
          .append(alignLevel).append(',')
          .append(alignGrade).append(',')
          .append(honor).append(',')
          .append(dishonor).append(',')
          .append(pvpEnabled ? "1|" : "0|");
        sb.append(lifePoints).append(',').append(maxLifePoints).append('|');
        sb.append(energy).append(',').append(maxEnergy).append('|');
        sb.append(initiative).append('|')
          .append(prospection).append('|');

        sb.append(actionPoints.getBase()).append(',')
          .append(actionPoints.getEquipments()).append(',')
          .append(actionPoints.getGifts()).append(',')
          .append(actionPoints.getContext()).append(',')
          .append(actionPoints.getSafeTotal()).append('|');

        sb.append(movementPoints.getBase()).append(',')
          .append(movementPoints.getEquipments()).append(',')
          .append(movementPoints.getGifts()).append(',')
          .append(movementPoints.getContext()).append(',')
          .append(movementPoints.getSafeTotal()); // no |  => see for-loop

        for (int i = 4; i < characs.length; ++i){
            ICharacteristic charac = statistics.get(characs[i]);

            sb.append('|');
            sb.append(charac.getBase()).append(',');
            sb.append(charac.getEquipments()).append(',');
            sb.append(charac.getGifts()).append(',');
            sb.append(charac.getContext()).append(',');
        }

        return sb.toString();
    }

    public static String showActorMessage(BaseRolePlayActorType actor){
        StringBuilder sb = new StringBuilder(40).append("GM|+");
        parseBaseRolePlayActorType(sb, actor);
        return sb.toString();
    }

    public static String updateActorMessage(BaseRolePlayActorType actor){
        StringBuilder sb = new StringBuilder(40).append("GM|~");
        parseBaseRolePlayActorType(sb, actor);
        return sb.toString();
    }

    public static String showActorsMessage(Collection<? extends BaseRolePlayActorType> actors){
        StringBuilder sb = new StringBuilder(10 + 30 * actors.size()).append("GM");
        for (BaseRolePlayActorType actor : actors){
            sb.append("|+");
            parseBaseRolePlayActorType(sb, actor);
        }
        return sb.toString();
    }

    private static void parseBaseRolePlayActorType(StringBuilder sb, BaseRolePlayActorType actor){
        sb.append(actor.getCurrentCellId()).append(';')
          .append(actor.getCurrentOrientation().ordinal()).append(';')
          .append("0;")
          .append(actor.getId()).append(';');

        if (actor instanceof RolePlayCharacterType){
            parseRolePlayCharacterType(sb, (RolePlayCharacterType) actor);
        }
        else if (actor instanceof RolePlayNpcType){
            parseRolePlayNpcType(sb, (RolePlayNpcType) actor);
        }
        else if (actor instanceof RolePlaySellerType){
            parseRolePlaySellerType(sb, (RolePlaySellerType) actor);
        }
    }

    private static void parseRolePlayCharacterType(StringBuilder sb, RolePlayCharacterType player){
        sb.append(player.getName()).append(';')
          .append(player.getBreedId()).append(';')
          .append(player.getSkin()).append('^').append(player.getSize()).append(';')
          .append(player.isGender() ? '1' : '0').append(';');

        sb.append("0,0,0,0").append(';'); //todo alignment

        sb.append(toHexOrNegative(player.getColor1())).append(';')
          .append(toHexOrNegative(player.getColor2())).append(';')
          .append(toHexOrNegative(player.getColor3())).append(';');

        ItemGameMessageFormatter.parseAccessories(sb, player.getAccessories());
        sb.append(';');

        sb.append(player.getLevel() >= 100 ? (player.getLevel() == 200 ? '2' : '1') : '0');

        sb.append(';')
          .append(';');

        if (player.hasGuild()){
            sb.append(player.getGuildName()).append(';')
              .append(player.getGuildEmblem().toString());
        }
        else{
            sb.append(";;");
        }

        sb.append("0;;");
    }

    private static void parseRolePlayNpcType(StringBuilder sb, RolePlayNpcType npc){
        sb.append(npc.getTemplateId()).append(';');
        sb.append("-4;"); // npc type = -4
        sb.append(npc.getSkin()).append('^');
        if (npc.sameScale()) sb.append(npc.getSize()).append(';');
        else sb.append(npc.getScaleX()).append('x').append(npc.getScaleY()).append(';');
        sb.append(npc.getGender() ? '1' : '0').append(';');
        sb.append(toHexOrNegative(npc.getColor1())).append(';');
        sb.append(toHexOrNegative(npc.getColor2())).append(';');
        sb.append(toHexOrNegative(npc.getColor3())).append(';');

        ItemGameMessageFormatter.parseAccessories(sb, npc.getAccessories());
        sb.append(';');

        sb.append(npc.getExtraClip()).append(';');
        sb.append(npc.getCustomArtwork());
    }

    private static void parseRolePlaySellerType(StringBuilder sb, RolePlaySellerType seller){
    	sb.append(seller.getName()).append(";");
    	sb.append("-5").append(";");
    	sb.append(seller.getSkin()).append("^").append(seller.getSize()).append(";");
        sb.append(toHexOrNegative(seller.getColor1())).append(';');
        sb.append(toHexOrNegative(seller.getColor2())).append(';');
        sb.append(toHexOrNegative(seller.getColor3())).append(';');

        ItemGameMessageFormatter.parseAccessories(sb, seller.getAccessories());
        sb.append(';');

        if (seller.hasGuild()){
            sb.append(seller.getGuildName()).append(';')
              .append(seller.getGuildEmblem().toString());
        }
        else{
            sb.append(";;");
        }

    	sb.append("0;");
    }

    public static String removeActorMessage(Long id) {
        return "GM|-" + id;
    }

    public static String noActionMessage() {
        return "GA;0";
    }

    public static String actorMovementMessage(long actorId, String path){
        return "GA1;1;" + actorId + ";" + path;
    }

    public static String changeMapMessage(long actorId){
        return "GA;2;" + actorId + ";";
    }

    public static String challengeRequestMessage(long senderId, long challengedId){
        return "GA;900;" + senderId + ";" + challengedId;
    }

    public static String challengeAcceptedMessage(long senderId, long challengedId){
        return "GA;901;" + senderId + ";" + challengedId;
    }

    public static String challengeDeclinedMessage(long senderId, long challengedId){
        return "GA;902;" + senderId + ";" + challengedId;
    }

    public static String kickMessage(String name, String reason){
        return "M018|" + name + "; " + reason;
    }
}
