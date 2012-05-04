package org.shivas.server.services.game.handlers;

import java.util.Collection;

import org.apache.mina.core.session.IoSession;
import org.shivas.protocol.client.formatters.GameMessageFormatter;
import org.shivas.server.core.GameActor;
import org.shivas.server.core.maps.GMap;
import org.shivas.server.core.maps.MapEvent;
import org.shivas.server.core.maps.MapObserver;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;
import org.shivas.server.utils.Converters;

import com.google.common.collect.Collections2;

public class GameHandler extends AbstractBaseHandler<GameClient> implements MapObserver {

	public GameHandler(GameClient client, IoSession session) {
		super(client, session);
	}

	public void init() throws Exception {
	}

	public void handle(String message) throws Exception {
		switch (message.charAt(1)) {
		case 'C':
			parseGameCreationMessage();
			break;
			
		case 'I':
			parseGameInformationsMessage();
			break;
		}
	}

	public void onClosed() {
	}

	@Override
	public void observe(GMap map, MapEvent event) {
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
		
		Collection<GameActor> actors = map.actors();
		
		session.write(GameMessageFormatter.showActorsMessage(Collections2.transform(actors, Converters.GAMEACTOR_TO_BASEROLEPLAYACTORTYPE)));
		session.write(GameMessageFormatter.mapLoadedMessage());
		session.write(GameMessageFormatter.fightCountMessage(0)); // TODO fights
		
		map.addObserver(this);
	}

}
