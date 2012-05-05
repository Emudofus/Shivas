package org.shivas.data;

import org.shivas.data.entity.*;

public interface EntityFactory {
	Breed newBreed();
	Experience newExperience();
	GameMap newGameMap();
	MapTrigger newMapTrigger();
	GameCell newGameCell();
}
