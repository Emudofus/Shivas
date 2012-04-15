package org.shivas.server.database;

import org.shivas.server.database.repositories.AccountRepository;

public interface RepositoryContainer {
	AccountRepository accounts();
}
