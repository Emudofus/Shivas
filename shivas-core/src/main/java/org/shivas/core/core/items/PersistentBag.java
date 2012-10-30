package org.shivas.core.core.items;

import org.shivas.core.database.models.GameItem;

public interface PersistentBag extends Bag {

	GameItem persist(GameItem item);
	boolean delete(GameItem item);
	
}
