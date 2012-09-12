package org.shivas.server.core.waypoints;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.shivas.data.Repository;
import org.shivas.data.entity.MapTemplate;
import org.shivas.data.entity.Waypoint;
import org.shivas.protocol.client.types.BaseWaypointType;
import org.shivas.server.database.models.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;

public class WaypointList implements Iterable<Waypoint> {
	
	private static Logger log = LoggerFactory.getLogger(WaypointList.class);

	public static long getCost(MapTemplate m1, MapTemplate m2){
        return 10 * (
        	   Math.abs(
        			   m1.getPosition().abscissa() -
        			   m2.getPosition().abscissa()
        	   ) + Math.abs(
        			   m1.getPosition().ordinate() -
        			   m2.getPosition().ordinate()
        	   ) - 1
    	);
    }
	
	public static WaypointList populate(Repository<Waypoint> waypoints, WaypointList list, String string) {
		for (String id : string.split(",")) {
			if (id.length() <= 0) continue;
			
			Waypoint waypoint = waypoints.byId(Integer.parseInt(id));
			if (waypoint != null) {
				list.add(waypoint);
			} else {
				log.error("unknown waypoint {}", id);
			}
		}
		return list;
	}
	
	public static String serialize(WaypointList list) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Waypoint waypoint : list) {
			if (first) first = false;
			else sb.append(',');
			sb.append(waypoint.getId());
		}
		return sb.toString();
	}
	
	private final Player owner;
	private final Map<Integer, Waypoint> waypoints = Maps.newHashMap();
	
	public WaypointList(Player owner) {
		this.owner = owner;
	}
	
	public void add(Waypoint waypoint) {
		waypoints.put(waypoint.getMap().getId(), waypoint);
	}
	
	public Waypoint get(int mapId) {
		return waypoints.get(mapId);
	}

	public boolean contains(Waypoint waypoint) {
		return waypoints.containsKey(waypoint.getMap().getId());
	}

	public Collection<BaseWaypointType> toBaseWaypointType() {
		return Collections2.transform(
				waypoints.values(),
				new Function<Waypoint, BaseWaypointType>() {
					public BaseWaypointType apply(Waypoint input) {
						return new BaseWaypointType(
								input.getMap().getId(),
								getCost(input.getMap(), owner.getLocation().getMap())
						);
					}
				}
		);
	}

	@Override
	public Iterator<Waypoint> iterator() {
		return waypoints.values().iterator();
	}

}
