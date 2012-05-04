package org.shivas.protocol.client.formatters;

import org.shivas.common.maths.LimitedValue;
import org.shivas.common.statistics.Characteristic;
import org.shivas.common.statistics.CharacteristicType;
import org.shivas.common.statistics.Statistics;
import org.shivas.protocol.client.types.BaseRolePlayActorType;
import org.shivas.protocol.client.types.RolePlayCharacterType;
import org.shivas.protocol.client.types.RolePlayNpcType;
import org.shivas.protocol.client.types.RolePlaySellerType;

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
             Statistics statistics)
    {
        Characteristic actionPoints = statistics.get(CharacteristicType.ActionPoints),
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

        sb.append(actionPoints.base()).append(',')
          .append(actionPoints.equipment()).append(',')
          .append(actionPoints.gift()).append(',')
          .append(actionPoints.context()).append(',')
          .append(actionPoints.safeTotal()).append('|');

        sb.append(movementPoints.base()).append(',')
          .append(movementPoints.equipment()).append(',')
          .append(movementPoints.gift()).append(',')
          .append(movementPoints.context()).append(',')
          .append(movementPoints.safeTotal());

        Characteristic charac = statistics.get(CharacteristicType.Strength);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.Vitality);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.Wisdom);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.Chance);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.Agility);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.Intelligence);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.RangePoints);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.Summons);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.Damage);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.PhysicalDamage);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.WeaponControl);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.DamagePer);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.HealPoints);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.TrapDamage);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.TrapDamagePer);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.DamageReturn);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.CriticalHit);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.CriticalFailure);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.DodgeActionPoints);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.DodgeMovementPoints);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistanceNeutral);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistancePercentNeutral);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistancePvpNeutral);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistancePercentPvpNeutral);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistanceEarth);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistancePercentEarth);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistancePvpEarth);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistancePercentPvpEarth);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistanceWater);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistancePercentWater);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistancePvpWater);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistancePercentPvpWater);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistanceWind);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistancePercentWind);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistancePvpWind);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistancePercentPvpWind);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistanceFire);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistancePercentFire);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistancePvpFire);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context()).append('|');
        charac = statistics.get(CharacteristicType.ResistancePercentPvpFire);
        sb.append(charac.base()).append(',').append(charac.equipment()).append(',').append(charac.gift()).append(',').append(charac.context());

        return sb.toString();
    }
    
    public static String statisticsMessage
    	(long currentExperience, long minExp, long maxExp,
	     long kamas,
	     short statsPoints, short spellsPoints,
	     int alignId, short alignLevel, short alignGrade, int honor, int dishonor, boolean pvpEnabled,
	     LimitedValue life, LimitedValue energy,
	     Statistics statistics)
    {
    	return statisticsMessage(
    			currentExperience, minExp, maxExp,
    			kamas, statsPoints, spellsPoints, 
    			alignId, alignLevel, alignGrade, honor, dishonor, pvpEnabled,
    			(short)life.current(), (short)life.max(),
    			(short)energy.current(), (short)energy.max(),
    			statistics.get(CharacteristicType.Initiative).total(),
    			statistics.get(CharacteristicType.Prospection).total(),
    			statistics
    	);
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
          .append(player.getGender().ordinal()).append(';');

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
