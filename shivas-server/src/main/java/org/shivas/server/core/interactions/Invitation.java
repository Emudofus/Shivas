package org.shivas.server.core.interactions;

import org.shivas.server.services.game.GameClient;

public abstract class Invitation extends AbstractAction {
	protected final GameClient source, target;
	
	public Invitation(GameClient source, GameClient target) {
		this.source = source;
		this.target = target;
	}
	
	public GameClient getSource() {
		return source;
	}

	public GameClient getTarget() {
		return target;
	}

	public abstract void accept() throws ActionException;
	public abstract void decline() throws ActionException;
	
	@Override
	protected void internalEnd() throws ActionException {
		decline();
	}
	
	@Override
	public void cancel() throws ActionException {
		decline();
	}
	
}
