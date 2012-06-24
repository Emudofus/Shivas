package org.shivas.server.core.items;

import java.util.Collection;

import org.shivas.data.entity.ItemTemplate;
import org.shivas.protocol.client.enums.ItemPositionEnum;
import org.shivas.protocol.client.types.BaseItemType;
import org.shivas.server.database.models.GameItem;

public interface Bag extends Iterable<GameItem> {
	GameItem get(long itemId);
	GameItem get(ItemPositionEnum position);
	Collection<GameItem> get(ItemTemplate template);
	
	void add(GameItem item);
	GameItem remove(long itemId);
	boolean remove(GameItem item);
	int count();
	boolean contains(GameItem item);
	
	GameItem[] accessories();
	Collection<GameItem> equiped();
	
	Collection<BaseItemType> toBaseItemType();
}
