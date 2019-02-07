package com.ritualsoftheold.weltschmerz.landmass;

import com.ritualsoftheold.weltschmerz.landmass.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Vertex;

import java.util.HashSet;
import java.util.Iterator;

public class Graph {
    private HashSet<Border> graphEdges;
    private HashSet<Vertex> graphVertices;

    public Graph(HashSet<Border> e) {
        graphVertices = null;
        graphEdges = e;
    }

    public Border[] getBorderArray() {
        Border[] vEdges = new Border[graphEdges.size()];
        graphEdges.toArray(vEdges);
        return vEdges;
    }

    public Vertex[] getVertexArray() {
        if (graphVertices == null)
            listVertices();
        Vertex[] vVerts = new Vertex[graphVertices.size()];
        graphVertices.toArray(vVerts);
        return vVerts;
    }

    private void listVertices() {
        graphVertices = new HashSet<>();
        for  (Border VE : graphEdges) {
            graphVertices.add(VE.getVertexA());
            graphVertices.add(VE.getVertexB());
        }
    }

    public void getVoronoiArea(Area[] areas){
        for(Area area: areas) {
            for (Vertex vertex : getVertexArray()) {
                if (vertex.getCentroid() == area.getCentroid()){
                    area.add(vertex);
                }
            }

            for (Border border : getBorderArray()) {
                if (border.getDatumA() == area.getCentroid() || border.getDatumB() == area.getCentroid()){
                    area.add(border);
                }
            }
        }
    }
}
