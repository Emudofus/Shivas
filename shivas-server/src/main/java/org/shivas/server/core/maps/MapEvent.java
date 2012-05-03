package org.shivas.server.core.maps;

import org.shivas.server.core.GameEvent;

public interface MapEvent extends GameEvent {
	MapEventType type();
}
