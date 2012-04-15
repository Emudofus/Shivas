package org.shivas.server.database;

import org.shivas.server.database.repositories.AccountRepository;
import org.shivas.server.database.repositories.PlayerRepository;

public interface RepositoryContainer {
	AccountRepository accounts();
	PlayerRepository players();
}
