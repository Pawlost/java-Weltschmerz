package com.ritualsoftheold.weltschmerz.geometry.units;

public class Border{
    public final Vertex vertexA;
    public final Vertex vertexB;
    public final Point datumA;
    public final Point datumB;

    public Border(Vertex a, Vertex b, Point ld, Point rd) {
        if (a.isNaN() || b.isNaN())
               throw new IllegalArgumentException("Undefined vertices" + " not allowed in an Edge.");

        double x = a.x - b.x;
        double y = a.y - b.y;
        datumA = ld;
        datumB = rd;
        vertexA = a;
        vertexB = b;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Border))
            return false;

        Border o = (Border) obj;

        if(vertexA.equals(o.vertexA) && vertexB.equals(o.vertexB)){
            return true;
        }else return vertexA.equals(o.vertexB) && vertexB.equals(o.vertexA);
    }
}
