package org.shivas.protocol.client.types;

import java.util.Collection;

import org.shivas.protocol.client.enums.ItemPositionEnum;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 23/02/12
 * Time: 18:49
 */
public class StoreItemType extends BaseItemType {
    private long price;

    public StoreItemType(long id, int templateId, int quantity, ItemPositionEnum position, Collection<BaseItemEffectType> effects, long price) {
        super(id, templateId, quantity, position, effects);
        this.price = price;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
