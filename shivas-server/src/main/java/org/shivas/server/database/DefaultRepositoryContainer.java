package org.shivas.server.database;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.server.database.repositories.AccountRepository;

@Singleton
public class DefaultRepositoryContainer implements RepositoryContainer {
	
	@Inject
	private AccountRepository accounts;

	public AccountRepository accounts() {
		return accounts;
	}

}
