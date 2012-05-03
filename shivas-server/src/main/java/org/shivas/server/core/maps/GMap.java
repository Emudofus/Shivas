package org.shivas.server.core.maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.shivas.common.observable.Observable;
import org.shivas.data.entity.GameMap;
import org.shivas.server.core.GameActor;
import org.shivas.server.core.GameActorWithoutId;

import com.google.common.collect.Maps;

public class GMap extends GameMap implements Observable<MapObserver, MapEvent> {

	private static final long serialVersionUID = 6687106835430542049L;
	
	private List<MapObserver> observers = Collections.synchronizedList(new ArrayList<MapObserver>());
	
	private Map<Integer, GameActor> actors = Maps.newConcurrentMap();
	private int actorId;

	public void addObserver(MapObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(MapObserver observer) {
		observers.remove(observer);
	}

	public void notifyObservers(MapEvent arg) {
		for (MapObserver observer : observers) {
			observer.observe(this, arg);
		}
	}

	public void enter(GameActor actor) {
		if (actor instanceof GameActorWithoutId) {
			((GameActorWithoutId) actor).setId(--actorId);
		}
		actors.put(actor.id(), actor);
	}
	
	public void leave(GameActor actor) {
		actors.remove(actor);
	}
	
	public int count() {
		return actors.size();
	}
	
	public Collection<GameActor> actors() {
		return actors.values();
	}

}
