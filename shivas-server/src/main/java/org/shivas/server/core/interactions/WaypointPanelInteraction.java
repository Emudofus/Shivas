package org.shivas.server.core.interactions;

import java.util.concurrent.Executors;

import org.shivas.protocol.client.formatters.WaypointGameMessageFormatter;
import org.shivas.server.services.game.GameClient;

public class WaypointPanelInteraction extends AbstractInteraction {
	
	private final GameClient client;

	public WaypointPanelInteraction(GameClient client) {
		this.client = client;
	}

	@Override
	public InteractionType getInteractionType() {
		return InteractionType.WAYPOINT_PANEL;
	}
	
	private void doBegin() throws InteractionException {
		client.write(WaypointGameMessageFormatter.listMessage(
				client.player().getLocation().getMap().getId(),
				client.player().getWaypoints().toBaseWaypointType()
		));
	}

	@Override
	public void begin() throws InteractionException {
		if (client.interactions().current().getInteractionType() == InteractionType.MOVEMENT) {
			client.interactions().current().endFuture().addListener(new Runnable() {
				public void run() {
					try {
						doBegin();
					} catch (InteractionException e) {
						e.printStackTrace();
					}
				}
			}, Executors.newSingleThreadExecutor());
		} else {
			doBegin();
		}
	}

	@Override
	public void cancel() throws InteractionException {
		client.write(WaypointGameMessageFormatter.closePanelMessage());
	}

	@Override
	protected void internalEnd() throws InteractionException {
		cancel();
	}

}
