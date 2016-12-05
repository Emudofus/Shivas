package org.shivas.data.entity;

import com.google.common.base.Function;
import org.shivas.data.EntityFactory;
import org.shivas.protocol.client.enums.ItemTypeEnum;
import org.shivas.protocol.client.types.BaseItemTemplateEffectType;
import org.shivas.protocol.client.types.BaseItemTemplateType;

import java.io.Serializable;
import java.util.Collection;

import static org.shivas.common.collections.CollectionQuery.from;

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
	 * @param itemSet the itemSet to set
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
		Item item = factory.newItem(this);
		item.setTemplate(this);
        item.setItemEffects(from(effects).transform(GENERATE_EFFECT).computeList());
		
		return item;
	}

    public BaseItemTemplateType toBaseItemTemplateType() {
        return new BaseItemTemplateType(id, from(effects).transform(CONVERTER).lazyCollection());
    }

    private static final Function<ItemEffectTemplate, ItemEffect> GENERATE_EFFECT = new Function<ItemEffectTemplate, ItemEffect>() {
        public ItemEffect apply(ItemEffectTemplate input) {
            return input.generate();
        }
    };

    private static final Function<ItemEffectTemplate,BaseItemTemplateEffectType> CONVERTER = new Function<ItemEffectTemplate, BaseItemTemplateEffectType>() {
        public BaseItemTemplateEffectType apply(ItemEffectTemplate input) {
            return input.toBaseItemTemplateEffectType();
        }
    };

	@SuppressWarnings("RedundantIfStatement")
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ItemTemplate that = (ItemTemplate) o;

		if (id != that.id) return false;
		if (level != that.level) return false;
		if (weight != that.weight) return false;
		if (forgemageable != that.forgemageable) return false;
		if (price != that.price) return false;
		if (itemSet != null ? !itemSet.equals(that.itemSet) : that.itemSet != null) return false;
		if (type != that.type) return false;
		if (conditions != null ? !conditions.equals(that.conditions) : that.conditions != null) return false;
		if (effects != null ? !effects.equals(that.effects) : that.effects != null) return false;
		if (factory != null ? !factory.equals(that.factory) : that.factory != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = (int) id;
		result = 31 * result + (itemSet != null ? itemSet.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (int) level;
		result = 31 * result + (int) weight;
		result = 31 * result + (forgemageable ? 1 : 0);
		result = 31 * result + (int) price;
		result = 31 * result + (conditions != null ? conditions.hashCode() : 0);
		result = 31 * result + (effects != null ? effects.hashCode() : 0);
		result = 31 * result + (factory != null ? factory.hashCode() : 0);
		return result;
	}
}
