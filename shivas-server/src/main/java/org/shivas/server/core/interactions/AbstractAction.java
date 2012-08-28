package org.shivas.server.core.interactions;

import org.shivas.server.core.events.EventType;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

public abstract class AbstractAction implements Action {
	
	private final SettableFuture<Action> endFuture = SettableFuture.create();
	
	protected abstract void internalEnd() throws ActionException;
	
	public final EventType type() {
		return EventType.ACTION;
	}

	public void end() throws ActionException {
		internalEnd();
		
		endFuture.set(this);
	}

	public ListenableFuture<Action> endFuture() {
		return endFuture;
	}

}
