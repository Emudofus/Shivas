package org.shivas.server.core.actions;

import org.apache.mina.core.session.IoSession;
import org.shivas.data.entity.MapTrigger;
import org.shivas.protocol.client.formatters.BasicGameMessageFormatter;
import org.shivas.server.core.GameActor;
import org.shivas.server.core.Location;
import org.shivas.server.core.Path;
import org.shivas.server.core.maps.GMap;
import org.shivas.server.core.maps.MapEvent;
import org.shivas.server.core.maps.MapEventType;
import org.shivas.server.services.game.GameClient;

public class RolePlayMovement extends AbstractAction implements MapEvent {
	
	private GameClient client;
	private IoSession session;
	private Path path;

	public RolePlayMovement(GameClient client, IoSession session, Path path) {
		this.client = client;
		this.session = session;
		this.path = path;
		
		// add current location to the path
		Location location = this.client.player().getLocation();
		this.path.add(0, new Path.Node(location.getOrientation(), location.getCell()));
	}

	@Override
	public GameActor actor() {
		return client.player();
	}

	@Override
	public ActionType actionType() {
		return ActionType.MOVEMENT;
	}

	@Override
	public MapEventType mapEventType() {
		return MapEventType.MOVE;
	}

	@Override
	public void begin() throws ActionException {
		client.player().getLocation().getMap().movement(this);
	}

	@Override
	protected void internalEnd() throws ActionException {
        Location location = client.player().getLocation();
        
        // set the new location
        Path.Node target = path.last();
        
        location.setCell(target.cell());
        location.setOrientation(target.orientation());
        
        // teleport if there's a trigger
        MapTrigger trigger = location.getMap().getTrigger().get(target.cell());
        if (trigger != null) {
        	client.player().teleport((GMap) trigger.getNextMap(), trigger.getNextCell());
        } else {
        	session.write(BasicGameMessageFormatter.noOperationMessage());
        }
	}

	@Override
	public void cancel() throws ActionException {
	}
	
	public void cancel(short cell) {
		
	}

	public Path path() {
		return path;
	}

}
