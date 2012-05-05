package org.shivas.server.core.actions;

public interface Action {
	ActionType type();
	
	void begin() throws ActionException;
	void end() throws ActionException;
	void cancel() throws ActionException;
}
