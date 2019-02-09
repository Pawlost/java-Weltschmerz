package com.ritualsoftheold.weltschmerz.landmass.geometry;

public class VoronoiBorder {

    public Centroid RightData = Centroid.UNKNOWN, LeftData = Centroid.UNKNOWN;
    public Vertex VVertexA = Vertex.UNKNOWN, VVertexB = Vertex.UNKNOWN;

    public boolean Done = false;

    public void AddVertex(Vertex V) {
        if (VVertexA == Vertex.UNKNOWN)
            VVertexA = V;
        else if (VVertexB == Vertex.UNKNOWN)
            VVertexB = V;
        else
            throw new RuntimeException("Tried to add third Location!");
    }

    public boolean isPartlyInfinite() {
        return VVertexA != Vertex.INFINITE && VVertexB != Vertex.INFINITE;
    }

    public Border toEdge() {
        return new Border(VVertexA, VVertexB, LeftData, RightData);
    }
}
