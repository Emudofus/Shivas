package org.shivas.login.database;

import org.shivas.login.database.repositories.AccountRepository;
import org.shivas.login.database.repositories.GameServerRepository;

public interface RepositoryContainer {
	AccountRepository getAccounts();
	GameServerRepository getServers();
}
