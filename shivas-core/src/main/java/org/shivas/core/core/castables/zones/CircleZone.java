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
public class CircleZone extends Zone {
    public CircleZone(int length) {
        this.length = length;
    }

    @Override
    public ZoneTypeEnum getZoneType() {
        return ZoneTypeEnum.CIRCLE;
    }

    @Override
    public <C extends GameCell> Set<C> filter(C start, C target, CellProvider<C> cells, MapTemplate map) {
        Set<C> result = Sets.newHashSet();
        result.add(target);
        addCells(result, target, cells, map, length);

        return result;
    }

    private <C extends GameCell> void addCells(Set<C> result, C source, CellProvider<C> cells, MapTemplate map, int remaining) {
        if (remaining <= 0) return;

        for (OrientationEnum orientation : OrientationEnum.ADJACENTS) {
            short cellId = Cells.getCellIdByOrientation(source.getId(), orientation, map);
            C cell = cells.getCell(cellId);
            result.add(cell);

            addCells(result, cell, cells, map, remaining - 1);
        }
    }
}
