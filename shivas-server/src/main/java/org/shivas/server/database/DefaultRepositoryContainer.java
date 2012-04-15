package org.shivas.server.database;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.server.database.repositories.AccountRepository;
import org.shivas.server.database.repositories.PlayerRepository;

@Singleton
public class DefaultRepositoryContainer implements RepositoryContainer {
	
	@Inject
	private AccountRepository accounts;
	
	@Inject
	private PlayerRepository players;

	public AccountRepository accounts() {
		return accounts;
	}
	
	public PlayerRepository players() {
		return players;
	}

}
