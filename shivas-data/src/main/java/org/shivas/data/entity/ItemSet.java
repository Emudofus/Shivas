package org.shivas.data.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class ItemSet implements Serializable {

	private static final long serialVersionUID = 4217103727949901207L;
	
	private short id;
	private Collection<ItemTemplate> items;
	private List<Collection<ItemEffect>> effects;
	
	/**
	 * @return the id
	 */
	public short getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(short id) {
		this.id = id;
	}
	/**
	 * @return the items
	 */
	public Collection<ItemTemplate> getItems() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(Collection<ItemTemplate> items) {
		this.items = items;
	}
	/**
	 * @return the effects
	 */
	public List<Collection<ItemEffect>> getEffects() {
		return effects;
	}
	/**
	 * @param effects the effects to set
	 */
	public void setEffects(List<Collection<ItemEffect>> effects) {
		this.effects = effects;
	}

}
