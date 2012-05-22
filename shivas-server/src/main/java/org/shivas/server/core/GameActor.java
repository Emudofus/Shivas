package org.shivas.server.core;

import org.shivas.protocol.client.types.BaseRolePlayActorType;
import org.shivas.server.core.maps.GameMap;

public interface GameActor {
	Integer id();
	String getName();
	Location getLocation();
	
	void teleport(GameMap map, short cell);
	
	BaseRolePlayActorType toBaseRolePlayActorType();
}
