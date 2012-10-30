package org.shivas.core.core.interactions;

import org.shivas.core.services.game.GameClient;

public abstract class Invitation extends AbstractInteraction implements LinkedInteraction, Acceptable, Declinable {
	protected final GameClient source, target;
	
	public Invitation(GameClient source, GameClient target) {
		this.source = source;
		this.target = target;
	}

    protected void writeToAll(Object message) {
        source.write(message);
        target.write(message);
    }
	
	@Override
	public GameClient getSource() {
		return source;
	}

	@Override
	public GameClient getTarget() {
		return target;
	}

	public abstract void accept() throws InteractionException;
	public abstract void decline() throws InteractionException;
	
	@Override
	protected void internalEnd() throws InteractionException {
		decline();
	}
	
	@Override
	public void cancel() throws InteractionException {
		decline();
	}
	
}
