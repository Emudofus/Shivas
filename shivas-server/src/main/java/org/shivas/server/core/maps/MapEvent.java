package org.shivas.server.core.maps;

import org.shivas.server.core.GameActor;
import org.shivas.server.core.events.Event;

public interface MapEvent extends Event {
	MapEventType mapEventType();
	
	GameActor actor();
}
