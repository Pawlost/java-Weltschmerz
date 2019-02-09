package com.ritualsoftheold.weltschmerz.landmass;

import com.ritualsoftheold.weltschmerz.landmass.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Vertex;

import java.util.ArrayList;

public class Location {

    private ArrayList<Border> borders;
    private ArrayList<Vertex> vertice;
    private Centroid centroid;

    public Location(double x, double y) {
        centroid = new Centroid(x, y);
        borders = new ArrayList<>();
        vertice = new ArrayList<>();
    }

    public void add(Border border) {
        this.borders.add(border);
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
        if (vertice.size() == 0)
            listVertices();
        Vertex[] points = new Vertex[vertice.size()];
        vertice.toArray(points);
        return points;
    }

    private void listVertices() {
        for (Border border : getBorders()) {
            vertice.add(border.getVertexA());
            vertice.add(border.getVertexB());
        }
    }

    public void reset() {

        double newX = (centroid.getX() + this.centerX())/2;
        double newY = (centroid.getY() + this.centerY())/2;

        centroid = new Centroid(newX, newY);
        borders.clear();
        vertice.clear();
    }

    private double centerX(){
        if (vertice.size() == 0)
            listVertices();

        double centreX = 0;

        for (Vertex vertex:vertice) {
            centreX += vertex.getX();
        }

        centreX /= vertice.size();
        return centreX;
    }

    private double centerY(){
        if (vertice.size() == 0)
            listVertices();

        double centreY = 0;

        for (Vertex vertex:vertice) {
            centreY += vertex.getY();
        }

        centreY /= vertice.size();
        return centreY;
    }
}
