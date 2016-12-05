package org.shivas.core.utils;

import com.google.common.collect.ImmutableMap;
import org.shivas.common.StringUtils;
import org.shivas.common.maths.Point;
import org.shivas.common.maths.Vector;
import org.shivas.core.core.paths.Node;
import org.shivas.core.core.paths.Path;
import org.shivas.data.entity.GameCell;
import org.shivas.data.entity.MapTemplate;
import org.shivas.protocol.client.enums.OrientationEnum;

import java.util.Map;

public final class Cells {
	private Cells() {}

    public static final Map<OrientationEnum, Vector> VECTORS =
            ImmutableMap.<OrientationEnum, Vector>builder()
                .put(OrientationEnum.EAST,       Vector.create( 1, -1))
                .put(OrientationEnum.SOUTH_EAST, Vector.create( 1,  0))
                .put(OrientationEnum.SOUTH,      Vector.create( 1,  1))
                .put(OrientationEnum.SOUTH_WEST, Vector.create( 0,  1))
                .put(OrientationEnum.WEST,       Vector.create(-1,  1))
                .put(OrientationEnum.NORTH_WEST, Vector.create(-1,  0))
                .put(OrientationEnum.NORTH,      Vector.create(-1, -1))
                .put(OrientationEnum.NORTH_EAST, Vector.create( 0, -1))
            .build();

    public static String encode(short cellid) {
        return Character.toString(StringUtils.EXTENDED_ALPHABET.charAt(cellid / 64)) +
                Character.toString(StringUtils.EXTENDED_ALPHABET.charAt(cellid % 64));
	}
	
	public static String encode(GameCell cell) {
		return encode(cell.getId());
	}
	
	public static short decode(String string) {
        return (short) (StringUtils.EXTENDED_ALPHABET.indexOf(string.charAt(0)) * 64 +
                StringUtils.EXTENDED_ALPHABET.indexOf(string.charAt(1)));
	}
	
	public static String encode(OrientationEnum orientation) {
		return String.valueOf(StringUtils.EXTENDED_ALPHABET.charAt(orientation.ordinal()));
	}

    public static OrientationEnum decode(char orientationCode){
        return OrientationEnum.valueOf(StringUtils.EXTENDED_ALPHABET.indexOf(orientationCode));
    }

    public static Point position(short cellId, int mapWidth) {
        int _loc5 = (int) Math.floor(cellId / (mapWidth * 2 - 1));
        int _loc6 = cellId - _loc5 * (mapWidth * 2 - 1);
        int _loc7 = _loc6 % mapWidth;
        int x = (cellId - (mapWidth - 1) * (_loc5 - _loc7)) / mapWidth;
        int y = _loc5 - _loc7;

        return new Point(x, y);
    }

    public static Point position(short cellId, MapTemplate map) {
        return position(cellId, map.getWidth());
    }

    public static Point position(Node node, MapTemplate map) {
        return position(node.cell(), map);
    }

    public static Point position(GameCell cell, MapTemplate map) {
        return position(cell.getId(), map);
    }

    public static int manhattanDistanceBetween(Point a, Point b) {
        return Math.abs(a.abscissa() - b.abscissa()) + Math.abs(a.ordinate() - b.ordinate());
    }

    public static int manhattanDistanceBetween(GameCell a, GameCell b, MapTemplate m) {
        return manhattanDistanceBetween(position(a, m), position(b, m));
    }

    public static double distanceBetween(Point a, Point b) {
        // Euclidean
        return (int) Math.ceil(Math.sqrt(Math.pow(a.abscissa() - b.abscissa(), 2) + Math.pow(a.ordinate() - b.ordinate(), 2)));
    }

    public static double distanceBetween(Node a, Node b, MapTemplate map) {
        return distanceBetween(position(a, map), position(b, map));
    }

    public static double distanceBetween(Node a, short b, MapTemplate map) {
        return distanceBetween(position(a.cell(), map), position(b, map));
    }

    public static double distanceBetween(GameCell a, GameCell b, MapTemplate map) {
        return distanceBetween(position(a, map), position(b, map));
    }

    public static short getCellIdByOrientation(short cellId, OrientationEnum orientation, int mapWidth) {
        switch (orientation) {
        case EAST:
            return (short) (cellId + 1);
        case SOUTH_EAST:
            return (short) (cellId + mapWidth);
        case SOUTH:
            return (short) (cellId + (mapWidth * 2 - 1));
        case SOUTH_WEST:
            return (short) (cellId + (mapWidth - 1));
        case WEST:
            return (short) (cellId - 1);
        case NORTH_WEST:
            return (short) (cellId - mapWidth);
        case NORTH:
            return (short) (cellId - (mapWidth * 2 - 1));
        case NORTH_EAST:
            return (short) (cellId - mapWidth + 1);

        default:
            throw new IllegalArgumentException("invalid orientation");
        }
    }

    public static short getCellIdByOrientation(short cellId, OrientationEnum orientation, MapTemplate map) {
        return getCellIdByOrientation(cellId, orientation, map.getWidth());
    }

    public static short getCellIdByOrientation(Node node, OrientationEnum orientation, MapTemplate map) {
        return getCellIdByOrientation(node.cell(), orientation, map);
    }

    public static long estimateTime(Path path, int mapWidth){
        long time = 0;
        int steps = path.size();

        for (int i = 0; i < steps - 1; ++i){
            Node current = path.get(i), next = path.get(i + 1);
            switch (next.orientation()){
            case EAST:
            case WEST:
                time += ( Math.abs(current.cell() - next.cell()) ) * (steps >= 4 ? 350 : 875);
                break;

            case NORTH:
            case SOUTH:
                time += ( Math.abs(current.cell() - next.cell()) / ( mapWidth * 2 - 1 ) ) * (steps >= 4 ? 350 : 875);
                break;

            case NORTH_EAST:
            case SOUTH_EAST:
                time += ( Math.abs(current.cell() - next.cell()) / ( mapWidth - 1 ) ) * (steps >= 4 ? 250 : 625);
                break;

            case NORTH_WEST:
            case SOUTH_WEST:
                time += ( Math.abs(current.cell() - next.cell()) / (mapWidth - 1) ) * (steps >= 4 ? 250 : 625);
                break;
            }
        }

        return time;
    }

    public static long estimateTime(Path path, MapTemplate map) {
        return estimateTime(path, map.getWidth());
    }

    public static OrientationEnum getOrientationByPoints(Point a, Point b) {
        Vector vector = Vector.fromPoints(a, b);
        for (Map.Entry<OrientationEnum, Vector> entry : VECTORS.entrySet()) {
            if (entry.getValue().hasSameDirectionOf(vector)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static OrientationEnum getOrientationByCells(short firstCellId, short secondCellId, int mapWidth) {
        return getOrientationByPoints(position(firstCellId, mapWidth), position(secondCellId, mapWidth));
    }

    public static OrientationEnum getOrientationByCells(short firstCellId, short secondCellId, MapTemplate map) {
        return getOrientationByCells(firstCellId, secondCellId, map.getWidth());
    }

    public static OrientationEnum getOrientationByCells(GameCell firstCell, GameCell secondCell, MapTemplate map) {
        return getOrientationByCells(firstCell.getId(), secondCell.getId(), map);
    }

    public static OrientationEnum getOrientationByNodes(Node first, Node second, MapTemplate map) {
        return getOrientationByCells(first.cell(), second.cell(), map);
    }
}
