package org.shivas.server.database;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.atomium.exception.LoadingException;
import org.shivas.server.database.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DefaultRepositoryContainer implements RepositoryContainer {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultRepositoryContainer.class);
	
	@Inject
	private AccountRepository accounts;
	
	@Inject
	private PlayerRepository players;
	
	public void load() {
		try {
			log.debug("{} accounts loaded", accounts.load());
			log.debug("{} players loaded", players.load());
		} catch (LoadingException e) {
			log.error("can't load because of {}", e.getMessage());
		}
	}

	public AccountRepository accounts() {
		return accounts;
	}
	
	public PlayerRepository players() {
		return players;
	}

}
