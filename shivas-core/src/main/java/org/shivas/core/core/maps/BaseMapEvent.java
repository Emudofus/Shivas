package org.shivas.core.core.maps;

import org.shivas.core.core.GameActor;
import org.shivas.core.core.events.EventType;

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
