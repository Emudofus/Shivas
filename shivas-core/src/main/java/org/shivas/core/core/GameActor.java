package org.shivas.core.core;

import org.shivas.protocol.client.types.BaseRolePlayActorType;
import org.shivas.core.core.maps.GameMap;

public interface GameActor {
	int getPublicId();
	
	String getName();
	Location getLocation();
	Look getLook();
	
	void teleport(GameMap map, short cell);
	
	BaseRolePlayActorType toBaseRolePlayActorType();
}
