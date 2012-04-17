package org.shivas.server.services.game.handlers;

import org.apache.mina.core.session.IoSession;
import org.joda.time.DateTime;
import org.shivas.common.services.IoSessionHandler;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;

public class BasicHandler extends AbstractBaseHandler<GameClient> {

	public BasicHandler(GameClient client, IoSession session) {
		super(client, session);
	}

	public IoSessionHandler<String> init() throws Exception {
		return this;
	}

	public void handle(String message) throws Exception {
		switch (message.charAt(1)) {
		case 'D':
			parseCurrentDateMessage();
			break;
		}
	}

	private void parseCurrentDateMessage() {
		session.write(BasicGameMessageFormatter.currentDateMessage(DateTime.now()));
	}

	public void onClosed() {
	}

}
