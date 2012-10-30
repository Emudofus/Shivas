package org.shivas.core.core.castables.zones;

import org.shivas.data.entity.GameCell;
import org.shivas.data.entity.MapTemplate;
import org.shivas.protocol.client.enums.ZoneTypeEnum;

import java.util.Collection;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 08:42
 */
public abstract class Zone {
    protected int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public abstract ZoneTypeEnum getZoneType();

    public abstract <C extends GameCell> Set<C> filter(C start, C target, Collection<C> cells, MapTemplate map);
}
