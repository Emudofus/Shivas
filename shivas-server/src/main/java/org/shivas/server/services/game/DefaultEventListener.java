package org.shivas.server.services.game;

import org.apache.mina.core.session.IoSession;
import org.shivas.protocol.client.formatters.GameMessageFormatter;
import org.shivas.server.core.actions.Action;
import org.shivas.server.core.actions.RolePlayMovement;
import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventListener;

public class DefaultEventListener implements EventListener {
	
	private final GameClient client;
	private final IoSession session;

	public DefaultEventListener(GameClient client, IoSession session) {
		this.client = client;
		this.session = session;
	}

	public void listen(Event event) {
		switch (event.type()) {
		case ACTION:
			listenAction((Action) event);
			break;
			
		case CHANNEL:
			break;
			
		case MAP:
			break;
		}
	}
	
	private void listenAction(Action action) {
		switch (action.actionType()) {
		case MOVEMENT:
			RolePlayMovement movement = (RolePlayMovement) action;
			session.write(GameMessageFormatter.actorMovementMessage(
					movement.actor().id().longValue(),
					movement.path().toString()
			));
			break;
		}
	}

}
