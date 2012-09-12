package org.shivas.server.core.interactions;

import java.util.concurrent.Executors;

import org.shivas.protocol.client.formatters.WaypointGameMessageFormatter;
import org.shivas.server.services.game.GameClient;

public class WaypointPanelInteraction extends AbstractAction {
	
	private final GameClient client;

	public WaypointPanelInteraction(GameClient client) {
		this.client = client;
	}

	@Override
	public ActionType actionType() {
		return ActionType.WAYPOINT_PANEL;
	}
	
	private void doBegin() throws ActionException {
		client.write(WaypointGameMessageFormatter.listMessage(
				client.player().getLocation().getMap().getId(),
				client.player().getWaypoints().toBaseWaypointType()
		));
	}

	@Override
	public void begin() throws ActionException {
		if (client.actions().current().actionType() == ActionType.MOVEMENT) {
			client.actions().current().endFuture().addListener(new Runnable() {
				public void run() {
					try {
						doBegin();
					} catch (ActionException e) {
						e.printStackTrace();
					}
				}
			}, Executors.newSingleThreadExecutor());
		} else {
			doBegin();
		}
	}

	@Override
	public void cancel() throws ActionException {
		client.write(WaypointGameMessageFormatter.closePanelMessage());
	}

	@Override
	protected void internalEnd() throws ActionException {
	}

}
