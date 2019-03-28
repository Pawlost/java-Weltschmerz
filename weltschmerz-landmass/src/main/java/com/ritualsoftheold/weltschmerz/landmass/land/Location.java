package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.core.Generation;
import com.ritualsoftheold.weltschmerz.core.Shape;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Vertex;
import com.sudoplay.joise.module.ModuleAutoCorrect;

import java.awt.*;
import java.util.ArrayList;

public class Location extends Area{

    private Plate tectonicPlate;
    private Centroid centroid;
    private Shape shape;
    private boolean isLand;

    public Location(double x, double y) {
        super();
        centroid = new Centroid(x, y);
    }

    @Override
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

            neighbors.clear();
            for (Border border : borders) {
                Centroid neighbor = this.centroid == border.getDatumA() ? border.getDatumB() : border.getDatumA();
                if (neighbor != null) {
                    neighbors.add(neighbor);
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
        polygon = null;
        borders.clear();
        vertice.clear();
        neighbors.clear();
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
        if(shape == null) {
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

            shape = Generation.landGeneration(elevation);
            isLand = shape.island;
        }
    }

    public void setShape(Shape shape) {
        this.shape = shape;
        this.isLand = shape.island;
    }

    public Shape getShape() {
        return shape;
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
}
