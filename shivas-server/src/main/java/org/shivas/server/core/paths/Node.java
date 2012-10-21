package org.shivas.server.core.paths;

import org.shivas.protocol.client.enums.OrientationEnum;
import org.shivas.server.utils.Cells;

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
