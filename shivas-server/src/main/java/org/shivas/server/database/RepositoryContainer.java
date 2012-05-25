package org.shivas.server.database;

import org.shivas.server.database.repositories.*;

public interface RepositoryContainer {
	void load();
	void save();
	
	AccountRepository accounts();
	PlayerRepository players();
	GameItemRepository items();
}
