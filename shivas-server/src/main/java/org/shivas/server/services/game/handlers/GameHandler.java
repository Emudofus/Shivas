package org.shivas.server.services.game.handlers;

import org.apache.mina.core.session.IoSession;
import org.shivas.protocol.client.enums.ActionTypeEnum;
import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.protocol.client.formatters.GameMessageFormatter;
import org.shivas.server.core.Path;
import org.shivas.server.core.actions.Action;
import org.shivas.server.core.actions.ActionException;
import org.shivas.server.core.actions.RolePlayMovement;
import org.shivas.server.core.maps.GMap;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.CriticalException;
import org.shivas.server.services.game.GameClient;
import org.shivas.server.utils.Converters;

import com.google.common.collect.Collections2;

public class GameHandler extends AbstractBaseHandler<GameClient> {

	public GameHandler(GameClient client, IoSession session) {
		super(client, session);
	}

	public void init() throws Exception {
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
		GMap map = client.player().getLocation().getMap();
		map.event().unsubscribe(client.eventListener());
		map.leave(client.player());
	}

	private void parseGameCreationMessage() {
		session.write(GameMessageFormatter.gameCreationSuccessMessage());

		session.write(GameMessageFormatter.statisticsMessage(
				client.player().getExperience().current(),
				client.player().getExperience().min(),
				client.player().getExperience().max(),
				1000L, // TODO bag
				client.player().getStats().statPoints(),
				client.player().getStats().spellPoints(),
				0, (short) 0, (short) 0, 0, 0, false, // TODO pvp
				client.player().getStats().life(),
				client.player().getStats().energy(),
				client.player().getStats()
		));
		
		session.write(GameMessageFormatter.mapDataMessage(
				client.player().getLocation().getMap().getId(),
				client.player().getLocation().getMap().getDate(),
				client.player().getLocation().getMap().getKey()
		));
	}

	private void parseGameInformationsMessage() {
		GMap map = client.player().getLocation().getMap();
		
		map.enter(client.player());
		
		session.write(GameMessageFormatter.showActorsMessage(Collections2.transform(map.actors(), Converters.GAMEACTOR_TO_BASEROLEPLAYACTORTYPE)));
		session.write(GameMessageFormatter.mapLoadedMessage());
		session.write(GameMessageFormatter.fightCountMessage(0)); // TODO fights
		
		map.event().subscribe(client.eventListener());
	}

	private void parseGameActionMessage(ActionTypeEnum action, String args) throws Exception {
		switch (action) {
		case MOVEMENT:
			parseMovementMessage(Path.parsePath(args));
			break;
		}
	}

	private void parseMovementMessage(Path path) throws ActionException {
		client.actions().push(new RolePlayMovement(client, session, path)).begin();
	}

	private void parseGameActionEndMessage(boolean success, String args) throws Exception {
		Action action = client.actions().remove();
		if (action == null) {
			throw new CriticalException("you can't do this");
		}

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
