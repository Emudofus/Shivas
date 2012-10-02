package org.shivas.data.entity;

import java.io.Serializable;
import java.util.Collection;

import com.google.common.collect.Collections2;
import org.shivas.common.collections.Collections3;
import org.shivas.data.EntityFactory;
import org.shivas.protocol.client.enums.ItemTypeEnum;

import com.google.common.base.Function;
import org.shivas.protocol.client.types.BaseItemTemplateEffectType;
import org.shivas.protocol.client.types.BaseItemTemplateType;

import javax.annotation.Nullable;

public class ItemTemplate implements Serializable {

	private static final long serialVersionUID = -4455899142517688060L;
	
	private short id;
	private ItemSet itemSet;
	private ItemTypeEnum type;
	private short level;
	private short weight;
	private boolean forgemageable;
	private short price;
	private String conditions;
	private Collection<ItemEffectTemplate> effects;
	
	protected final EntityFactory factory;
	
	public ItemTemplate(EntityFactory factory) {
		this.factory = factory;
	}
	
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
	 * @return the item set
	 */
	public ItemSet getItemSet() {
		return itemSet;
	}
	/**
	 * @param set the item set to set
	 */
	public void setItemSet(ItemSet itemSet) {
		this.itemSet = itemSet;
	}
	/**
	 * @return the type
	 */
	public ItemTypeEnum getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(ItemTypeEnum type) {
		this.type = type;
	}
	/**
	 * @return the level
	 */
	public short getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(short level) {
		this.level = level;
	}
	/**
	 * @return the weight
	 */
	public short getWeight() {
		return weight;
	}
	/**
	 * @param weight the weight to set
	 */
	public void setWeight(short weight) {
		this.weight = weight;
	}
	/**
	 * @return the forgemageable
	 */
	public boolean isForgemageable() {
		return forgemageable;
	}
	/**
	 * @param forgemageable the forgemageable to set
	 */
	public void setForgemageable(boolean forgemageable) {
		this.forgemageable = forgemageable;
	}
	/**
	 * @return the price
	 */
	public short getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(short price) {
		this.price = price;
	}
	/**
	 * @return the conditions
	 */
	public String getConditions() {
		return conditions;
	}
	/**
	 * @param conditions the conditions to set
	 */
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}
	
	/**
	 * @return the effects
	 */
	public Collection<ItemEffectTemplate> getEffects() {
		return effects;
	}
	/**
	 * @param effects the effects to set
	 */
	public void setEffects(Collection<ItemEffectTemplate> effects) {
		this.effects = effects;
	}
	
	public Item generate() {
		Item item = factory.newItem();
		
		item.setTemplate(this);
		
		item.setEffects(Collections3.transform(effects, new Function<ItemEffectTemplate, ItemEffect>() {
			public ItemEffect apply(ItemEffectTemplate arg0) {
				return arg0.generate();
			}
		}));
		
		return item;
	}

    public BaseItemTemplateType toBaseItemTemplateType() {
        return new BaseItemTemplateType(id, Collections2.transform(effects, new Function<ItemEffectTemplate, BaseItemTemplateEffectType>() {
            public BaseItemTemplateEffectType apply(ItemEffectTemplate input) {
                return input.toBaseItemTemplateEffectType();
            }
        }));
    }
}
