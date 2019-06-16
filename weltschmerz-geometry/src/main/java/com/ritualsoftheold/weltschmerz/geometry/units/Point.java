package com.ritualsoftheold.weltschmerz.geometry.units;

public class Point {
    static final Point UNKNOWN = new Point(Double.NaN, Double.NaN);

    public final double x;
    public final double y;

    public Point (double x, double y){
        this.x = x;
        this.y = y;
    }

    boolean isNaN() {
        return Double.isNaN(x) || Double.isNaN(y);
    }

    public boolean isInfinite() {
        return Double.isInfinite(x) || Double.isInfinite(y);
    }

    public double dist(Vertex o) {
        final double dx = x - o.x;
        final double dy = y - o.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Point clone() throws CloneNotSupportedException {
        return (Point) super.clone();
    }
}
