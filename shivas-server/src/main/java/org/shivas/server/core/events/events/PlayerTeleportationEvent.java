package org.shivas.server.core.events.events;

import org.shivas.server.core.Location;
import org.shivas.server.core.events.Event;
import org.shivas.server.core.events.EventType;
import org.shivas.server.database.models.Player;

public class PlayerTeleportationEvent implements Event {
	
	private Player player;
	private Location target;

	public PlayerTeleportationEvent(Player player, Location target) {
		this.player = player;
		this.target = target;
	}

	public EventType type() {
		return EventType.TELEPORTATION;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Location getTarget() {
		return target;
	}

	public void setTarget(Location target) {
		this.target = target;
	}

}
