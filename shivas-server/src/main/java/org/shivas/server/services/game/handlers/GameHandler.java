package org.shivas.server.services.game.handlers;

import org.shivas.data.entity.Waypoint;
import org.shivas.protocol.client.enums.ActionTypeEnum;
import org.shivas.protocol.client.enums.InteractiveObjectTypeEnum;
import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.protocol.client.formatters.GameMessageFormatter;
import org.shivas.protocol.client.formatters.InfoGameMessageFormatter;
import org.shivas.server.core.Location;
import org.shivas.server.core.Path;
import org.shivas.server.core.interactions.Action;
import org.shivas.server.core.interactions.ActionException;
import org.shivas.server.core.interactions.RolePlayMovement;
import org.shivas.server.core.interactions.WaypointPanelInteraction;
import org.shivas.server.core.maps.GameMap;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;
import org.shivas.server.utils.Converters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Collections2;

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
		client.write(GameMessageFormatter.fightCountMessage(0)); // TODO fights
		
		map.event().subscribe(client.eventListener());
	}

	private void parseGameActionMessage(ActionTypeEnum action, String args) throws Exception {
		String[] splitted = null;
		switch (action) {
		case MOVEMENT:
			parseMovementMessage(Path.parsePath(args));
			break;
			
		case INTERACTIVE_OBJECT:
			splitted = args.split(";");
			parseUseObject(
					Short.parseShort(splitted[0]),
					InteractiveObjectTypeEnum.valueOf(Integer.parseInt(splitted[1]))
			);
			break;
			
		default:
			log.trace("action {} is not implemented", action);
			break;
		}
	}

	private void parseMovementMessage(Path path) throws ActionException {
		client.actions().push(new RolePlayMovement(client, path)).begin();
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
	
	private void parseOpenWaypointPanel() throws ActionException {
		client.actions().push(new WaypointPanelInteraction(client)).begin();
	}

	private void parseGameActionEndMessage(boolean success, String args) throws Exception {
		Action action = client.actions().remove();
		assertFalse(action == null, "you can't do this");

		switch (action.actionType()) {
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
