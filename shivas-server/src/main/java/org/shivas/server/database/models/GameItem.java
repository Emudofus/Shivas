package org.shivas.server.database.models;

import java.util.Collection;

import org.atomium.PersistableEntity;
import org.shivas.data.entity.Item;
import org.shivas.data.entity.ItemEffect;
import org.shivas.data.entity.ItemTemplate;

public class GameItem implements Item, PersistableEntity<Long> {

	private static final long serialVersionUID = 9166056670961929357L;
	
	private long id;
	private ItemTemplate template;
	private Collection<ItemEffect> effects;
	
	public GameItem() {
	}

	public GameItem(long id, ItemTemplate template, Collection<ItemEffect> effects) {
		this.id = id;
		this.template = template;
		this.effects = effects;
	}

	@Override
	public Long id() {
		return id;
	}

	@Override
	public void setId(Long pk) {
		this.id = pk;
	}

	@Override
	public ItemTemplate getTemplate() {
		return template;
	}

	@Override
	public void setTemplate(ItemTemplate template) {
		this.template = template;
	}

	@Override
	public Collection<ItemEffect> getEffects() {
		return effects;
	}

	@Override
	public void setEffects(Collection<ItemEffect> effects) {
		this.effects = effects;
	}

}
