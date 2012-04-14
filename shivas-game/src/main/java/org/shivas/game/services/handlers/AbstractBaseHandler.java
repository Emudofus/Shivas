package org.shivas.game.services.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.game.services.GameClient;

public abstract class AbstractBaseHandler implements IoSessionHandler<String> {

	protected final GameClient client;
	protected final IoSession  session;
	
	public AbstractBaseHandler(GameClient client, IoSession session) {
		this.client  = client;
		this.session = session;
	}

}
