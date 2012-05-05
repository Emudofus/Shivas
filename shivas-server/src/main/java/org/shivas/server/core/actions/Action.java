package org.shivas.server.core.actions;

import org.shivas.server.core.GameEvent;

public interface Action extends GameEvent {
	ActionType type();
	
	void begin() throws ActionException;
	void end() throws ActionException;
	void cancel() throws ActionException;
}
