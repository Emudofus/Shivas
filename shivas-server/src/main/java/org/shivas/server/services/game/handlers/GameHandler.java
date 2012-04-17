package org.shivas.server.services.game.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;

public class GameHandler extends AbstractBaseHandler<GameClient> {

	public GameHandler(GameClient client, IoSession session) {
		super(client, session);
	}

	public IoSessionHandler<String> init() throws Exception {
		return this;
	}

	public void handle(String message) throws Exception {
		switch (message.charAt(1)) {
		case 'C':
			parseGameCreationMessage();
			break;
			
		case 'I':
			parseGameInformationsMessage();
			break;
		}
	}

	public void onClosed() {
	}

	private void parseGameCreationMessage() {
		// TODO Auto-generated method stub
		
	}

	private void parseGameInformationsMessage() {
		// TODO Auto-generated method stub
		
	}

}
