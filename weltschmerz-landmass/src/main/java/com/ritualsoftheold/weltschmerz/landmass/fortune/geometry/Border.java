package com.ritualsoftheold.weltschmerz.landmass.fortune.geometry;

public class Border{
    private Vertex VertexA;
    private Vertex VertexB;
    private Centroid leftDatum;
    private Centroid rightDatum;

    public Border(Vertex a, Vertex b, Centroid ld, Centroid rd) {
        if (a.isNaN() || b.isNaN())
               throw new IllegalArgumentException("Undefined vertices" + " not allowed in an Edge.");

        leftDatum = ld;
        rightDatum = rd;
        VertexA = a;
        VertexB = b;
    }

    public Vertex getVertexA() {
        return VertexA;
    }

    public Vertex getVertexB() {
        return VertexB;
    }

    public Centroid getDatumA() {
        return leftDatum;
    }

    public Centroid getDatumB() {
        return rightDatum;
    }

    public void setDatumA(Centroid leftDatum) {
        this.leftDatum = leftDatum;
    }

    public void setDatumB(Centroid rightDatum) {
        this.rightDatum = rightDatum;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Border))
            return false;

        Border o = (Border) obj;

        if(VertexA.equals(o.VertexA) && VertexB.equals(o.VertexB)){
            return true;
        }else return VertexA.equals(o.VertexB) && VertexB.equals(o.VertexA);
    }
}
