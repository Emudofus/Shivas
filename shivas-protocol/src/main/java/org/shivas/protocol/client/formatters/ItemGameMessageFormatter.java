package org.shivas.protocol.client.formatters;

import org.shivas.common.StringUtils;
import org.shivas.common.maths.LimitedValue;
import org.shivas.protocol.client.enums.ItemPositionEnum;
import org.shivas.protocol.client.types.BaseItemEffectType;
import org.shivas.protocol.client.types.BaseItemType;

import java.util.Collection;

/**
 * User: Blackrush
 * Date: 06/11/11
 * Time: 10:15
 * IDE:  IntelliJ IDEA
 */
public class ItemGameMessageFormatter {
    public static void parseAccessories(StringBuilder sb, int[] accessories){
    	if (accessories == null) {
    		sb.append(",,,,");
    		return;
    	}
    	
        boolean first = true;
        for (int accessory : accessories){
            if (first) first = false;
            else sb.append(',');

            sb.append(StringUtils.toHexOr(accessory > 0, accessory, ""));
        }
    }

    public static void parseAccessories(StringBuilder sb, Iterable<Integer> accessories){
    	if (accessories == null) {
    		sb.append(",,,,");
    		return;
    	}
    	
        boolean first = true;
        for (Integer accessory : accessories){
            if (first) first = false;
            else sb.append(',');

            sb.append(StringUtils.toHexOr(accessory != null && accessory > 0, accessory, ""));
        }
    }

    public static String inventoryStatsMessage(int usedPods, int maxPods){
        return "Ow" + usedPods + "|" + maxPods;
    }
    
    public static String inventoryStatsMessage(LimitedValue pods) {
    	return "Ow" + pods.current() + "|" + pods.max();
    }

    private static void formatItem(StringBuilder sb, BaseItemType item){
        sb.append(Long.toHexString(item.getId())).append('~');
        sb.append(Integer.toHexString(item.getTemplateId())).append('~');
        sb.append(Integer.toHexString(item.getQuantity())).append('~');
        sb.append(item.getPosition() != ItemPositionEnum.NotEquiped ? Integer.toHexString(item.getPosition().value()) : "").append('~');

        boolean first = true;
        for (BaseItemEffectType effect : item.getEffects()){
            if (first) first = false;
            else sb.append(',');

            sb.append(Integer.toHexString(effect.getEffect().value())).append('#');
            if (effect.getEffect().isWeaponEffect()) {
            	sb.append(Integer.toHexString(effect.getDice().min())).append('#');
            	sb.append(Integer.toHexString(effect.getDice().max())).append('#');
                sb.append("0#");
                sb.append(effect.getDice().toString());
            } else {
                sb.append(Integer.toHexString(effect.getBonus())).append('#');
                sb.append("0#0#0d0+0");
            }
        }
    }

    public static void formatItems(StringBuilder sb, Collection<BaseItemType> items){
    	if (items == null) return;
        boolean first = true;
        for (BaseItemType item : items){
            if (first) first = false;
            else sb.append(';');

            formatItem(sb, item);
        }
    }

    public static String addItemMessage(BaseItemType item){
        StringBuilder sb = new StringBuilder().append("OAKO");
        formatItem(sb, item);
        return sb.toString();
    }

    public static String quantityMessage(long itemId, int quantity) {
        return "OQ" + itemId + "|" + quantity;
    }

    public static String deleteMessage(long itemId) {
        return "OR" + itemId;
    }

    public static String itemMovementMessage(long itemId, ItemPositionEnum position){
        return "OM" + itemId + "|" + (position != ItemPositionEnum.NotEquiped ? position.value() : "");
    }

    public static String alreadyEquipedMessage() {
        return "OMEA";
    }

    public static String tooSlowLevelMessage() {
        return "OMEL";
    }

    public static String fullInventoryMessage() {
        return "OMEF";
    }

    public static String accessoriesMessage(long playerId, int[] accessories){
        StringBuilder sb = new StringBuilder().append("Oa");

        sb.append(playerId).append('|');

        boolean first = true;
        for (int accessory : accessories){
            if (first) first = false;
            else sb.append(',');

            sb.append(accessory != -1 ? Integer.toHexString(accessory) : "");
        }

        return sb.toString();
    }

    public static String addItemSetMessage(short itemSetId, Collection<BaseItemType> items, Collection<BaseItemEffectType> effects) {
        StringBuilder sb = new StringBuilder().append("OS+");

        sb.append(itemSetId).append('|');

        boolean first = true;
        for (BaseItemType item : items){
            if (first) first = false;
            else sb.append(';');

            sb.append(item.getTemplateId());
        }
        sb.append('|');

        first = true;
        for (BaseItemEffectType effect : effects){
            if (first) first = false;
            else sb.append(',');

            sb.append(Integer.toHexString(effect.getEffect().value())).append('#');
            sb.append(Integer.toHexString(effect.getBonus())).append('#');
            sb.append("0#");
            sb.append("0");
        }

        return sb.toString();
    }

    public static String removeItemSetMessage(short itemSetId) {
        return "OS-" + itemSetId;
    }
}
