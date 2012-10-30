package org.shivas.core.core.items;

import org.shivas.data.entity.ItemTemplate;
import org.shivas.protocol.client.enums.ItemPositionEnum;
import org.shivas.protocol.client.types.BaseItemType;
import org.shivas.core.database.models.GameItem;

import java.util.Collection;

public interface Bag extends Iterable<GameItem> {
	GameItem get(long itemId);
	GameItem get(ItemPositionEnum position);
	GameItem get(ItemTemplate template);
    boolean has(ItemPositionEnum position);
	
	void add(GameItem item);
	GameItem remove(long itemId);
	boolean remove(GameItem item);
	int count();
	boolean contains(GameItem item);
	
	GameItem[] accessories();
	Collection<GameItem> equiped();
	
	Collection<BaseItemType> toBaseItemType();
	Collection<GameItem> toCollection();
}
