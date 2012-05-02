package org.shivas.server.services.game.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.common.services.IoSessionHandler;
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
	public IoSessionHandler<String> init() throws Exception {
		return super.init();
	}

	@Override
	public void onClosed() {
		super.onClosed();
		
		client.account().setConnected(false);
		client.service().repositories().accounts().save(client.account());
	}

	@Override
	protected void configure() {
		add('A', new ApproachHandler(client, session));
		add('B', new BasicHandler(client, session));
		add('G', new GameHandler(client, session));
	}

	@Override
	protected void onReceivedUnknownMessage(String message) {
		log.debug("unknown message {}", message);
		
		session.write(BasicGameMessageFormatter.noOperationMessage());
	}

}
