package org.shivas.server.core;

import org.shivas.protocol.client.types.BaseRolePlayActorType;
import org.shivas.server.core.maps.GMap;

public interface GameActor {
	Integer id();
	String getName();
	Location getLocation();
	
	void teleport(GMap map, short cell);
	
	BaseRolePlayActorType toBaseRolePlayActorType();
}
