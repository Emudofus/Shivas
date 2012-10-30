package org.shivas.core.core.castables.zones;

import org.shivas.data.entity.GameCell;
import org.shivas.data.entity.MapTemplate;
import org.shivas.protocol.client.enums.ZoneTypeEnum;

import java.util.Collection;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 30/10/12
 * Time: 18:04
 */
public class CrossZone extends Zone {
    @Override
    public ZoneTypeEnum getZoneType() {
        return ZoneTypeEnum.CROSS;
    }

    @Override
    public <C extends GameCell> Set<C> filter(C start, C target, Collection<C> cells, MapTemplate map) {
        return null; // TODO cross zone
    }
}
