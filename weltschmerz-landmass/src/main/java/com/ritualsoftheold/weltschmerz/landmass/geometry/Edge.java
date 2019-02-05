package com.ritualsoftheold.weltschmerz.landmass.geometry;

public class Edge implements Comparable<Edge> {
    private final Point VVertexA;
    private final Point VVertexB;
    private final Point leftDatum;
    private final Point rightDatum;

    public Edge(Point a, Point b, Point ld, Point rd) {
        if (a.isNaN() || b.isNaN())
            throw new IllegalArgumentException("Undefined vertices" +
                    " not allowed in an Edge.");
        if (ld.isNaN() || rd.isNaN())
            throw new IllegalArgumentException("Undefined data points" +
                    " not allowed in an Edge.");
        if (ld.isInfinite() || rd.isInfinite())
            throw new IllegalArgumentException("Infinite data points" +
                    " not allowed in an Edge.");

        leftDatum = ld;
        rightDatum = rd;
        VVertexA = a;
        VVertexB = b;
    }

    public Point getVertexA() {
        return VVertexA;
    }

    public Point getVertexB() {
        return VVertexB;
    }

    public Point getDatumA() {
        return leftDatum;
    }

    public Point getDatumB() {
        return rightDatum;
    }

    public int compareTo(Edge ev) {
        Point me1, me2;
        if (VVertexA.compareTo(VVertexB) < 0) {
            me1 = VVertexA;
            me2 = VVertexB;
        } else {
            me1 = VVertexB;
            me2 = VVertexA;
        }

        Point o1, o2;
        if (ev.VVertexA.compareTo(ev.VVertexB) < 0) {
            o1 = ev.VVertexA;
            o2 = ev.VVertexB;
        } else {
            o1 = ev.VVertexB;
            o2 = ev.VVertexA;
        }

        int stat = me1.compareTo(o1);
        if (stat == 0)
            stat = me2.compareTo(o2);
        return stat;
    }

    @Override
    public int hashCode() {
        return VVertexA.hashCode() ^ VVertexB.hashCode();
    }
}