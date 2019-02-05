package com.ritualsoftheold.weltschmerz.landmass;

import com.ritualsoftheold.weltschmerz.landmass.geometry.Edge;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Point;

import java.util.HashSet;
import java.util.Iterator;

public class Graph {
    private HashSet<Edge> graphEdges;
    private HashSet<Point> graphVertices;

    public Graph(HashSet<Edge> e) {
        graphVertices = null;
        graphEdges = e;
    }

    public Iterator<Edge> getEdges() {
        return graphEdges.iterator();
    }

    public Edge[] getEdgeArray() {
        Edge[] vEdges = new Edge[graphEdges.size()];
        graphEdges.toArray(vEdges);
        return vEdges;
    }

    public Point[] getVertexArray() {
        if (graphVertices == null)
            listVertices();
        Point[] vVerts = new Point[graphVertices.size()];
        graphVertices.toArray(vVerts);
        return vVerts;
    }

    private void listVertices() {
        graphVertices = new HashSet<>();
        for (final Edge VE : graphEdges) {
            graphVertices.add(VE.getVertexA());
            graphVertices.add(VE.getVertexB());
        }
    }
}
