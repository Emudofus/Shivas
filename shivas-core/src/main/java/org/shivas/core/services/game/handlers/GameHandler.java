package org.shivas.core.services.game.handlers;

import com.google.common.collect.Collections2;
import org.shivas.core.core.GameActor;
import org.shivas.core.core.Location;
import org.shivas.core.core.events.events.ChangeMapEvent;
import org.shivas.core.core.fights.FightInvitation;
import org.shivas.core.core.interactions.Interaction;
import org.shivas.core.core.interactions.InteractionException;
import org.shivas.core.core.interactions.RolePlayMovement;
import org.shivas.core.core.interactions.WaypointPanelInteraction;
import org.shivas.core.core.maps.GameMap;
import org.shivas.core.core.paths.Path;
import org.shivas.core.database.models.Player;
import org.shivas.core.services.AbstractBaseHandler;
import org.shivas.core.services.CriticalException;
import org.shivas.core.services.game.GameClient;
import org.shivas.core.utils.Converters;
import org.shivas.data.entity.Waypoint;
import org.shivas.protocol.client.enums.ActionTypeEnum;
import org.shivas.protocol.client.enums.InteractiveObjectTypeEnum;
import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.protocol.client.formatters.GameMessageFormatter;
import org.shivas.protocol.client.formatters.InfoGameMessageFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameHandler extends AbstractBaseHandler<GameClient> {
	
	private static final Logger log = LoggerFactory.getLogger(GameHandler.class);

	public GameHandler(GameClient client) {
		super(client);
	}

	public void init() throws Exception {
		client.player().getStats().refresh();
		
		client.player().getEvent().subscribe(client.eventListener());
	}

	public void handle(String message) throws Exception {
		switch (message.charAt(1)) {
		case 'A':
			parseGameActionMessage(ActionTypeEnum.valueOf(Integer.parseInt(message.substring(2, 5))), message.substring(5));
			break;
		
		case 'C':
			parseGameCreationMessage();
			break;
			
		case 'I':
			parseGameInformationsMessage();
			break;
			
		case 'K':
			parseGameActionEndMessage(message.charAt(2) == 'K', message.substring(3));
			break;
		}
	}

	public void onClosed() {
		GameMap map = client.player().getLocation().getMap();
		map.event().unsubscribe(client.eventListener());
		map.leave(client.player());
	}

	private void parseGameCreationMessage() {
		client.write(GameMessageFormatter.gameCreationSuccessMessage());

		client.write(client.player().getStats().packet());
		
		client.write(GameMessageFormatter.mapDataMessage(
				client.player().getLocation().getMap().getId(),
				client.player().getLocation().getMap().getDate(),
				client.player().getLocation().getMap().getKey()
		));
	}

	private void parseGameInformationsMessage() {
		GameMap map = client.player().getLocation().getMap();
		
		map.enter(client.player());
		
		client.write(GameMessageFormatter.showActorsMessage(Collections2.transform(map.actors(), Converters.GAMEACTOR_TO_BASEROLEPLAYACTORTYPE)));
		client.write(GameMessageFormatter.mapLoadedMessage());
		client.write(GameMessageFormatter.fightCountMessage(map.countFights()));
		
		map.event().subscribe(client.eventListener());
		
		client.player().getEvent().publish(new ChangeMapEvent(client.player()));
	}

	private void parseGameActionMessage(ActionTypeEnum action, String data) throws Exception {
		String[] args;
		switch (action) {
		case MOVEMENT:
			parseMovementMessage(Path.parsePath(data));
			break;
			
		case INTERACTIVE_OBJECT:
			args = data.split(";");
			parseUseObject(
					Short.parseShort(args[0]),
					InteractiveObjectTypeEnum.valueOf(Integer.parseInt(args[1]))
			);
			break;

        case ASK_FIGHT:
            parseFightInvitationMessage(Integer.parseInt(data));
            break;

        case DECLINE_FIGHT:
            parseDeclineFightInvitationMessage();
            break;

        case ACCEPT_FIGHT:
            parseAcceptFightInvitationMessage();
            break;
			
		default:
			log.trace("action {} is not implemented", action);
			break;
		}
	}

    private void parseMovementMessage(Path path) throws InteractionException {
		client.interactions().push(new RolePlayMovement(client, path)).begin();
	}

    private void parseFightInvitationMessage(int playerId) throws CriticalException, InteractionException {
        GameActor actor = client.player().getLocation().getMap().get(playerId);
        assertTrue(actor != null, "unknown player %d on current map", playerId);
        assertTrue(actor instanceof Player, "target must be a Player");

        Player player = (Player) actor;

        // target != null because player is on map and therefore connected
        client.interactions().push(new FightInvitation(client, player.getClient(), client.service().fightFactory())).begin();
    }

    private void parseDeclineFightInvitationMessage() throws InteractionException {
        client.interactions().remove(FightInvitation.class).decline();
    }

    private void parseAcceptFightInvitationMessage() throws CriticalException, InteractionException {
        assertFalse(client.interactions().current(FightInvitation.class).getSource() == client, "only target can accept invitation");

        client.interactions().remove(FightInvitation.class).accept();
    }

	private void parseUseObject(short cellId, InteractiveObjectTypeEnum objectType) throws Exception {
		switch (objectType) {
		case SAVE_WAYPOINT:
			parseSaveWaypoint();
			break;
			
		case WAYPOINT:
			parseOpenWaypointPanel();
			break;
			
		default:
			log.warn("InteractiveObject {} not implemented", objectType);
			break;
		}
	}

	private void parseSaveWaypoint() throws Exception {
		Waypoint waypoint = client.player().getLocation().getMap().getWaypoint();
		assertTrue(waypoint != null, "there are not waypoint here");
		
		client.player().setSavedLocation(new Location(
				(GameMap) waypoint.getMap(),
				waypoint.getCell(),
				OrientationEnum.SOUTH_WEST
		));
		
		client.write(InfoGameMessageFormatter.waypointSavedMessage());
	}
	
	private void parseOpenWaypointPanel() throws InteractionException {
		client.interactions().front(new WaypointPanelInteraction(client)).begin();
	}

	private void parseGameActionEndMessage(boolean success, String args) throws Exception {
		Interaction action = client.interactions().remove();
        if (action == null) return;

		switch (action.getInteractionType()) {
		case MOVEMENT:
			if (success) {
				action.end();
			} else {
				String[] args_splitted = args.split("\\|");
				
				OrientationEnum orientation = OrientationEnum.valueOf(Integer.parseInt(args_splitted[0]));
				short cell = Short.parseShort(args_splitted[1]);
				
				((RolePlayMovement) action).cancel(orientation, cell);
			}
			break;
			
		default:
			if (success) action.end();
			else 		 action.cancel();
			break;
		}
	}

}
