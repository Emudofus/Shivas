package org.shivas.server.services.game;

import org.shivas.protocol.client.formatters.ChannelGameMessageFormatter;
import org.shivas.protocol.client.formatters.GameMessageFormatter;
import org.shivas.protocol.client.formatters.ItemGameMessageFormatter;
import org.shivas.server.core.actions.Action;
import org.shivas.server.core.actions.RolePlayMovement;
import org.shivas.server.core.channels.ChannelEvent;
import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventListener;
import org.shivas.server.core.events.events.PlayerTeleportationEvent;
import org.shivas.server.core.events.events.SystemMessageEvent;
import org.shivas.server.core.maps.MapEvent;

public class DefaultEventListener implements EventListener {
	
	private final GameClient client;

	public DefaultEventListener(GameClient client) {
		this.client = client;
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
			
		case SYSTEM_MESSAGE:
			listenSystemMessage((SystemMessageEvent) event);
			break;
			
		case NEW_ACTION: // not implemented
			break;
		}
	}

	private void listenAction(Action action) {
		switch (action.actionType()) {
		case MOVEMENT:
			RolePlayMovement movement = (RolePlayMovement) action;
			client.write(GameMessageFormatter.actorMovementMessage(
					movement.actor().id().longValue(),
					movement.path().toString()
			));
			break;
		}
	}

	private void listenChannel(ChannelEvent event) {
		client.write(ChannelGameMessageFormatter.clientMultiMessage(
				event.channel(),
				event.author().id(),
				event.author().getName(),
				event.message()
		));
	}

	private void listenMap(MapEvent event) {
		switch (event.mapEventType()) {
		case ENTER:
			client.write(GameMessageFormatter.showActorMessage(event.actor().toBaseRolePlayActorType()));
			break;
			
		case LEAVE:
			client.write(GameMessageFormatter.removeActorMessage(event.actor().id()));
			break;
			
		case UPDATE:
			client.write(GameMessageFormatter.updateActorMessage(event.actor().toBaseRolePlayActorType()));
			break;
			
		case ACCESSORIES:
			client.write(ItemGameMessageFormatter.accessoriesMessage(event.actor().id(), event.actor().getLook().accessories()));
			break;
		}
	}
	
	private void listenTeleportation(PlayerTeleportationEvent event) {
		// TODO party : refresh mini-map
	}

	private void listenSystemMessage(SystemMessageEvent event) {
		client.write(ChannelGameMessageFormatter.informationMessage(event.message()));
	}

}
