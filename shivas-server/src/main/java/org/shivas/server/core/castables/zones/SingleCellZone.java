package org.shivas.server.core.castables.zones;

import com.google.common.collect.Sets;
import org.shivas.data.entity.GameCell;
import org.shivas.data.entity.MapTemplate;
import org.shivas.protocol.client.enums.ZoneTypeEnum;

import java.util.Collection;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 29/10/12
 * Time: 08:43
 */
public class SingleCellZone extends Zone {
    public static final SingleCellZone INSTANCE = new SingleCellZone();
    private SingleCellZone() {}

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public void setLength(int length) { }

    @Override
    public ZoneTypeEnum getZoneType() {
        return ZoneTypeEnum.SINGLE_CELL;
    }

    @Override
    public <C extends GameCell> Set<C> filter(C start, C target, Collection<C> cells, MapTemplate map) {
        return Sets.newHashSet(target);
    }
}
