package com.ritualsoftheold.weltschmerz.landmass.markers;

public class Border{
    private Vertex VertexA;
    private Vertex VertexB;
    private Marker leftDatum;
    private Marker rightDatum;

    public Border(Vertex a, Vertex b, Marker ld, Marker rd) {
        if (a.isNaN() || b.isNaN())
               throw new IllegalArgumentException("Undefined vertices" + " not allowed in an Edge.");

        leftDatum = ld;
        rightDatum = rd;
        VertexA = a;
        VertexB = b;
    }

    public void setVertexA(Vertex vertexA) {
        VertexA = vertexA;
    }

    public void setVertexB(Vertex vertexB) {
        VertexB = vertexB;
    }

    public Vertex getVertexA() {
        return VertexA;
    }

    public Vertex getVertexB() {
        return VertexB;
    }

    public Marker getDatumA() {
        return leftDatum;
    }

    public Marker getDatumB() {
        return rightDatum;
    }

    public void setDatumA(Marker leftDatum) {
        this.leftDatum = leftDatum;
    }

    public void setDatumB(Marker rightDatum) {
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

    @Override
    public Border clone() {
        return new Border(VertexA, VertexB, null, null);
    }
}
