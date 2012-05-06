package org.shivas.server.core;

import java.util.ArrayList;

import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.server.utils.Cells;

public class Path extends ArrayList<Path.Node> {

	private static final long serialVersionUID = -7753768718718684848L;

	public static class Node {
		public static Node parseNode(String string) {
			return new Node(
					Cells.decode(string.charAt(0)),
					Cells.decode(string.substring(1))
			);
		}
		
		private OrientationEnum orientation;
		private short cell;
		
		public Node() {
		}

		public Node(OrientationEnum orientation, short cell) {
			this.orientation = orientation;
			this.cell = cell;
		}

		public OrientationEnum orientation() {
			return orientation;
		}

		public void setOrientation(OrientationEnum orientation) {
			this.orientation = orientation;
		}

		public short cell() {
			return cell;
		}

		public void setCell(short cell) {
			this.cell = cell;
		}
		
		public String toString() {
			return Cells.encode(orientation) +
				   Cells.encode(cell);
		}
	}

	public static Path parsePath(String string) {
		Path path = new Path();
		for (int i = 0; i < string.length(); i += 3) {
			String part = string.substring(i, i + 3);
			Node node = Node.parseNode(part);
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
	
	public String toString() {
		StringBuilder sb = new StringBuilder(size() * 3);
		for (int i = 0; i < size(); ++i) {
			sb.append(get(i).toString());
		}
		return sb.toString();
	}
	
}
