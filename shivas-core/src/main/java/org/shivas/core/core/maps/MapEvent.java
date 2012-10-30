package org.shivas.core.core.maps;

import org.shivas.core.core.GameActor;
import org.shivas.core.core.events.Event;

public interface MapEvent extends Event {
	MapEventType mapEventType();
	
	GameActor actor();
}
