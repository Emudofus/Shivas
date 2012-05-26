package org.shivas.server.database.models;

import java.util.Collection;

import org.atomium.PersistableEntity;
import org.shivas.data.entity.Item;
import org.shivas.data.entity.ItemEffect;
import org.shivas.data.entity.ItemTemplate;
import org.shivas.protocol.client.enums.ItemPositionEnum;

public class GameItem implements Item, PersistableEntity<Long> {

	private static final long serialVersionUID = 9166056670961929357L;
	
	private long id;
	private ItemTemplate template;
	private Player owner;
	private Collection<ItemEffect> effects;
	private ItemPositionEnum position;
	private int quantity;
	
	public GameItem() {
	}

	public GameItem(long id, ItemTemplate template, Player owner, Collection<ItemEffect> effects, ItemPositionEnum position, int quantity) {
		this.id = id;
		this.template = template;
		this.owner = owner;
		this.effects = effects;
		this.position = position;
		this.quantity = quantity;
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

	/**
	 * @return the owner
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	@Override
	public Collection<ItemEffect> getEffects() {
		return effects;
	}

	@Override
	public void setEffects(Collection<ItemEffect> effects) {
		this.effects = effects;
	}

	/**
	 * @return the position
	 */
	public ItemPositionEnum getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(ItemPositionEnum position) {
		this.position = position;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @param quantity the quantity to add
	 */
	public void plusQuantity(int quantity) {
		this.quantity += quantity;
	}

	/**
	 * @param quantity the quantity to remove
	 */
	public void minusQuantity(int quantity) {
		this.quantity -= quantity;
	}

}
