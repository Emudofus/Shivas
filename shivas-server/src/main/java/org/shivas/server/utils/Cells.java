package org.shivas.server.utils;

import org.shivas.common.StringUtils;
import org.shivas.common.maths.Point;
import org.shivas.data.entity.GameCell;
import org.shivas.data.entity.MapTemplate;
import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.server.core.paths.Node;

public final class Cells {
	private Cells() {}
	
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

    public static int distanceBetween(Point a, Point b) {
        return Math.abs(a.abscissa() - b.abscissa()) +
                Math.abs(a.ordinate() - b.ordinate());
    }

    public static int distanceBetween(Node a, Node b, MapTemplate map) {
        return distanceBetween(position(a, map), position(b, map));
    }

    public static int distanceBetween(Node a, short b, MapTemplate map) {
        return distanceBetween(position(a.cell(), map), position(b, map));
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

    public static short getCellIdByOrientation(Node node, OrientationEnum orientation, MapTemplate map) {
        return getCellIdByOrientation(node.cell(), orientation, map.getWidth());
    }
}
