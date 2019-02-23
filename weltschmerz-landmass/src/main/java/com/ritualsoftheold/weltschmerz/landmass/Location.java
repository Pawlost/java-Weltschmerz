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
        for (Border border : borders) {
            vertice.add(border.getVertexA());
            vertice.add(border.getVertexB());
        }
    }

    public void reset() {

        double newX = (centroid.getX() + this.centerX()) / 2;
        double newY = (centroid.getY() + this.centerY()) / 2;

        centroid = new Centroid(newX, newY);
        borders.clear();
        vertice.clear();
    }

    private double centerX() {
        if (vertice.size() == 0)
            listVertices();

        double center = 0;

        for (Vertex vertex : vertice) {
            center += vertex.getX();
        }

        center /= vertice.size();
        return center;
    }

    private double centerY() {
        if (vertice.size() == 0)
            listVertices();

        double center = 0;

        for (Vertex vertex : vertice) {
            center += vertex.getY();
        }

        center /= vertice.size();
        return center;
    }

    public void circularize() {
        ArrayList<Border> cloneBorders = new ArrayList<>();
        cloneBorders.add(borders.get(0));

        while (checkNext(cloneBorders));

        if(borders.size() == cloneBorders.size()) {
            borders = cloneBorders;
        }else{
            borders = new ArrayList<>();
        }
    }

    private boolean checkNext(ArrayList<Border> cloneBorders){
        for(int i = 0; i < borders.size(); i++){
            Border next = borders.get(i);
            if(!cloneBorders.contains(next)) {
                Border border = cloneBorders.get(cloneBorders.size() -1);
                if (border.getVertexB() == next.getVertexA()) {
                    cloneBorders.add(next);
                    return true;
                } else if (border.getVertexB() == next.getVertexB()) {
                    Border newBorder = new Border(next.getVertexB(), next.getVertexA(), next.getDatumA(), next.getDatumB());
                    cloneBorders.add(newBorder);
                    borders.set(i, newBorder);
                    return true;
                }
            }
        }
        return false;
    }
}
