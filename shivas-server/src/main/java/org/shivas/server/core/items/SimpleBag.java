package org.shivas.server.core.items;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.shivas.data.entity.ItemTemplate;
import org.shivas.protocol.client.enums.ItemPositionEnum;
import org.shivas.protocol.client.types.BaseItemType;
import org.shivas.server.database.models.GameItem;
import org.shivas.server.utils.Converters;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SimpleBag implements Bag {
	
	private final Map<Long, GameItem> items = Maps.newHashMap();
	
	public GameItem get(long itemId) {
		return items.get(itemId);
	}
	
	public GameItem get(ItemPositionEnum position) {
		for (GameItem item : this) {
			if (item.getPosition().equals(position)) {
				return item;
			}
		}
		return null;
	}
	
	public Collection<GameItem> get(ItemTemplate template) {
		List<GameItem> items = Lists.newArrayList();
		for (GameItem item : this) {
			if (item.getTemplate() == template) {
				items.add(item);
			}
		}
		return items;
	}
	
	public void add(GameItem item) {
		items.put(item.id(), item);
	}
	
	public GameItem remove(long itemId) {
		return items.remove(itemId);
	}
	
	public boolean remove(GameItem item) {
		return items.remove(item.id()) == item;
	}
	
	public int count() {
		return items.size();
	}
	
	public GameItem[] accessories() {
        GameItem[] accessories = new GameItem[5];
        accessories[0] = get(ItemPositionEnum.Weapon);
        accessories[1] = get(ItemPositionEnum.Hat);
        accessories[2] = get(ItemPositionEnum.Cloak);
        accessories[3] = get(ItemPositionEnum.Pet);
        accessories[4] = get(ItemPositionEnum.Shield);
        return accessories;
	}
	
	public int[] accessoriesTemplateId() {
        int[] accessories = new int[5];
        
        GameItem item;
        
        item = get(ItemPositionEnum.Weapon);
        accessories[0] = item != null ? item.getTemplate().getId() : -1;
        item = get(ItemPositionEnum.Hat);
        accessories[1] = item != null ? item.getTemplate().getId() : -1;
        item = get(ItemPositionEnum.Cloak);
        accessories[2] = item != null ? item.getTemplate().getId() : -1;
        item = get(ItemPositionEnum.Pet);
        accessories[3] = item != null ? item.getTemplate().getId() : -1;
        item = get(ItemPositionEnum.Shield);
        accessories[4] = item != null ? item.getTemplate().getId() : -1;
        
        return accessories;
	}
	
	public Collection<GameItem> equiped() {
		List<GameItem> equiped = Lists.newArrayList();
		for (GameItem item : this) {
			if (item.getPosition().equipment()) {
				equiped.add(item);
			}
		}
		return equiped;
	}

	@Override
	public Iterator<GameItem> iterator() {
		return items.values().iterator();
	}

	@Override
	public Collection<BaseItemType> toBaseItemType() {
		return Collections2.transform(items.values(), Converters.GAMEITEM_TO_BASEITEMTYPE);
	}

}
