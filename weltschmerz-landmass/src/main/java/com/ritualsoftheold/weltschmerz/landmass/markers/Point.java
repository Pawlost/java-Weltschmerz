package com.ritualsoftheold.weltschmerz.landmass.markers;

public abstract class Point {
    double x;
    double y;

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

    public double dist(Marker o) {
        final double dx = x - o.x;
        final double dy = y - o.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Point clone() throws CloneNotSupportedException {
        return  (Point) super.clone();
    }
}
