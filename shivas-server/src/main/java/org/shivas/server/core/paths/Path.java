package org.shivas.server.core.paths;

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
	
	public Node first() {
		return get(0);
	}
	
	public Node last() {
		return get(size() - 1);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder(size() * 3);
        for (Node node : this) {
            sb.append(node.toString());
        }
		return sb.toString();
	}
	
}
