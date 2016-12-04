package org.shivas.core.core.paths;

import org.shivas.data.entity.MapTemplate;
import org.shivas.core.utils.Cells;

import java.util.ArrayList;

public class Path extends ArrayList<Node> {

	private static final long serialVersionUID = -7753768718718684848L;

	public static Path parsePath(String string) {
		Path path = new Path();
		for (int i = 0; i < string.length(); i += 3) {
			String part = string.substring(i, i + 3);
			Node node = Node.parseNode(part);
			path.add(node);
		}
		return path;
	}

    public static Path parsePathWithoutOrientation(String string) {
        Path path = new Path();
        for (int i = 0; i < string.length(); i += 2) {
            String part = string.substring(i, i + 2);
            Node node = Node.parseNodeWithoutOrientation(part);
            path.add(node);
        }
        return path;
    }
	
	public Node first() {
		return get(0);
	}
	
	public Node last() {
		return get(size() - 1);
	}

    public boolean contains(short cellId) {
        for (Node node : this) {
            if (node.cell() == cellId) return true;
        }
        return false;
    }

    public long estimateTimeOn(MapTemplate map) {
        return Cells.estimateTime(this, map);
    }
	
	public String toString() {
		StringBuilder sb = new StringBuilder(size() * 3);
        for (Node node : this) {
            sb.append(node.encode());
        }
		return sb.toString();
	}
	
}
