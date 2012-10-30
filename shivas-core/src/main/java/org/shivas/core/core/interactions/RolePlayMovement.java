package org.shivas.core.core.interactions;

import org.shivas.data.entity.MapTrigger;
import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;
import org.shivas.core.core.GameActor;
import org.shivas.core.core.Location;
import org.shivas.core.core.maps.GameMap;
import org.shivas.core.core.paths.Node;
import org.shivas.core.core.paths.Path;
import org.shivas.core.services.game.GameClient;

public class RolePlayMovement extends AbstractInteraction {
	
	private GameClient client;
	private Path path;

	public RolePlayMovement(GameClient client, Path path) {
		this.client = client;
		this.path = path;
		
		// add current location to the path
		Location location = this.client.player().getLocation();
		this.path.add(0, new Node(location.getOrientation(), location.getCell()));
	}

	public InteractionType getInteractionType() {
		return InteractionType.MOVEMENT;
	}

	public GameActor actor() {
		return client.player();
	}

	public Path path() {
		return path;
	}

	public void begin() throws InteractionException {
		client.player().getLocation().getMap().movement(this);
	}

	protected void internalEnd() throws InteractionException {
        Location location = client.player().getLocation();
        
        // set the new location
        Node target = path.last();
        
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

	public void cancel() throws InteractionException {
	}
	
	public void cancel(OrientationEnum orientation, short cell) {
		Location location = client.player().getLocation();
		
		location.setOrientation(orientation);
		location.setCell(cell);
	}

}
