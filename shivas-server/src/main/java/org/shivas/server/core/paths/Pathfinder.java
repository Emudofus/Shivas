package org.shivas.server.core.paths;

import org.shivas.common.collections.SortedArrayList;
import org.shivas.data.entity.MapTemplate;
import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.server.core.Location;

import static org.shivas.server.utils.Cells.distanceBetween;
import static org.shivas.server.utils.Cells.getCellIdByOrientation;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 21/10/12
 * Time: 10:28
 */
public class Pathfinder {
    protected final ScoredNode start;
    protected final MapTemplate map;
    protected final short target;

    protected final SortedArrayList<ScoredNode> open = new SortedArrayList<ScoredNode>();

    public Pathfinder(ScoredNode start, short target, MapTemplate map) {
        this.start = start;
        this.target = target;
        this.map = map;
    }

    public Pathfinder(Location start, short target) {
        this(new ScoredNode(start.getOrientation(), start.getCell()), target, start.getMap());
    }

    public Pathfinder(short start, short target, OrientationEnum currentOrientation, MapTemplate map) {
        this(new ScoredNode(currentOrientation, start), target, map);
    }

    public Path find(boolean allDirections) throws PathNotFoundException {
        Path path = new Path();
        addAdjacents(start, allDirections);

        while (!open.isEmpty()) {
            ScoredNode best = open.remove(0);
            path.add(best);
            if (mayStop(best)) break;

            addAdjacents(best, allDirections);
            if (open.isEmpty()) throw new PathNotFoundException();
        }

        return path;
    }

    protected boolean mayStop(Node node) {
        return node.cell() == target;
    }

    protected boolean canAdd(short cellId) {
        return map.getCell(cellId).isWalkable();
    }

    protected void addAdjacents(Node node, boolean allDirections) {
        for (OrientationEnum orientation : (allDirections ? OrientationEnum.values() : OrientationEnum.ADJACENTS)) {
            short cellId = getCellIdByOrientation(node, orientation, map);

            if (cellId != -1 && canAdd(cellId)) {
                ScoredNode newNode = new ScoredNode(orientation, cellId);
                newNode.setDistanceToStart(distanceBetween(start, newNode, map));
                newNode.setDistanceToEnd(distanceBetween(newNode, target, map));

                open.add(newNode);
            }
        }
    }
}
