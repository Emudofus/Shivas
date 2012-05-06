package org.shivas.server.core.maps;

import org.shivas.server.core.GameActor;

public class BaseMapEvent implements MapEvent {
	
	private GameActor actor;
	private MapEventType type;

	public BaseMapEvent(GameActor actor, MapEventType type) {
		this.actor = actor;
		this.type = type;
	}

	@Override
	public GameActor actor() {
		return actor;
	}

	@Override
	public MapEventType mapEventType() {
		return type;
	}

}
