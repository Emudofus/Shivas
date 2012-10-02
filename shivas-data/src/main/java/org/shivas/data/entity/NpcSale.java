package org.shivas.data.entity;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 03/10/12
 * Time: 00:37
 */
public class NpcSale implements Serializable {
    private static final long serialVersionUID = 1625073160142742199L;

    private Npc npc;
    private ItemTemplate item;
    private short price;

    public Npc getNpc() {
        return npc;
    }

    public void setNpc(Npc npc) {
        this.npc = npc;
    }

    public ItemTemplate getItem() {
        return item;
    }

    public void setItem(ItemTemplate item) {
        this.item = item;
    }

    public short getPrice() {
        return price;
    }

    public void setPrice(short price) {
        this.price = price;
    }
}
