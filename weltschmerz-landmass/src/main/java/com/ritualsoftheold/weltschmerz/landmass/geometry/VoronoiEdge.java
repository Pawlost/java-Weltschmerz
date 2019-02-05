package com.ritualsoftheold.weltschmerz.landmass.geometry;

public class VoronoiEdge {

    public Point RightData = Point.UNKNOWN, LeftData = Point.UNKNOWN;
    public Point VVertexA = Point.UNKNOWN, VVertexB = Point.UNKNOWN;

    public boolean Done = false;

    public void AddVertex(Point V) {
        if (VVertexA == Point.UNKNOWN)
            VVertexA = V;
        else if (VVertexB == Point.UNKNOWN)
            VVertexB = V;
        else
            throw new RuntimeException("Tried to add third vertex!");
    }

   public boolean isPartlyInfinite() {
        return VVertexA != Point.INFINITE && VVertexB != Point.INFINITE;
    }

    public Edge toEdge() {
        return new Edge(VVertexA, VVertexB, LeftData, RightData);
    }
}
