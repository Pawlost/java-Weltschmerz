package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.core.Generation;
import com.ritualsoftheold.weltschmerz.core.Shape;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Vertex;
import com.sudoplay.joise.module.ModuleAutoCorrect;

import java.awt.*;
import java.util.ArrayList;

public class Location extends Area{

    private Shape shape;
    private boolean isLand;

    public Location(double x, double y) {
        super(x, y);
    }

    public void reset() {

        double newX = (centroid.getX() + super.centerX()) / 2;
        double newY = (centroid.getY() + super.centerY()) / 2;

        centroid = new Centroid(newX, newY);
        polygon = null;
        borders.clear();
        vertice.clear();
        neighbors.clear();
    }


    public void setLand(ModuleAutoCorrect mod, int detail){
        polygon = new Polygon();
        for (Vertex vertex : getVertice()) {
            polygon.addPoint((int) vertex.getX(), (int) vertex.getY());
        }

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

    public boolean isLand(){
        return isLand;
    }
}
