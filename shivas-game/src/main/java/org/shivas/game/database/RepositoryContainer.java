package org.shivas.game.database;

import org.shivas.game.database.repositories.BreedRepository;
import org.shivas.game.database.repositories.PlayerRepository;

public interface RepositoryContainer {
	PlayerRepository getPlayers();
	
	BreedRepository getBreeds();
}
