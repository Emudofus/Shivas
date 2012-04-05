package org.shivas.protocol.client.types;

import org.shivas.protocol.client.enums.ItemPositionEnum;

import java.util.Collection;

/**
 * User: Blackrush
 * Date: 26/12/11
 * Time: 20:48
 * IDE : IntelliJ IDEA
 */
public class BaseItemType {
    private long id;
    private int templateId;
    private int quantity;
    private ItemPositionEnum position;
    private Collection<BaseItemEffectType> effects;

    public BaseItemType(long id, int templateId, int quantity, ItemPositionEnum position, Collection<BaseItemEffectType> effects) {
        this.id = id;
        this.templateId = templateId;
        this.quantity = quantity;
        this.position = position;
        this.effects = effects;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ItemPositionEnum getPosition() {
        return position;
    }

    public void setPosition(ItemPositionEnum position) {
        this.position = position;
    }

    public Collection<BaseItemEffectType> getEffects() {
        return effects;
    }

    public void setEffects(Collection<BaseItemEffectType> effects) {
        this.effects = effects;
    }
}
