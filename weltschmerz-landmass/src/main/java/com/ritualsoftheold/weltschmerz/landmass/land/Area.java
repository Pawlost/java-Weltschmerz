package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Vertex;

import java.util.ArrayList;

public abstract class Area {

    protected ArrayList<Border> borders;
    protected ArrayList<Vertex> vertice;
    protected ArrayList<Centroid> neighbors;
    protected java.awt.Polygon polygon;

    public Area() {
        borders = new ArrayList<>();
        vertice = new ArrayList<>();
        neighbors = new ArrayList<>();
    }

    public Border[] getBorders() {
        Border[] cloneBorders = new Border[borders.size()];
        borders.toArray(cloneBorders);
        return cloneBorders;
    }

    public Vertex[] getVertice() {
        if (vertice.size() == 0){
            listVariables();
        }
        Vertex[] points = new Vertex[vertice.size()];
        vertice.toArray(points);
        return points;
    }

    public Centroid[] getNeighbors() {
        if (neighbors.size() == 0)
            listVariables();
        Centroid[] center = new Centroid[neighbors.size()];
        neighbors.toArray(center);
        return center;
    }

    public void circularize() {
        if(borders.size() > 2) {

            ArrayList<Border> cloneBorders = new ArrayList<>();
            cloneBorders.add(borders.get(0));

            while (checkNext(cloneBorders));

            if (borders.size() == cloneBorders.size()) {
                borders = cloneBorders;

                polygon = new java.awt.Polygon();
                for (Vertex vertex : getVertice()) {
                    polygon.addPoint((int) vertex.getX(), (int) vertex.getY());
                }

                listVariables();
            } else {
                borders = new ArrayList<>();
            }
        }
    }

    protected void reset(){}
    protected void listVariables(){}

    //Method for circularization
    public boolean checkNext(ArrayList<Border> cloneBorders) {
        for (int i = 0; i < borders.size(); i++) {
            Border next = borders.get(i);
            if (!cloneBorders.contains(next)) {
                Border border = cloneBorders.get(cloneBorders.size() - 1);
                if (border.getVertexB().equals(next.getVertexA())) {
                    cloneBorders.add(next);
                    return true;
                } else if (border.getVertexB().equals(next.getVertexB())) {
                    Border newBorder = new Border(next.getVertexB(), next.getVertexA(), next.getDatumA(), next.getDatumB());
                    cloneBorders.add(newBorder);
                    borders.set(i, newBorder);
                    return true;
                }
            }
        }

        Border first = cloneBorders.get(0);
        Border last = cloneBorders.get(cloneBorders.size() - 1);

        if (first.getVertexA() ==last.getVertexB() && borders.size() != cloneBorders.size()) {
            for(Border border:getBorders()){
                if(!isBorderInside(border, cloneBorders)){
                    borders.remove(border);
                }
            }
        }

        return false;
    }

    private boolean isBorderInside(Border border, ArrayList<Border> cloneBorders){
        for(Border clone:cloneBorders) {
            if(clone.equals(border)){
                return true;
            }
        }
        return false;
    }

    public java.awt.Polygon getPolygon() {
        return polygon;
    }
}
