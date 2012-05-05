package org.shivas.data.entity.factory;

import org.shivas.data.EntityFactory;
import org.shivas.data.entity.Breed;
import org.shivas.data.entity.Experience;
import org.shivas.data.entity.GameCell;
import org.shivas.data.entity.GameMap;
import org.shivas.data.entity.MapTrigger;

public class BaseEntityFactory implements EntityFactory {

	@Override
	public Breed newBreed() {
		return new Breed();
	}

	@Override
	public Experience newExperience() {
		return new Experience();
	}

	@Override
	public GameMap newGameMap() {
		return new GameMap();
	}

	@Override
	public MapTrigger newMapTrigger() {
		return new MapTrigger();
	}

	@Override
	public GameCell newGameCell() {
		return new GameCell();
	}

}
