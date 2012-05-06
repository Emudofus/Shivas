package org.shivas.server.core.actions;

import org.shivas.server.core.GameEvent;

import com.google.common.util.concurrent.ListenableFuture;

public interface Action extends GameEvent {
	ActionType actionType();
	
	void begin() throws ActionException;
	void end() throws ActionException;
	void cancel() throws ActionException;
	
	ListenableFuture<Action> endFuture();
}
