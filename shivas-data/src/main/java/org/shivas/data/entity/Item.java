package org.shivas.data.entity;

import java.io.Serializable;
import java.util.Collection;

public interface Item extends Serializable, Comparable<Item> {

	ItemTemplate getTemplate();
	void setTemplate(ItemTemplate template);

	Collection<ItemEffect> getEffects();
	void setEffects(Collection<ItemEffect> effects);
}
