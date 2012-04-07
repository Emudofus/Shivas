package org.shivas.login.database;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.login.database.repositories.AccountRepository;
import org.shivas.login.database.repositories.GameServerRepository;

@Singleton
public class DefaultRepositoryContainer implements RepositoryContainer {
	
	@Inject
	private AccountRepository accounts;
	
	@Inject
	private GameServerRepository servers;

	public AccountRepository getAccounts() {
		return accounts;
	}

	public GameServerRepository getServers() {
		return servers;
	}

}
