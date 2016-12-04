package org.shivas.core.core.paths;

import org.shivas.protocol.client.enums.OrientationEnum;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 21/10/12
 * Time: 12:07
 */
public class ScoredNode extends Node implements Comparable<ScoredNode> {
    private double distanceToStart, distanceToEnd;

    public ScoredNode(OrientationEnum orientation, short cell) {
        super(orientation, cell);
    }

    public double distanceToStart() {
        return distanceToStart;
    }

    public void setDistanceToStart(double distanceToStart) {
        this.distanceToStart = distanceToStart;
    }

    public double distanceToEnd() {
        return distanceToEnd;
    }

    public void setDistanceToEnd(double distanceToEnd) {
        this.distanceToEnd = distanceToEnd;
    }

    public double sum() {
        return distanceToStart + distanceToEnd;
    }

    @Override
    public int compareTo(ScoredNode o) {
        return Double.compare(this.distanceToEnd, o.distanceToEnd);
    }
}
