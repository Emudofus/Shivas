package org.shivas.server.core.paths;

import org.shivas.protocol.client.enums.OrientationEnum;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 21/10/12
 * Time: 12:07
 */
public class ScoredNode extends Node implements Comparable<ScoredNode> {
    private int distanceToStart, distanceToEnd;

    public ScoredNode(OrientationEnum orientation, short cell) {
        super(orientation, cell);
    }

    public int distanceToStart() {
        return distanceToStart;
    }

    public void setDistanceToStart(int distanceToStart) {
        this.distanceToStart = distanceToStart;
    }

    public int distanceToEnd() {
        return distanceToEnd;
    }

    public void setDistanceToEnd(int distanceToEnd) {
        this.distanceToEnd = distanceToEnd;
    }

    public int sum() {
        return distanceToStart + distanceToEnd;
    }

    @Override
    public int compareTo(ScoredNode o) {
        return this.distanceToEnd - o.distanceToEnd;
    }
}
