package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Vertex;

import java.util.ArrayList;

public abstract class Area implements Polygon {

    protected ArrayList<Border> borders;
    protected ArrayList<Vertex> vertice;
    protected ArrayList<Centroid> neighbors;
    protected java.awt.Polygon polygon;

    public Area() {
        borders = new ArrayList<>();
        vertice = new ArrayList<>();
        neighbors = new ArrayList<>();
    }

    public ArrayList<Border> getBorders() {
        if(borders.size() == 0)
            listVariables();
        return borders;
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

    public void circularize() {
        if(borders.size() > 2) {
            ArrayList<Border> cloneBorders = new ArrayList<>();
            cloneBorders.add(borders.get(0));

            while (checkNext(cloneBorders)) ;

            if (borders.size() == cloneBorders.size()) {
                borders = cloneBorders;
            } else {
                borders = new ArrayList<>();
            }
        }
    }

    //Method for circularization
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

    public java.awt.Polygon getPolygon() {
        return polygon;
    }
}
