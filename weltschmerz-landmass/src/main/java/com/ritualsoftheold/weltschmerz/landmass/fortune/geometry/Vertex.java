package com.ritualsoftheold.weltschmerz.landmass.fortune.geometry;

import com.ritualsoftheold.weltschmerz.landmass.PrecisionMath;

public class Vertex extends Point implements Comparable<Vertex> {

    public static final Vertex INFINITE =
            new Vertex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true);

    public static final Vertex UNKNOWN = new Vertex(Double.NaN, Double.NaN, true);

    public Vertex(double x, double y) {
        this(x, y, false);
    }

    public Vertex(double x, double y, boolean allowOdd) {
        if (!allowOdd) {
            if (Double.isInfinite(x) || Double.isInfinite(y))
                throw new IllegalArgumentException("Infinite co-ordinates" +
                        " not allowed in a Vertex.");
            if (Double.isNaN(x) || Double.isNaN(y))
                throw new IllegalArgumentException("NaN co-ordinates" +
                        " not allowed in a Vertex.");
        }

        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vertex))
            return false;

        final Vertex o = (Vertex) obj;
        return PrecisionMath.eq(x, o.x) && PrecisionMath.eq(y, o.y);
    }

    public Vertex clone(){
        return new Vertex(x, y);
    }

    public int compareTo(Vertex o) {
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
}
