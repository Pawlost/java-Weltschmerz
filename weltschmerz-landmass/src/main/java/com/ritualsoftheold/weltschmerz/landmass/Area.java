package com.ritualsoftheold.weltschmerz.landmass;

import com.ritualsoftheold.weltschmerz.landmass.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Vertex;

import java.util.ArrayList;

public class Area{

    private ArrayList<Border> borders;
    private ArrayList<Vertex> vertice;
    private Centroid centroid;

    public Area(double x, double y) {
        centroid = new Centroid(x, y);
        borders = new ArrayList<>();
        vertice = new ArrayList<>();
    }

    public void add(Border border){
        this.borders.add(border);
    }

    public void add(Vertex vertex){
        this.vertice.add(vertex);
    }

    public Centroid getCentroid() {
        return centroid;
    }

    public Border[] getBorders() {
        Border[] edges = new Border[borders.size()];
        borders.toArray(edges);
        return edges;
    }

    public Vertex[] getVertice() {
        if(vertice.size() > 0)
            listVertices();
        Vertex[] points = new Vertex[borders.size()];
        vertice.toArray(points);
        return points;
    }

    private void listVertices() {

        for (Border VE : getBorders()) {
            vertice.add(VE.getVertexA());
            vertice.add(VE.getVertexB());
        }
    }
}
