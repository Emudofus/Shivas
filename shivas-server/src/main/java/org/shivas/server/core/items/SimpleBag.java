package org.shivas.server.core.items;

import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import org.shivas.data.entity.ItemTemplate;
import org.shivas.protocol.client.enums.ItemPositionEnum;
import org.shivas.protocol.client.enums.ItemTypeEnum;
import org.shivas.protocol.client.types.BaseItemType;
import org.shivas.server.database.models.GameItem;
import org.shivas.server.utils.Converters;
import org.shivas.server.utils.Filters;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class SimpleBag implements Bag {
	
	public static boolean validMovement(ItemTypeEnum type, ItemPositionEnum position) {
		switch (position) {
		case Amulet:
			return type == ItemTypeEnum.Amulet;
			
		case Belt:
			return type == ItemTypeEnum.Belt;
			
		case Boot:
			return type == ItemTypeEnum.Boot;
			
		case Cloak:
			return type == ItemTypeEnum.Cloack;
			
		case Dofus1:
		case Dofus2:
		case Dofus3:
		case Dofus4:
		case Dofus5:
		case Dofus6:
			return type == ItemTypeEnum.Dofus;
			
		case Hat:
			return type == ItemTypeEnum.Hat;
			
		case LeftRing:
		case RightRing:
			return type == ItemTypeEnum.Ring;
			
		case Pet:
			return type == ItemTypeEnum.Pet;
			
		case Shield:
			return type == ItemTypeEnum.Shield;
			
		case Weapon:
			return type.isWeapon();
			
		default:
			return true;
		}
	}
	
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
	
	public GameItem get(ItemTemplate template) {
		for (GameItem item : this) {
			if (item.getTemplate() == template) {
                return item;
			}
		}
		return null;
	}

    @Override
    public boolean has(ItemPositionEnum position) {
        for (GameItem item : this) {
            if (item.getPosition() == position) {
                return true;
            }
        }
        return false;
    }

    public void add(GameItem item) {
		items.put(item.getId(), item);
	}
	
	public GameItem remove(long itemId) {
		return items.remove(itemId);
	}
	
	public boolean remove(GameItem item) {
		return items.remove(item.getId()) == item;
	}
	
	public int count() {
		return items.size();
	}

	public boolean contains(GameItem item) {
		return items.containsValue(item);
	}
	
	public GameItem sameAs(GameItem item) {
		for (GameItem i : items.values()) {
			if (i.getPosition() == ItemPositionEnum.NotEquiped && i.equals(item)) {
				return i;
			}
		}
		return null;
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
		return Collections2.filter(items.values(), Filters.EQUIPED_ITEM_FILTER);
	}
	
	public boolean validMovement(GameItem item, ItemPositionEnum pos) {
		if (!validMovement(item.getTemplate().getType(), pos)) return false;
		
		if (pos != ItemPositionEnum.NotEquiped && get(pos) != null) return false;
		
		if (item.getTemplate().getType() == ItemTypeEnum.Ring) {
			ItemPositionEnum other = pos == ItemPositionEnum.LeftRing ? ItemPositionEnum.RightRing : ItemPositionEnum.LeftRing;
			
			GameItem otherItem = get(other);

			if (otherItem != null) return otherItem.getTemplate() != item.getTemplate();
		}
		
		return true;
	}

	@Override
	public Iterator<GameItem> iterator() {
		return items.values().iterator();
	}

	@Override
	public Collection<BaseItemType> toBaseItemType() {
		return Collections2.transform(items.values(), Converters.GAMEITEM_TO_BASEITEMTYPE);
	}

	@Override
	public Collection<GameItem> toCollection() {
		return items.values();
	}

}
