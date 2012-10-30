package org.shivas.core.core.interactions;

import org.shivas.core.core.events.EventType;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

public abstract class AbstractInteraction implements Interaction {
	
	private final SettableFuture<Interaction> endFuture = SettableFuture.create();
	
	protected abstract void internalEnd() throws InteractionException;
	
	public final EventType type() {
		return EventType.INTERACTION;
	}

	public void end() throws InteractionException {
		internalEnd();
		
		endFuture.set(this);
	}

	public ListenableFuture<Interaction> endFuture() {
		return endFuture;
	}

}
