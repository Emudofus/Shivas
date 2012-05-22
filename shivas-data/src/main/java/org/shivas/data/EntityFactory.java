package org.shivas.data;

import org.shivas.data.entity.*;

public interface EntityFactory {
	Breed newBreed();
	Experience newExperience();
	MapTemplate newMapTemplate();
	MapTrigger newMapTrigger();
	GameCell newGameCell();
}
