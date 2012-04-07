package org.shivas.login.database;

import org.shivas.login.database.repositories.AccountRepository;

public interface RepositoryContainer {
	AccountRepository getAccounts();
}
