package org.shivas.server.services.game;

import org.shivas.data.entity.Waypoint;
import org.shivas.protocol.client.formatters.*;
import org.shivas.server.core.channels.ChannelEvent;
import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventListener;
import org.shivas.server.core.events.events.ChangeMapEvent;
import org.shivas.server.core.events.events.FriendConnectionEvent;
import org.shivas.server.core.events.events.PlayerTeleportationEvent;
import org.shivas.server.core.events.events.SystemMessageEvent;
import org.shivas.server.core.exchanges.ExchangeEvent;
import org.shivas.server.core.exchanges.ItemExchangeEvent;
import org.shivas.server.core.exchanges.KamasExchangeEvent;
import org.shivas.server.core.exchanges.ReadyExchangeEvent;
import org.shivas.server.core.interactions.Interaction;
import org.shivas.server.core.interactions.InteractionException;
import org.shivas.server.core.interactions.RolePlayMovement;
import org.shivas.server.core.maps.GameMap;
import org.shivas.server.core.maps.MapEvent;
import org.shivas.server.core.parties.PartyEvent;
import org.shivas.server.core.stores.StoreEvent;
import org.shivas.server.core.stores.StoreInteraction;
import org.shivas.server.database.models.Player;

public class DefaultEventListener implements EventListener {
	
	private final GameClient client;

	public DefaultEventListener(GameClient client) {
		this.client = client;
	}

	public void listen(Event event) {
		switch (event.type()) {
		case INTERACTION:
			listenAction((Interaction) event);
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
		
		case CHANGE_MAP:
			listenChangeMap((ChangeMapEvent) event);
			break;

        case EXCHANGE:
            listenExchange((ExchangeEvent) event);
            break;

        case STORE:
            listenStore((StoreEvent) event);
            break;
		}
	}

    private void listenAction(Interaction action) {
		switch (action.getInteractionType()) {
		case MOVEMENT:
			RolePlayMovement movement = (RolePlayMovement) action;
			client.write(GameMessageFormatter.actorMovementMessage(
					movement.actor().getPublicId(),
					movement.path().toString()
			));
			break;
			
		default:
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
			map.event().unsubscribe(client.eventListener());
			map.leave(client.player());

			client.player().setLocation(event.getTarget());
			
			client.write(GameMessageFormatter.changeMapMessage(client.player().getPublicId()));
			client.write(GameMessageFormatter.mapDataMessage(
					client.player().getLocation().getMap().getId(),
					client.player().getLocation().getMap().getDate(),
					client.player().getLocation().getMap().getKey()
			));
		}
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
				client.party().unsubscribe(client.eventListener());
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

	private void listenChangeMap(ChangeMapEvent event) {
		if (event.getPlayer() == client.player()) {
			/// waypoints ///
			Waypoint waypoint = client.player().getLocation().getMap().getWaypoint();
			if (waypoint != null && !client.player().getWaypoints().contains(waypoint)) {
				client.player().getWaypoints().add(waypoint);
				
				client.write(InfoGameMessageFormatter.newZaapMessage());
			}
		}
		
		// TODO party : refresh mini-map
	}

    private void listenExchange(ExchangeEvent event) {
        boolean local = event.getSource() == client;

        switch (event.getExchangeEventType()) {
        case READY:
            ReadyExchangeEvent readyExchangeEvent = (ReadyExchangeEvent) event;
            client.write(TradeGameMessageFormatter.tradeReadyMessage(
                    readyExchangeEvent.isReady(),
                    readyExchangeEvent.getSource().player().getId()
            ));
            break;

        case KAMAS:
            client.write(TradeGameMessageFormatter.tradeSetKamasMessage(((KamasExchangeEvent) event).getKamas(), local));
            break;

        case UPDATE_ITEM:
        case ADD_ITEM:
            client.write(TradeGameMessageFormatter.tradePutItemMessage(((ItemExchangeEvent) event).getItem().toBaseItemType(), local));
            break;

        case REMOVE_ITEM:
            client.write(TradeGameMessageFormatter.tradeRemoveItemMessage(((ItemExchangeEvent) event).getItem().getId(), local));
            break;
        }
    }

    private void listenStore(StoreEvent event) {
        StoreInteraction interaction = client.interactions().current(StoreInteraction.class);

        switch (event.getStoreEventType()) {
        case CLOSE:
            try {
                client.interactions().remove(StoreInteraction.class).end();
            } catch (InteractionException e) {
                e.printStackTrace();
            }
            break;

        case REFRESH:
            client.write(TradeGameMessageFormatter.storedItemsListMessage(interaction.getStore().toStoreItemType()));
            break;
        }
    }

}
