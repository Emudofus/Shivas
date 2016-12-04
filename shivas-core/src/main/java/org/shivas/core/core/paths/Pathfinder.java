package org.shivas.core.core.paths;

import org.shivas.common.collections.SortedArrayList;
import org.shivas.data.entity.MapTemplate;
import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.core.core.Location;

import static org.shivas.core.utils.Cells.distanceBetween;
import static org.shivas.core.utils.Cells.getCellIdByOrientation;

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
    protected final boolean allDirections;

    protected final SortedArrayList<ScoredNode> open = new SortedArrayList<>();
    protected final Path path = new Path();

    protected boolean found;

    public Pathfinder(ScoredNode start, short target, MapTemplate map, boolean allDirections) {
        this.start = start;
        this.target = target;
        this.map = map;
        this.allDirections = allDirections;
    }

    public Pathfinder(Location start, short target, boolean allDirections) {
        this(new ScoredNode(start.getOrientation(), start.getCell()), target, start.getMap(), allDirections);
    }

    public Pathfinder(short start, short target, OrientationEnum currentOrientation, MapTemplate map, boolean allDirections) {
        this(new ScoredNode(currentOrientation, start), target, map, allDirections);
    }

    public Path find() throws PathNotFoundException {
        if (found) return path;
        found = true;

        addAdjacents(start, allDirections);

        while (!open.isEmpty()) {
            ScoredNode best = open.remove(0);
            path.add(best);
            onAdded(best);
            if (mayStop(best)) break;

            addAdjacents(best, allDirections);
            if (open.isEmpty()) throw new PathNotFoundException();
        }

        return path;
    }

    protected void onAdded(Node node) {

    }

    protected boolean mayStop(Node node) {
        return node.cell() == target;
    }

    protected boolean canAdd(short cellId) {
        return !path.contains(cellId) && map.getCell(cellId).isWalkable();
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
