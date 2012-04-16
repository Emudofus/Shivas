package org.shivas.server.services.game.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;
import org.shivas.server.services.AbstractBaseHandlerContainer;
import org.shivas.server.services.game.GameClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RolePlayHandler extends AbstractBaseHandlerContainer<GameClient> {
	
	private static final Logger log = LoggerFactory.getLogger(RolePlayHandler.class);

	public RolePlayHandler(GameClient client, IoSession session) {
		super(client, session);
	}

	@Override
	protected void configure() {
	}

	@Override
	protected void onReceivedUnknownMessage(String message) {
		log.debug("unknown message {}", message);
		
		session.write(BasicGameMessageFormatter.noOperationMessage());
	}

}
