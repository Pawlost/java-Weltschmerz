package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Vertex;

import java.awt.*;
import java.util.ArrayList;

public abstract class Area {

    protected ArrayList<Border> borders;
    protected ArrayList<Vertex> vertice;
    protected ArrayList<Centroid> neighbors;
    protected Polygon polygon;
    protected Centroid centroid;

    public Area(double x, double y) {
        centroid = new Centroid(x, y);
        borders = new ArrayList<>();
        vertice = new ArrayList<>();
        neighbors = new ArrayList<>();
    }

    public Border[] getBorders() {
        Border[] edges = new Border[borders.size()];
        borders.toArray(edges);
        return edges;
    }

    public Vertex[] getVertice() {
        if (vertice.size() == 0)
            listVariables();
        Vertex[] points = new Vertex[vertice.size()];
        vertice.toArray(points);
        return points;
    }

    public Centroid[] getNeighbors() {
        if (neighbors.size() == 0)
            listVariables();
        Centroid[] center = new Centroid[vertice.size()];
        neighbors.toArray(center);
        return center;
    }

    protected double centerX() {
        if (vertice.size() == 0)
            listVariables();

        double center = 0;

        for (Vertex vertex : vertice) {
            center += vertex.getX();
        }

        center /= vertice.size();

        return center;
    }

    protected double centerY() {
        if (vertice.size() == 0)
            listVariables();

        double center = 0;

        for (Vertex vertex : vertice) {
            center += vertex.getY();
        }

        center /= vertice.size();
        return center;
    }

    private void listVariables() {
        for (Border border : borders) {
            vertice.add(border.getVertexA());
            vertice.add(border.getVertexB());

            Centroid neighbor = this.centroid == border.getDatumA()? border.getDatumB(): border.getDatumA();
            if(neighbor != null) {
                neighbors.add(neighbor);
            }
        }
    }

    public void circularize() {
        ArrayList<Border> cloneBorders = new ArrayList<>();
        cloneBorders.add(borders.get(0));

        while (checkNext(cloneBorders)) ;

        if (borders.size() == cloneBorders.size()) {
            borders = cloneBorders;
        } else {
            borders = new ArrayList<>();
        }
    }

    private boolean checkNext(ArrayList<Border> cloneBorders) {
        for (int i = 0; i < borders.size(); i++) {
            Border next = borders.get(i);
            if (!cloneBorders.contains(next)) {
                Border border = cloneBorders.get(cloneBorders.size() - 1);
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

    public Polygon getPolygon() {
        return polygon;
    }

    public void add(Border border) {
        this.borders.add(border);
    }

    public Centroid getCentroid() {
        return centroid;
    }

}
