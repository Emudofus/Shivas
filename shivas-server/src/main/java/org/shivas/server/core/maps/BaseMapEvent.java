package org.shivas.server.core.maps;

import org.shivas.server.core.GameActor;
import org.shivas.server.core.events.EventType;

public class BaseMapEvent implements MapEvent {
	
	private GameActor actor;
	private MapEventType type;

	public BaseMapEvent(GameActor actor, MapEventType type) {
		this.actor = actor;
		this.type = type;
	}

	@Override
	public EventType type() {
		return EventType.MAP;
	}

	public MapEventType mapEventType() {
		return type;
	}

	public GameActor actor() {
		return actor;
	}

}
