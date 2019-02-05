package com.ritualsoftheold.weltschmerz.landmass.geometry;

import com.ritualsoftheold.weltschmerz.landmass.PrecisionMath;

public class Point implements Comparable<Point> {

    public static final Point INFINITE =
            new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true);

    public static final Point UNKNOWN = new Point(Double.NaN, Double.NaN, true);

    public Point(double x, double y) {
        this(x, y, false);
    }

    private Point(double x, double y, boolean allowOdd) {
        if (!allowOdd) {
            if (Double.isInfinite(x) || Double.isInfinite(y))
                throw new IllegalArgumentException("Infinite co-ordinates" +
                        " not allowed in a Point.");
            if (Double.isNaN(x) || Double.isNaN(y))
                throw new IllegalArgumentException("NaN co-ordinates" +
                        " not allowed in a Point.");
        }

        this.x = x;
        this.y = y;
    }

    public boolean isInfinite() {
        return Double.isInfinite(x) || Double.isInfinite(y);
    }

    public boolean isNaN() {
        return Double.isNaN(x) || Double.isNaN(y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double dist(Point o) {
        final double dx = x - o.x;
        final double dy = y - o.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Point))
            return false;

        final Point o = (Point) obj;
        return PrecisionMath.eq(x, o.x) && PrecisionMath.eq(y, o.y);
    }

    public int compareTo(Point o) {
        if (!PrecisionMath.eq(y, o.y)) {
            if (y < o.y)
                return -1;
            else if (y > o.y)
                return 1;
        } else if (!PrecisionMath.eq(x, o.x)) {
            if (x < o.x)
                return -1;
            else if (x > o.x)
                return 1;
        }
        return 0;
    }

    @Override
    public int hashCode() {
        long xb = Double.doubleToLongBits(PrecisionMath.round(x));
        long yb = Double.doubleToLongBits(PrecisionMath.round(y));

        return (int) xb ^ (int) (xb >> 32) ^
                (int) (yb >> 53) ^ (int) (yb >> 21) ^ (int) (yb << 11);
    }

    @Override
    public String toString() {
        return "<" + x + "," + y + ">";
    }

    private final double x;
    private final double y;
}
