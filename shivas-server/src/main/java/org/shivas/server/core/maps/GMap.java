package org.shivas.server.core.maps;

import java.util.Collection;
import java.util.Map;

import org.shivas.data.entity.GameMap;
import org.shivas.server.core.GameActor;
import org.shivas.server.core.GameActorWithoutId;
import org.shivas.server.core.actions.RolePlayMovement;
import org.shivas.server.core.events.EventDispatcher;

import com.google.common.collect.Maps;

public class GMap extends GameMap {

	private static final long serialVersionUID = 6687106835430542049L;
	
	private final EventDispatcher event;
	
	private final Map<Integer, GameActor> actors = Maps.newConcurrentMap();
	private int actorId;

	public GMap(EventDispatcher event) {
		this.event = event;
	}
	
	public int count() {
		return actors.size();
	}
	
	public Collection<GameActor> actors() {
		return actors.values();
	}
	
	public EventDispatcher event() {
		return event;
	}

	public void enter(GameActor actor) {
		if (actor instanceof GameActorWithoutId) {
			((GameActorWithoutId) actor).setId(--actorId);
		}
		actors.put(actor.id(), actor);
		
		event.publish(new BaseMapEvent(actor, MapEventType.ENTER));
	}
	
	public void leave(GameActor actor) {
		actors.remove(actor);
		
		event.publish(new BaseMapEvent(actor, MapEventType.LEAVE));
	}
	
	public void movement(RolePlayMovement movement) {
		event.publish(movement);
	}

}
