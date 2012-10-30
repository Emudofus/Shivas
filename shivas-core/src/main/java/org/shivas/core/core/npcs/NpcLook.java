package org.shivas.core.core.npcs;

import org.shivas.data.entity.ItemTemplate;
import org.shivas.data.entity.Npc;
import org.shivas.core.core.Colors;
import org.shivas.core.core.Look;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/09/12
 * Time: 20:19
 */
public class NpcLook implements Look {
    private static int[] itemTemplateToArray(ItemTemplate[] itemTemplates) {
        int[] accessories = new int[5];
        int i = 0;
        for (ItemTemplate item : itemTemplates) {
            accessories[i] = item != null ? item.getId() : -1;
            i++;
        }
        return accessories;
    }

    private final Npc npc;

    private Colors colors;
    private int[] accessoriesId;

    public NpcLook(Npc npc) {
        this.npc = npc;
    }

    @Override
    public short skin() {
        return npc.getTemplate().getSkin();
    }

    @Override
    public void setSkin(short skin) { }

    @Override
    public short size() {
        return npc.getTemplate().getSize();
    }

    @Override
    public void setSize(short size) { }

    @Override
    public Colors colors() {
        if (colors == null) {
            colors = new Colors(npc.getTemplate().getColor1(), npc.getTemplate().getColor2(), npc.getTemplate().getColor3());
        }
        return colors;
    }

    @Override
    public void setColors(Colors colors) { }

    @Override
    public int[] accessories() {
        if (accessoriesId == null) {
            accessoriesId = itemTemplateToArray(npc.getTemplate().getAccessories());
        }

        return accessoriesId;
    }
}
