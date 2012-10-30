package org.shivas.core.services.game.handlers;

import org.shivas.data.entity.Waypoint;
import org.shivas.protocol.client.formatters.WaypointGameMessageFormatter;
import org.shivas.core.core.interactions.WaypointPanelInteraction;
import org.shivas.core.core.maps.GameMap;
import org.shivas.core.core.waypoints.WaypointList;
import org.shivas.core.services.AbstractBaseHandler;
import org.shivas.core.services.game.GameClient;

public class WaypointHandler extends AbstractBaseHandler<GameClient> {

	public WaypointHandler(GameClient client) {
		super(client);
	}

	@Override
	public void init() throws Exception {
	}

	@Override
	public void onClosed() {
	}

	@Override
	public void handle(String message) throws Exception {
		switch (message.charAt(1)) {
		case 'U':
			parseUseMessage(Integer.parseInt(message.substring(2)));
			break;
		
		case 'V':
			parseClosePanelMessage();
			break;
		}
	}

	private void parseUseMessage(int targetMapId) throws Exception {
		Waypoint waypoint = client.player().getWaypoints().get(targetMapId);
		assertFalse(waypoint == null, "unknown waypoint on map %d", targetMapId);
		
		long cost = WaypointList.getCost(waypoint.getMap(), client.player().getLocation().getMap());
		
		if (cost > client.player().getBag().getKamas()) {
			client.write(WaypointGameMessageFormatter.useErrorMessage());
		} else {
			client.player().getBag().minusKamas(cost);
			
			client.write(client.player().getStats().packet());
			client.interactions().remove(WaypointPanelInteraction.class).end();
			
			client.player().teleport(
					(GameMap) waypoint.getMap(),
					waypoint.getCell()
			);
		}
	}

	private void parseClosePanelMessage() throws Exception {
		client.interactions().remove(WaypointPanelInteraction.class).end();
	}

}
