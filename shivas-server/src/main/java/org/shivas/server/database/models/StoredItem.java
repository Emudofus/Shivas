package org.shivas.server.database.models;

import com.google.common.collect.Collections2;
import org.atomium.PersistableEntity;
import org.shivas.protocol.client.types.StoreItemType;
import org.shivas.server.utils.Converters;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 20/09/12
 * Time: 18:40
 */
public class StoredItem implements PersistableEntity<Long> {
    private GameItem item;
    private int quantity;
    private long price;

    @Override
    public Long getId() {
        return item.getId();
    }

    @Override
    public void setId(Long id) { }

    public GameItem getItem() {
        return item;
    }

    public void setItem(GameItem item) {
        this.item = item;
    }

    public Player getOwner() {
        return item.getOwner();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public StoredItem plusQuantity(int quantity) {
        this.quantity += quantity;
        return this;
    }

    public StoredItem minusQuantity(int quantity) {
        this.quantity -= quantity;
        return this;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public StoreItemType toStoreItemType() {
        return new StoreItemType(
                item.getId(),
                item.getTemplate().getId(),
                quantity,
                item.getPosition(),
                Collections2.transform(item.getItemEffects(), Converters.ITEMEFFECT_TO_BASEITEMEFFECTTYPE),
                price
        );
    }
}
