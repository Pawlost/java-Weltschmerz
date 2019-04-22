package com.ritualsoftheold.weltschmerz.landmass.fortune.geometry;

public class Centroid extends Point {

    public static final Centroid UNKNOWN = new Centroid(Double.NaN, Double.NaN, true);

    public Centroid(double x, double y) {
            this(x, y, false);
    }

    public Centroid(double x, double y, boolean allowOdd) {
        if (!allowOdd) {
            if (Double.isInfinite(x) || Double.isInfinite(y))
                throw new IllegalArgumentException("Infinite co-ordinates" +
                        " not allowed in a Centroid.");
            if (Double.isNaN(x) || Double.isNaN(y))
                throw new IllegalArgumentException("NaN co-ordinates" +
                        " not allowed in a Centroid.");
        }

        this.x = x;
        this.y = y;
    }

}
