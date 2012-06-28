package org.shivas.server.core.maps;

import java.util.Collection;
import java.util.Map;

import org.shivas.data.entity.MapTemplate;
import org.shivas.server.core.GameActor;
import org.shivas.server.core.GameActorWithoutId;
import org.shivas.server.core.actions.RolePlayMovement;
import org.shivas.server.core.events.EventDispatcher;
import org.shivas.server.core.events.EventDispatchers;

import com.google.common.collect.Maps;

public class GameMap extends MapTemplate {

	private static final long serialVersionUID = 6687106835430542049L;
	
	private final EventDispatcher event = EventDispatchers.create();
	
	private final Map<Integer, GameActor> actors = Maps.newConcurrentMap();
	private int actorId;
	
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
		actors.remove(actor.id());
		
		event.publish(new BaseMapEvent(actor, MapEventType.LEAVE));
	}
	
	public void movement(RolePlayMovement movement) {
		event.publish(movement);
	}
	
	public void update(GameActor actor) {
		event.publish(new BaseMapEvent(actor, MapEventType.UPDATE));
	}
	
	public void updateAccessories(GameActor actor) {
		event.publish(new BaseMapEvent(actor, MapEventType.ACCESSORIES));
	}

}
