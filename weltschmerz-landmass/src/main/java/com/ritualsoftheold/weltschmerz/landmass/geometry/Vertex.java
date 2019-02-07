package com.ritualsoftheold.weltschmerz.landmass.geometry;

import com.ritualsoftheold.weltschmerz.landmass.PrecisionMath;

public class Vertex extends Point implements Comparable<Vertex> {

    private Centroid centroid;

    public static final Vertex INFINITE =
            new Vertex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, true, null);

    public static final Vertex UNKNOWN = new Vertex(Double.NaN, Double.NaN, true, null);

    public Vertex(double x, double y, Centroid centroid) {
        this(x, y, false, centroid);
    }

    public Vertex(double x, double y, boolean allowOdd, Centroid centroid) {
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
        this.centroid = centroid;
    }

    public Centroid getCentroid() {
        return centroid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Vertex))
            return false;

        final Vertex o = (Vertex) obj;
        return PrecisionMath.eq(x, o.x) && PrecisionMath.eq(y, o.y);
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
