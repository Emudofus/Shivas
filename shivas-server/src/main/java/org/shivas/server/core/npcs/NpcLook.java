package org.shivas.server.core.npcs;

import org.shivas.data.entity.ItemTemplate;
import org.shivas.data.entity.Npc;
import org.shivas.server.core.Colors;
import org.shivas.server.core.Look;

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
            accessories[i] = item.getId();
            i++;
        }
        return accessories;
    }

    private final Npc npc;
    private final Colors colors;
    private final int[] accessoriesId;

    public NpcLook(Npc npc) {
        this.npc = npc;
        this.colors = new Colors(npc.getTemplate().getColor1(), npc.getTemplate().getColor2(), npc.getTemplate().getColor3());
        this.accessoriesId = itemTemplateToArray(npc.getTemplate().getAccessories());
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
        return colors;
    }

    @Override
    public void setColors(Colors colors) { }

    @Override
    public int[] accessories() {
        return accessoriesId;
    }
}
