package org.shivas.server.database;

import org.shivas.server.database.repositories.*;

public interface RepositoryContainer {
	void load();
	
	AccountRepository accounts();
	PlayerRepository players();
}
