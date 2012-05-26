package org.shivas.server.core.items;

import org.shivas.server.database.models.GameItem;

public interface PersistentBag extends Bag {

	GameItem persist(GameItem item);
	boolean delete(GameItem item);
	
}
