package org.shivas.login.database;

import org.shivas.login.database.dao.AccountRepository;

public interface RepositoryContainer {
	AccountRepository getAccounts();
}
