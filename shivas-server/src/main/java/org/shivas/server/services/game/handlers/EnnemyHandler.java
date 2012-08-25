package org.shivas.server.services.game.handlers;

import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;

public class EnnemyHandler extends AbstractBaseHandler<GameClient> {

	public EnnemyHandler(GameClient client) {
		super(client);
	}

	@Override
	public void init() throws Exception {
	}

	@Override
	public void onClosed() {
	}

	@Override
	public void handle(String message) throws Exception {
		switch (message.charAt(1)) {
		case 'L':
			parseListMessage();
			break;
		}
	}

	private void parseListMessage() {
		// TODO enemies
	}

}
