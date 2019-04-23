package com.ritualsoftheold.weltschmerz.landmass.markers;

public class Marker extends Point {

    public static final Marker UNKNOWN = new Marker(Double.NaN, Double.NaN, true);

    public Marker(double x, double y) {
            this(x, y, false);
    }

    public Marker(double x, double y, boolean allowOdd) {
        if (!allowOdd) {
            if (Double.isInfinite(x) || Double.isInfinite(y))
                throw new IllegalArgumentException("Infinite co-ordinates" +
                        " not allowed in a Marker.");
            if (Double.isNaN(x) || Double.isNaN(y))
                throw new IllegalArgumentException("NaN co-ordinates" +
                        " not allowed in a Marker.");
        }

        this.x = x;
        this.y = y;
    }

}
