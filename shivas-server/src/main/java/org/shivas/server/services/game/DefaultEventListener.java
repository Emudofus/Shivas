package org.shivas.server.services.game;

import org.shivas.protocol.client.formatters.ChannelGameMessageFormatter;
import org.shivas.protocol.client.formatters.GameMessageFormatter;
import org.shivas.protocol.client.formatters.InfoGameMessageFormatter;
import org.shivas.protocol.client.formatters.ItemGameMessageFormatter;
import org.shivas.protocol.client.formatters.PartyGameMessageFormatter;
import org.shivas.server.core.channels.ChannelEvent;
import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventListener;
import org.shivas.server.core.events.events.FriendConnectionEvent;
import org.shivas.server.core.events.events.PlayerTeleportationEvent;
import org.shivas.server.core.events.events.SystemMessageEvent;
import org.shivas.server.core.interactions.Action;
import org.shivas.server.core.interactions.RolePlayMovement;
import org.shivas.server.core.maps.GameMap;
import org.shivas.server.core.maps.MapEvent;
import org.shivas.server.core.parties.PartyEvent;
import org.shivas.server.database.models.Player;

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
			
		case NEW_INTERACTION: // not implemented
			break;
			
		case FRIEND_CONNECTION:
			listenFriendConnection((FriendConnectionEvent) event);
			break;
			
		case PARTY:
			listenParty((PartyEvent) event);
			break;
		}
	}

	private void listenAction(Action action) {
		switch (action.actionType()) {
		case MOVEMENT:
			RolePlayMovement movement = (RolePlayMovement) action;
			client.write(GameMessageFormatter.actorMovementMessage(
					movement.actor().getPublicId(),
					movement.path().toString()
			));
			break;
			
		case PARTY_INVITATION: // nothing to do, PartyHandler handles it
			break;
			
		case WAYPOINT_PANEL: // nothing to do
			break;
		}
	}

	private void listenChannel(ChannelEvent event) {
		if (event.author() instanceof Player && !client.player().canReceiveMessageFrom((Player) event.author())) return;
		
		client.write(ChannelGameMessageFormatter.clientMultiMessage(
				event.channel(),
				event.author().getPublicId(),
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
			client.write(GameMessageFormatter.removeActorMessage(event.actor().getPublicId()));
			break;
			
		case UPDATE:
			client.write(GameMessageFormatter.updateActorMessage(event.actor().toBaseRolePlayActorType()));
			break;
			
		case ACCESSORIES:
			client.write(ItemGameMessageFormatter.accessoriesMessage(event.actor().getPublicId(), event.actor().getLook().accessories()));
			break;
		}
	}
	
	private void listenTeleportation(PlayerTeleportationEvent event) {
		if (event.getPlayer() == client.player()) {			
			GameMap map = client.player().getLocation().getMap();
			map.event().unsubscribe(this);
			map.leave(client.player());

			client.player().setLocation(event.getTarget());
			
			client.write(GameMessageFormatter.changeMapMessage(client.player().getPublicId()));
			client.write(GameMessageFormatter.mapDataMessage(
					client.player().getLocation().getMap().getId(),
					client.player().getLocation().getMap().getDate(),
					client.player().getLocation().getMap().getKey()
			));
		}
		
		// TODO party : refresh mini-map
	}

	private void listenSystemMessage(SystemMessageEvent event) {
		client.write(ChannelGameMessageFormatter.informationMessage(event.message()));
	}

	private void listenFriendConnection(FriendConnectionEvent event) {
		client.write(InfoGameMessageFormatter.friendConnectedMessage(
				event.getFriend().getNickname(),
				event.getFriend().getCurrentPlayer().getName()
		));
	}

	private void listenParty(PartyEvent event) {
		switch (event.getPartyEventType()) {
		case ADD_MEMBER:
			client.write(PartyGameMessageFormatter.addMemberMessage(event.getPlayer().toBasePartyMemberType()));
			break;
			
		case REMOVE_MEMBER:
			if (event.getPlayer() == client.player()) {
				client.write(PartyGameMessageFormatter.quitMessage());
				client.party().unsubscribe(this);
				client.setParty(null);
			} else {
				client.write(PartyGameMessageFormatter.removeMemberMessage(event.getPlayer().getId()));
			}
			break;
			
		case REFRESH_MEMBER:
			client.write(PartyGameMessageFormatter.refreshMemberMessage(event.getPlayer().toBasePartyMemberType()));
			break;
			
		case NEW_OWNER:
			client.write(PartyGameMessageFormatter.leaderInformationMessage(event.getPlayer().getId()));
			break;
			
		case CLOSE:
			client.write(PartyGameMessageFormatter.quitMessage());
			client.setParty(null);
			break;
		}
	}

}
