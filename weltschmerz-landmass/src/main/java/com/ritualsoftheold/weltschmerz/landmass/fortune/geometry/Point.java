package com.ritualsoftheold.weltschmerz.landmass.fortune.geometry;

public class Point {
    public final double x;
    public final double y;

    public Point (double x, double y){
        this.x = x;
        this.y = y;
    }

    public boolean isNaN() {
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
        return new Point(x, y);
    }
}
