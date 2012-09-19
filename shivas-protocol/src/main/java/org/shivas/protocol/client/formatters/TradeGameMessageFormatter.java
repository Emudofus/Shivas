package org.shivas.protocol.client.formatters;

import org.shivas.protocol.client.enums.TradeErrorEnum;
import org.shivas.protocol.client.enums.TradeTypeEnum;
import org.shivas.protocol.client.types.*;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 13/01/12
 * Time: 19:19
 */
public class TradeGameMessageFormatter {
    public static String startTradeMessage(TradeTypeEnum type){
        return "ECK" + type.value();
    }

    public static String startTradeMessage(TradeTypeEnum type, long traderId){
        return "ECK" + type.value() + "|" + traderId;
    }

    public static String tradeRequestErrorMessage(TradeErrorEnum error){
        return "ERE" + error.value();
    }

    public static String tradeRequestMessage(long sourceId, long targetId, TradeTypeEnum type){
        return "ERK" + sourceId + "|" + targetId + "|" + type.value();
    }

    public static String tradeQuitMessage(){
        return "EV";
    }

    public static String tradeSetKamasMessage(long kamas, boolean local) {
        return "E" + (local ? "M" : "m") + "KG" + kamas;
    }

    public static String tradeReadyMessage(boolean ready, long sourceId){
        return "EK" + (ready ? "1" : "0") + sourceId;
    }

    public static String tradePutItemMessage(BaseItemType item, boolean local){
        StringBuilder sb = new StringBuilder(local ? "EMKO+" : "EmKO+");

        sb.append(item.getId()).append('|');
        sb.append(item.getQuantity());

        if (!local) {
            sb.append('|').append(item.getTemplateId());
            sb.append('|');

            boolean first = true;
            for (BaseItemEffectType effect : item.getEffects()){
                if (first) first = false;
                else sb.append(',');

                sb.append(Integer.toHexString(effect.getEffect().value())).append('#');
                sb.append(Integer.toHexString(effect.getBonus())).append('#');
                sb.append("0#0#0d0+0");
            }
        }

        return sb.toString();
    }

    public static String tradeRemoveItemMessage(long itemId, boolean local){
        return "E" + (local ? "M" : "m") + "KO-" + itemId;
    }

    public static String tradeSuccessMessage(){
        return "EVa";
    }

    public static String itemListMessage(Collection<BaseItemTemplateType> items){
        StringBuilder sb = new StringBuilder().append("EL");

        boolean first = true;
        for (BaseItemTemplateType item : items){
            if (first) first = false;
            else sb.append('|');

            sb.append(item.getId()).append(';');

            boolean first2 = true;
            for (BaseItemTemplateEffectType effect : item.getEffects()){
                if (first2) first2 = false;
                else sb.append(',');

                sb.append(effect.getEffect() != null ? Integer.toHexString(effect.getEffect().value()) : "0").append('#');
                if (effect.getDice().min() == effect.getDice().max()){
                    sb.append(Integer.toHexString(effect.getDice().max())).append('#');
                    sb.append('#');
                }
                else{
                    sb.append(Integer.toHexString(effect.getDice().min())).append('#');
                    sb.append(Integer.toHexString(effect.getDice().max())).append('#');
                }
                sb.append('#');
                sb.append(effect.getDice().toString());
            }
        }

        return sb.toString();
    }

    public static String buySuccessMessage(){
        return "EBK";
    }

    public static String buyErrorMessage() {
        return "EBE";
    }

    public static String sellSuccessMessage(){
        return "ESK";
    }

    public static String sellErrorMessage() {
        return "ESE";
    }

    public static String storedItemsListMessage(Collection<StoreItemType> items){
        StringBuilder sb = new StringBuilder().append("EL");

        boolean first1 = true;
        for (StoreItemType item : items){
            if (first1) first1 = false;
            else sb.append('|');

            sb.append(item.getId()).append(';');
            sb.append(item.getQuantity()).append(';');
            sb.append(item.getTemplateId()).append(';');

            boolean first2 = true;
            for (BaseItemEffectType effect : item.getEffects()){
                if (first2) first2 = false;
                else sb.append(',');

                sb.append(Integer.toHexString(effect.getEffect().value())).append('#');
                sb.append(Integer.toHexString(effect.getBonus())).append('#');
                sb.append("0#0#0d0+0");
            }
            sb.append(';');

            sb.append(item.getPrice());
        }

        return sb.toString();
    }
}
