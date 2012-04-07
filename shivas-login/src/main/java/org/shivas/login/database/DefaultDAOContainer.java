package org.shivas.login.database;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.shivas.login.database.dao.AccountDAO;

@Singleton
public class DefaultDAOContainer implements DAOContainer {
	
	@Inject
	private AccountDAO accounts;

	public AccountDAO getAccounts() {
		return accounts;
	}

}
