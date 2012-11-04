package org.shivas.core.core.castables.zones;

import com.google.common.collect.Sets;
import org.shivas.core.utils.Cells;
import org.shivas.data.entity.CellProvider;
import org.shivas.data.entity.GameCell;
import org.shivas.data.entity.MapTemplate;
import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.protocol.client.enums.ZoneTypeEnum;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 30/10/12
 * Time: 18:05
 */
public class LineZone extends Zone {
    public LineZone(int length) {
        this.length = length;
    }

    @Override
    public ZoneTypeEnum getZoneType() {
        return ZoneTypeEnum.LINE;
    }

    @Override
    public <C extends GameCell> Set<C> filter(C start, C target, CellProvider<C> cells, MapTemplate map) {
        OrientationEnum orientation = Cells.getOrientationByCells(start, target, map);

        Set<C> result = Sets.newHashSet();
        result.add(target);

        C last = target;
        for (int i = 0; i < length; ++i) {
            short cellId = Cells.getCellIdByOrientation(last.getId(), orientation, map);
            C cell = cells.getCell(cellId);
            result.add(cell);

            last = cell;
        }

        return result;
    }
}
