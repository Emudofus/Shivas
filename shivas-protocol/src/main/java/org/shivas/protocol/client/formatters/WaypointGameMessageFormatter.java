package org.shivas.protocol.client.formatters;

import java.util.Collection;

import org.shivas.protocol.client.types.BaseWaypointType;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 22/02/12
 * Time: 19:50
 * To change this template use File | Settings | File Templates.
 */
public class WaypointGameMessageFormatter {
    public static String closePanelMessage(){
        return "WV";
    }

    public static String listMessage(int currentMapId, Collection<BaseWaypointType> waypoints){
        StringBuilder sb = new StringBuilder().append("WC");

        sb.append(currentMapId);

        for (BaseWaypointType waypoint : waypoints){
            sb.append('|');

            sb.append(waypoint.getMapId()).append(';');
            sb.append(waypoint.getCost());
        }

        return sb.toString();
    }
}
