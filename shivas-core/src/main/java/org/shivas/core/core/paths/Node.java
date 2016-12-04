package org.shivas.core.core.paths;

import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.core.utils.Cells;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 20/10/12
 * Time: 19:03
 */
public class Node {
    public static Node parseNode(String string) {
        return new Node(
                Cells.decode(string.charAt(0)),
                Cells.decode(string.substring(1))
        );
    }

    public static Node parseNodeWithoutOrientation(String string) {
        return new Node(null, Cells.decode(string));
    }

    private OrientationEnum orientation;
    private short cell;

    public Node(OrientationEnum orientation, short cell) {
        this.orientation = orientation;
        this.cell = cell;
    }

    public static Node of(int cell, OrientationEnum orientation) {
        return new Node(orientation, (short) cell);
    }

    public OrientationEnum orientation() {
        return orientation;
    }

    public short cell() {
        return cell;
    }

    public String encode() {
        return Cells.encode(orientation) + Cells.encode(cell);
    }

    @Override
    public String toString() {
        return "Node(" + cell + " " + orientation.name() + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ScoredNode)) return false;

        Node node = (Node) o;

        return cell == node.cell && orientation == node.orientation;
    }

    @Override
    public int hashCode() {
        int result = orientation != null ? orientation.hashCode() : 0;
        result = 31 * result + (int) cell;
        return result;
    }
}
