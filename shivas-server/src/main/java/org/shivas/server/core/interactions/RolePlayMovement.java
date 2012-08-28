package org.shivas.server.core.interactions;

import org.shivas.data.entity.MapTrigger;
import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;
import org.shivas.server.core.GameActor;
import org.shivas.server.core.Location;
import org.shivas.server.core.Path;
import org.shivas.server.core.maps.GameMap;
import org.shivas.server.services.game.GameClient;

public class RolePlayMovement extends AbstractAction {
	
	private GameClient client;
	private Path path;

	public RolePlayMovement(GameClient client, Path path) {
		this.client = client;
		this.path = path;
		
		// add current location to the path
		Location location = this.client.player().getLocation();
		this.path.add(0, new Path.Node(location.getOrientation(), location.getCell()));
	}

	public ActionType actionType() {
		return ActionType.MOVEMENT;
	}

	public GameActor actor() {
		return client.player();
	}

	public Path path() {
		return path;
	}

	public void begin() throws ActionException {
		client.player().getLocation().getMap().movement(this);
	}

	protected void internalEnd() throws ActionException {
        Location location = client.player().getLocation();
        
        // set the new location
        Path.Node target = path.last();
        
        location.setCell(target.cell());
        location.setOrientation(target.orientation());
        
        // teleport if there's a trigger
        MapTrigger trigger = location.getMap().getTrigger().get(target.cell());
        if (trigger != null) {
        	client.player().teleport((GameMap) trigger.getNextMap(), trigger.getNextCell());
        } else {
        	client.write(BasicGameMessageFormatter.noOperationMessage());
        }
	}

	public void cancel() throws ActionException {
	}
	
	public void cancel(OrientationEnum orientation, short cell) {
		Location location = client.player().getLocation();
		
		location.setOrientation(orientation);
		location.setCell(cell);
	}

}
