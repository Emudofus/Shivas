package org.shivas.core.core.maps;

import com.google.common.collect.Maps;
import org.shivas.common.collections.CollectionQuery;
import org.shivas.core.core.GameActor;
import org.shivas.core.core.GameActorWithoutId;
import org.shivas.core.core.events.EventDispatcher;
import org.shivas.core.core.events.EventDispatchers;
import org.shivas.core.core.fights.Fight;
import org.shivas.core.core.interactions.RolePlayMovement;
import org.shivas.core.utils.Filters;
import org.shivas.data.entity.MapTemplate;

import java.util.Collection;
import java.util.Map;

public class GameMap extends MapTemplate {

    public static final byte MAX_STORE_PER_MAP = 5;

	private static final long serialVersionUID = 6687106835430542049L;
	
	private final EventDispatcher event = EventDispatchers.create();
	
	private final Map<Integer, GameActor> actors = Maps.newConcurrentMap();
    private final Map<Integer, Fight> fights = Maps.newConcurrentMap();
	private int actorId;

    public GameActor get(int actorId) {
        return actors.get(actorId);
    }

    public Fight getFight(int fightId) {
        return fights.get(fightId);
    }
	
	public int count() {
		return actors.size();
	}

    public int countFights() {
        return fights.size();
    }

    public boolean hasAvailableStorePlaces() {
        return CollectionQuery.from(actors.values()).count(Filters.STOREACTOR_FILTER) < MAX_STORE_PER_MAP;
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
		actors.put(actor.getPublicId(), actor);
		
		event.publish(new BaseMapEvent(actor, MapEventType.ENTER));
	}
	
	public void leave(GameActor actor) {
		actors.remove(actor.getPublicId());
		
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

    public void add(Fight fight) {
        fights.put(fight.getId(), fight);
    }

    public void remove(Fight fight) {
        fights.remove(fight.getId());
    }
}
