package org.shivas.login.database;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.login.database.dao.AccountRepository;

@Singleton
public class DefaultRepositoryContainer implements RepositoryContainer {
	
	@Inject
	private AccountRepository accounts;

	public AccountRepository getAccounts() {
		return accounts;
	}

}
