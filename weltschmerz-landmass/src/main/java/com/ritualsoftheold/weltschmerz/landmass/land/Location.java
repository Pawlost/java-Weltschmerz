package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.core.Generation;
import com.ritualsoftheold.weltschmerz.core.Legend;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Vertex;
import com.sudoplay.joise.module.ModuleAutoCorrect;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Location {

    private Plate tectonicPlate;
    private Centroid centroid;
    private Legend legend;
    private boolean isLand;

    private ArrayList<Border> borders;
    private ArrayList<Vertex> vertice;
    private ArrayList<Location> neighbors;
    private ArrayList<Centroid> nearCentroids;
    private java.awt.Polygon polygon;

    public Location(double x, double y) {
        centroid = new Centroid(x, y);

        borders = new ArrayList<>();
        vertice = new ArrayList<>();
        neighbors = new ArrayList<>();
        nearCentroids = new ArrayList<>();
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

    public Location[] getNeighbors() {
        if (neighbors.size() == 0)
            listVariables();
        Location[] copyNeighbors = new Location[neighbors.size()];
        neighbors.toArray(copyNeighbors);
        return copyNeighbors;
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

    public void makeNeighbors(ArrayList<Location> world) {
        for (Location next : world) {
            ArrayList<Centroid> centroids = next.getNearCentroids();
            if (centroids.contains(centroid)) {
                neighbors.add(next);
            }
        }
    }

    public java.awt.Polygon getPolygon() {
        return polygon;
    }

    public void listVariables() {
        vertice.clear();
        for (Border border : borders) {
            vertice.add(border.getVertexA());
            vertice.add(border.getVertexB());
        }

        if(borders.size() > 2) {
            Border newBorder = completePolygon();
            if (newBorder != null) {
                borders.add(newBorder);
            }

            nearCentroids.clear();
            for (Border border : borders) {
                Centroid neighbor = this.centroid == border.getDatumA() ? border.getDatumB() : border.getDatumA();
                if (neighbor != null) {
                    nearCentroids.add(neighbor);
                }
            }
        }
    }

    private Border completePolygon(){
        Vertex start = getVertice()[0];
        Vertex end = getVertice()[vertice.size() - 1];
        if(!end.equals(start)) {
            Border newBorder = new Border(end, start, centroid, null);
            for (Border border : borders) {
                if (border.getVertexA() == start && border.getVertexB() == end
                        || border.getVertexB() == start && border.getVertexA() == end) {
                    return null;
                }
            }
            return newBorder;
        }
        return null;
    }

    public void reset() {

        double newX = (centroid.getX() + centerX()) / 2;
        double newY = (centroid.getY() + centerY()) / 2;

        centroid = new Centroid(newX, newY);
        borders.clear();
        vertice.clear();
        neighbors.clear();
        nearCentroids.clear();
    }

    private double centerX() {
        if (vertice.size() == 0)
            listVariables();

        double center = 0;

        for (Vertex vertex : vertice) {
            center += vertex.getX();
        }

        center /= vertice.size();

        return center;
    }

    private double centerY() {
        if (vertice.size() == 0)
            listVariables();

        double center = 0;

        for (Vertex vertex : vertice) {
            center += vertex.getY();
        }

        center /= vertice.size();
        return center;
    }


    public void setLand(ModuleAutoCorrect mod, int detail){
        if(legend == null) {
            Rectangle boundries = polygon.getBounds();
            ArrayList<Double> elevation = new ArrayList<>();

            int width = boundries.width + boundries.x;
            int height = boundries.height + boundries.y;

            for (int x = boundries.x; x < width; x += detail) {
                for (int y = boundries.y; y < height; y += detail) {
                    Point point = new java.awt.Point(x, y);
                    if (polygon.contains(point)) {
                        double r = mod.get((double) x, (double) y);
                        elevation.add(r);
                    }
                }
            }

            legend = Generation.landGeneration(elevation);
            isLand = legend.land;
        }
    }

    public void setLegend(Legend legend) {
        this.legend = legend;
        this.isLand = legend.land;
    }

    public Legend getLegend() {
        return legend;
    }

    public void setTectonicPlate(Plate tectonicPlate) {
        this.tectonicPlate = tectonicPlate;
    }

    public Plate getTectonicPlate() {
        return tectonicPlate;
    }

    public void add(Border border) {
        this.borders.add(border);
    }

    public Centroid getCentroid() {
        return centroid;
    }

    public boolean isLand(){
        return isLand;
    }

    public ArrayList<Centroid> getNearCentroids() {
        return nearCentroids;
    }
}
