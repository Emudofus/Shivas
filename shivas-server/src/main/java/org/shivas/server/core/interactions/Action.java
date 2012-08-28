package org.shivas.server.core.interactions;

import org.shivas.server.core.events.Event;

import com.google.common.util.concurrent.ListenableFuture;

public interface Action extends Event {
	ActionType actionType();
	
	void begin() throws ActionException;
	void end() throws ActionException;
	void cancel() throws ActionException;
	
	ListenableFuture<Action> endFuture();
}
