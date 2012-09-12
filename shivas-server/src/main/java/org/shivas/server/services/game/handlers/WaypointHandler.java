package org.shivas.server.services.game.handlers;

import org.shivas.data.entity.Waypoint;
import org.shivas.protocol.client.formatters.WaypointGameMessageFormatter;
import org.shivas.server.core.interactions.WaypointPanelInteraction;
import org.shivas.server.core.maps.GameMap;
import org.shivas.server.core.waypoints.WaypointList;
import org.shivas.server.services.AbstractBaseHandler;
import org.shivas.server.services.game.GameClient;

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
		WaypointPanelInteraction panel = client.interactions().remove();
		assertFalse(panel == null, "you have not opened the waypoint panel");
		Waypoint waypoint = client.player().getWaypoints().get(targetMapId);
		assertFalse(waypoint == null, "unknown waypoint on map %d", targetMapId);
		
		long cost = WaypointList.getCost(waypoint.getMap(), client.player().getLocation().getMap());
		
		if (cost > client.player().getBag().getKamas()) {
			client.write(WaypointGameMessageFormatter.useErrorMessage());
		} else {
			client.player().getBag().minusKamas(cost);
			
			client.write(client.player().getStats().packet());
			panel.end();
			
			client.player().teleport(
					(GameMap) waypoint.getMap(),
					waypoint.getCell()
			);
		}
	}

	private void parseClosePanelMessage() throws Exception {
		WaypointPanelInteraction panel = client.interactions().remove();
		assertFalse(panel == null, "you have not opened waypoints panel");
		
		panel.end();
	}

}
