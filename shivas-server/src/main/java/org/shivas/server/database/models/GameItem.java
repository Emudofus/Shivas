package org.shivas.server.database.models;

import java.util.Collection;
import java.util.Map;

import org.atomium.PersistableEntity;
import org.shivas.common.collections.Maps2;
import org.shivas.data.entity.Item;
import org.shivas.data.entity.ConstantItemEffect;
import org.shivas.data.entity.ItemEffect;
import org.shivas.data.entity.ItemTemplate;
import org.shivas.protocol.client.enums.ItemEffectEnum;
import org.shivas.protocol.client.enums.ItemPositionEnum;
import org.shivas.protocol.client.types.BaseItemType;
import org.shivas.server.utils.Converters;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class GameItem implements Item, PersistableEntity<Long> {

	private static final long serialVersionUID = 9166056670961929357L;
	
	private long id;
	private ItemTemplate template;
	private Player owner;
	private Map<ItemEffectEnum, ItemEffect> effects;
	private ItemPositionEnum position;
	private int quantity;
	
	public GameItem() {
		position = ItemPositionEnum.NotEquiped;
		quantity = 1;
	}

	public GameItem(long id, ItemTemplate template, Player owner, Collection<ItemEffect> effects, ItemPositionEnum position, int quantity) {
		this.id = id;
		this.template = template;
		this.owner = owner;
		this.effects = Maps2.toMap(effects, new Function<ItemEffect, ItemEffectEnum>() {
			public ItemEffectEnum apply(ItemEffect input) {
				return input.getType();
			}
		});
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
		return effects.values();
	}

	@Override
	public void setEffects(Collection<ItemEffect> effects) {
		this.effects = Maps2.toMap(effects, new Function<ItemEffect, ItemEffectEnum>() {
			public ItemEffectEnum apply(ItemEffect input) {
				return input.getType();
			}
		});
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
	
	public GameItem slice(int quantity) {
		GameItem copy = copy();
		copy.setQuantity(quantity);
		
		minusQuantity(quantity);
		
		return copy;
	}
	
	public GameItem sliceOne() {
		return slice(1);
	}
	
	public GameItem copy() {
		return new GameItem(-1, template, owner, Collections2.transform(effects.values(), Converters.ITEMEFFECT_COPY), position, 0);
	}
	
	public BaseItemType toBaseItemType() {
		return new BaseItemType(
				id,
				template.getId(),
				quantity,
				position,
				Collections2.transform(effects.values(), Converters.ITEMEFFECT_TO_BASEITEMEFFECTTYPE)
		);
	}
	
	private static int sum(Item item) {
		int i = 0;
		for (ItemEffect effect : item.getEffects()) {
			if (effect instanceof ConstantItemEffect) {
				i += ((ConstantItemEffect) effect).getBonus();
			}
		}
		return i;
	}

	@Override
	public int compareTo(Item other) {
		return sum(this) - sum(other);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		GameItem other = (GameItem) obj;
		
		if (other.template == template)
			return true;
		if (sum(this) != sum(other))
			return false;
		
		for (ItemEffectEnum e : effects.keySet()) {
			ItemEffect first  = effects.get(e),
					   second = other.effects.get(e);

			if (!first.equals(second)) return false;
		}
		
		return true;
	}
	
	

}
