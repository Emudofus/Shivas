package org.shivas.server.core.interactions;

import org.shivas.server.core.events.Event;

import com.google.common.util.concurrent.ListenableFuture;

public interface Interaction extends Event {
	InteractionType getInteractionType();
	
	void begin() throws InteractionException;
	void end() throws InteractionException;
	void cancel() throws InteractionException;
	
	ListenableFuture<Interaction> endFuture();
}
