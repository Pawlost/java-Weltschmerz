package com.ritualsoftheold.weltschmerz.landmass.geometry;

public class Border {
    private Vertex VVertexA;
    private Vertex VVertexB;
    private Centroid leftDatum;
    private Centroid rightDatum;

    public Border(Vertex a, Vertex b, Centroid ld, Centroid rd) {
        if (a.isNaN() || b.isNaN())
               throw new IllegalArgumentException("Undefined vertices" + " not allowed in an Edge.");
        if (ld.isNaN() || rd.isNaN())
            throw new IllegalArgumentException("Undefined data points" + " not allowed in an Edge.");
        if (ld.isInfinite() || rd.isInfinite())
            throw new IllegalArgumentException("Infinite data points" + " not allowed in an Edge.");

        leftDatum = ld;
        rightDatum = rd;
        VVertexA = a;
        VVertexB = b;
    }

    public Vertex getVertexA() {
        return VVertexA;
    }

    public Vertex getVertexB() {
        return VVertexB;
    }

    public Centroid getDatumA() {
        return leftDatum;
    }

    public Centroid getDatumB() {
        return rightDatum;
    }

    public void setVVertexB(Vertex VVertexB) {
        this.VVertexB = VVertexB;
    }

    public void setVVertexA(Vertex VVertexA) {
        this.VVertexA = VVertexA;
    }
}
