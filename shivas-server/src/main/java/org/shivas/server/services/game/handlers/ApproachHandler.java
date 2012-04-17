package org.shivas.server.services.game.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;

public class ApproachHandler extends AbstractBaseHandler<GameClient> {

	public ApproachHandler(GameClient client, IoSession session) {
		super(client, session);
	}

	public IoSessionHandler<String> init() throws Exception {
		return this;
	}

	public void handle(String message) throws Exception {
	}

	public void onClosed() {
	}

}
