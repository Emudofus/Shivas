package org.shivas.common.maths;

/**
 * Created with IntelliJ IDEA.
 * User: Blackrush
 * Date: 02/11/12
 * Time: 17:59
 */
public class Vector {
    public static final Vector NIL = new Vector(0, 0);

    public static Vector fromPoints(Point a, Point b) {
        return create(b.abscissa() - a.abscissa(), b.ordinate() - a.ordinate());
    }

    public static Vector create(int x, int y) {
        if (x == 0 && y == 0) return NIL;
        return new Vector(x, y);
    }

    private int x, y;

    private Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Point apply(Point point) {
        return new Point(
                point.abscissa() + x,
                point.ordinate() + y
        );
    }

    public boolean isCollinearWith(Vector that) {
        if (that == null) throw new IllegalArgumentException("must not be null");
        return this == NIL || that == NIL || (this.x * that.y) == (this.y * that.x);
    }

    public Double getCoefficient(Vector that) {
        if (!isCollinearWith(that)) return null;
        return (double)this.x / (double)that.x;
    }

    public boolean hasSameDirectionOf(Vector that) {
        return this.getCoefficient(that) > 0.0;
    }

    @Override
    public boolean equals(Object that) {
        return this.getClass() == that.getClass() && equals((Vector) that);

    }

    public boolean equals(Vector that) {
        return that != null && (that == this || this.x == that.x && this.y == that.y);
    }

    @Override
    public int hashCode() {
        return x + y;
    }

    @Override
    public String toString() {
        return String.format("(%d;%d)", x, y);
    }
}
