package org.shivas.server.core.actions;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

public abstract class AbstractAction implements Action {
	
	private final SettableFuture<Action> endFuture = SettableFuture.create();
	
	protected abstract void internalEnd() throws ActionException;

	@Override
	public void end() throws ActionException {
		internalEnd();
		
		endFuture.set(this);
	}

	@Override
	public ListenableFuture<Action> endFuture() {
		return endFuture;
	}

}
