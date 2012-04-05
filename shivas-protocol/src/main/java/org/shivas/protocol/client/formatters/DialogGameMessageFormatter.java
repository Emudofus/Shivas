package org.shivas.protocol.client.formatters;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 27/01/12
 * Time: 23:56
 */
public class DialogGameMessageFormatter {
    public static String dialogSuccessMessage(int npcId){
        return "DCK" + npcId;
    }

    public static String questionMessage(int id, String[] arguments, Collection<Integer> responses){
        StringBuilder sb = new StringBuilder().append("DQ");

        sb.append(id);

        if (arguments != null && arguments.length > 0){
            for (String argument : arguments){
                sb.append(';');
                sb.append(argument);
            }
        }

        sb.append('|');

        boolean first = true;
        for (Integer response : responses){
            if (first) first = false;
            else sb.append(';');
            sb.append(response);
        }

        return sb.toString();
    }

    public static String dialogEndMessage(){
        return "DV";
    }
}
