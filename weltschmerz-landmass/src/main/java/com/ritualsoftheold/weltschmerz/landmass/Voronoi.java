package com.ritualsoftheold.weltschmerz.landmass;

import com.ritualsoftheold.weltschmerz.landmass.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Vertex;

import java.util.HashSet;

public class Voronoi {
    private HashSet<Border> voronoiBorders;
    private HashSet<Vertex> voroniVertices;

    public Voronoi(HashSet<Border> e) {
        voroniVertices = null;
        voronoiBorders = e;
        listVertices();
    }

    public Border[] getBorderArray() {
        Border[] vEdges = new Border[voronoiBorders.size()];
        voronoiBorders.toArray(vEdges);
        return vEdges;
    }

    public Vertex[] getVertexArray() {
        Vertex[] vVerts = new Vertex[voroniVertices.size()];
        voroniVertices.toArray(vVerts);
        return vVerts;
    }

    private void listVertices() {
        voroniVertices = new HashSet<>();
        for (Border VE : voronoiBorders) {
            voroniVertices.add(VE.getVertexA());
            voroniVertices.add(VE.getVertexB());
        }
    }

    public void getVoronoiArea(Location[] locations){
        for(Location location : locations) {
            for (Border border : getBorderArray()) {
                if (border.getDatumA() == location.getCentroid() || border.getDatumB() == location.getCentroid()){
                    location.add(border);
                }
            }
        }
    }
}
