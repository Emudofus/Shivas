package org.shivas.common.maths;

import java.io.Serializable;

public class Point implements Serializable {
	
	private static final long serialVersionUID = -7429256159668151330L;
	
	private int abscissa;
	private int ordinate;
	
	public Point() {
	}
	
	public Point(int abscissa, int ordinate) {
		this.abscissa = abscissa;
		this.ordinate = ordinate;
	}

	public int abscissa() {
		return abscissa;
	}

	public void setAbscissa(int abscissa) {
		this.abscissa = abscissa;
	}

	public int ordinate() {
		return ordinate;
	}

	public void setOrdinate(int ordinate) {
		this.ordinate = ordinate;
	}

    @Override
    public int hashCode() {
        return abscissa + ordinate;
    }

    @Override
    public boolean equals(Object that) {
        return this == that ||
               that != null &&
               this.getClass() == that.getClass() &&
               this.equals((Point) that);
    }

    public boolean equals(Point that) {
        return that == this ||
               that != null &&
               this.abscissa == that.abscissa && this.ordinate == that.ordinate;
    }

    @Override
    public String toString() {
        return String.format("(%d;%d)", abscissa, ordinate);
    }
}
