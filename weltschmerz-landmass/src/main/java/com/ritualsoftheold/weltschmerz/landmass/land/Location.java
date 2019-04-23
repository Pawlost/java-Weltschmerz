package com.ritualsoftheold.weltschmerz.landmass.land;


import com.ritualsoftheold.weltschmerz.landmass.markers.Border;
import com.ritualsoftheold.weltschmerz.landmass.markers.Marker;
import com.ritualsoftheold.weltschmerz.landmass.markers.Vertex;
import com.ritualsoftheold.weltschmerz.noise.Shape;
import com.ritualsoftheold.weltschmerz.noise.WeltschmerzNoise;

import java.awt.*;
import java.util.ArrayList;

public class Location {

    private Plate tectonicPlate;
    private Marker marker;
    private Shape shape;
    private boolean isLand;

    private ArrayList<Border> borders;
    private ArrayList<Double> elevation;
    private ArrayList<Vertex> vertice;
    private ArrayList<Location> neighbors;
    private ArrayList<Marker> nearMarkers;
    private Rectangle chunk;

    public Location(double x, double y, double size) {
        marker = new Marker(x, y);

        chunk = new Rectangle((int)x, (int)y, (int) size, (int)size);
        elevation = new ArrayList<>();
        borders = new ArrayList<>();
        vertice = new ArrayList<>();
        neighbors = new ArrayList<>();
        nearMarkers = new ArrayList<>();
    }

    public Border[] getBorders() {
        Border[] cloneBorders = new Border[borders.size()];
        borders.toArray(cloneBorders);
        return cloneBorders;
    }

    private Vertex[] getVertice() {
        if (vertice.size() == 0) {
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

    public void addNeighbor(Location neighbor){
        neighbors.add(neighbor);
    }

    private void listVariables() {
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

            nearMarkers.clear();
            for (Border border : borders) {
                Marker neighbor = this.marker == border.getDatumA() ? border.getDatumB() : border.getDatumA();
                if (neighbor != null) {
                    nearMarkers.add(neighbor);
                }
            }
        }
    }

    private Border completePolygon(){
        Vertex start = getVertice()[0];
        Vertex end = getVertice()[vertice.size() - 1];
        if(!end.equals(start)) {
            Border newBorder = new Border(end, start, marker, null);
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

        double newX = (marker.getX() + centerX()) / 2;
        double newY = (marker.getY() + centerY()) / 2;

        marker = new Marker(newX, newY);
        elevation.clear();
        borders.clear();
        vertice.clear();
        neighbors.clear();
        nearMarkers.clear();
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

    public void makeLand(WeltschmerzNoise noise, int spacing){
        if(shape == null) {
            int width = chunk.width + chunk.x;
            int height = chunk.height + chunk.y;

            for (int x = chunk.x; x < width; x += spacing) {
                for (int y = chunk.y; y < height; y += spacing) {
                    Point point = new java.awt.Point(x, y);
                    if (chunk.contains(point)) {
                        elevation.add(noise.getNoise(x, y));
                    }
                }
            }

            shape = noise.landGeneration(elevation);
            isLand = shape.land;
        }
    }

    public void setShape(Shape shape) {
        this.shape = shape;
        this.isLand = shape.land;
    }

    public Shape getShape() {
        return shape;
    }

    public String getKey() {
        return shape.key;
    }

    public Rectangle getChunk(){
        return chunk;
    }

    public void setTectonicPlate(Plate tectonicPlate) {
        this.tectonicPlate = tectonicPlate;
    }

    public Plate getTectonicPlate() {
        return tectonicPlate;
    }

    public Marker getMarker() {
        return marker;
    }

    public boolean isLand(){
        return isLand;
    }

    public void setLand(boolean land) {
        isLand = land;
    }
}
