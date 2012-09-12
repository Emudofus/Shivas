package org.shivas.server.services.game.handlers;

import org.shivas.server.core.interactions.WaypointPanelInteraction;
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
		case 'V':
			parseClosePanelMessage();
			break;
		}
	}

	private void parseClosePanelMessage() throws Exception {
		WaypointPanelInteraction panel = client.actions().remove();
		assertFalse(panel == null, "you have not opened waypoints panel");
		
		panel.cancel();
	}

}
