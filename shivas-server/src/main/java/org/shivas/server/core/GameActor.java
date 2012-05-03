package org.shivas.server.core;

import org.shivas.protocol.client.types.BaseRolePlayActorType;

public interface GameActor {
	Integer id();
	
	BaseRolePlayActorType toBaseRolePlayActorType();
}
