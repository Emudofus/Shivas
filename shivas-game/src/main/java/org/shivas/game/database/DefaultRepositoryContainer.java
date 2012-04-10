package org.shivas.game.database;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.game.database.repositories.BreedRepository;
import org.shivas.game.database.repositories.PlayerRepository;

@Singleton
public class DefaultRepositoryContainer implements RepositoryContainer {
	
	@Inject
	private PlayerRepository players;
	
	@Inject
	private BreedRepository breeds;

	public PlayerRepository getPlayers() {
		return players;
	}

	public BreedRepository getBreeds() {
		return breeds;
	}
	
}
