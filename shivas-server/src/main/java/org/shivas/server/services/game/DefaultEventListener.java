package org.shivas.server.services.game;

import org.apache.mina.core.session.IoSession;
import org.shivas.protocol.client.formatters.ChannelGameMessageFormatter;
import org.shivas.protocol.client.formatters.GameMessageFormatter;
import org.shivas.server.core.actions.Action;
import org.shivas.server.core.actions.RolePlayMovement;
import org.shivas.server.core.channels.ChannelEvent;
import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventListener;
import org.shivas.server.core.events.events.PlayerTeleportationEvent;
import org.shivas.server.core.events.events.PrivateMessageEvent;
import org.shivas.server.core.maps.MapEvent;

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
			listenChannel((ChannelEvent) event);
			break;
			
		case MAP:
			listenMap((MapEvent) event);
			break;
			
		case TELEPORTATION:
			listenTeleportation((PlayerTeleportationEvent) event);
			break;
			
		case PRIVATE_MESSAGE:
			listenPrivateMessage((PrivateMessageEvent) event);
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

	private void listenChannel(ChannelEvent event) {
		session.write(ChannelGameMessageFormatter.clientMultiMessage(
				event.channel(),
				event.author().id(),
				event.author().getName(),
				event.message()
		));
	}

	private void listenMap(MapEvent event) {
		switch (event.mapEventType()) {
		case ENTER:
			session.write(GameMessageFormatter.showActorMessage(event.actor().toBaseRolePlayActorType()));
			break;
			
		case LEAVE:
			session.write(GameMessageFormatter.removeActorMessage(event.actor().id()));
			break;
			
		case UPDATE:
			session.write(GameMessageFormatter.updateActorMessage(event.actor().toBaseRolePlayActorType()));
			break;
		}
	}
	
	private void listenTeleportation(PlayerTeleportationEvent event) {
		if (event.getPlayer() != client.player()) return;
		
		// TODO
	}

	private void listenPrivateMessage(PrivateMessageEvent event) {
		if (event.source() != client.player() && event.target() != client.player()) return; // this client can't see other's private message
		
		if (event.source() == client.player()) { // this client is the pm's author
			session.write(ChannelGameMessageFormatter.clientPrivateMessage(
					false,
					event.target().id(),
					event.target().getName(),
					event.message()
			));
		} else { // this client is the pm's target
			session.write(ChannelGameMessageFormatter.clientPrivateMessage(
					true,
					event.source().id(),
					event.source().getName(),
					event.message()
			));
		}
	}

}
