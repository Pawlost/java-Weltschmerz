package com.ritualsoftheold.weltschmerz.landmass.land;


import com.ritualsoftheold.weltschmerz.landmass.markers.Marker;
import com.ritualsoftheold.weltschmerz.noise.Shape;
import com.ritualsoftheold.weltschmerz.noise.WeltschmerzNoise;

import java.awt.*;
import java.util.ArrayList;

public class Location {

    private Plate tectonicPlate;
    private Marker marker;
    private Shape shape;
    private boolean isLand;

    private ArrayList<Double> elevation;
    private ArrayList<Location> neighbors;
    private ArrayList<Marker> nearMarkers;
    private Rectangle chunk;

    public Location(double x, double y, double size) {
        marker = new Marker(x, y);

        chunk = new Rectangle((int)x, (int)y, (int) size, (int)size);
        elevation = new ArrayList<>();
        neighbors = new ArrayList<>();
        nearMarkers = new ArrayList<>();
    }

    public Location[] getNeighbors() {
        Location[] copyNeighbors = new Location[neighbors.size()];
        neighbors.toArray(copyNeighbors);
        return copyNeighbors;
    }

    public void addNeighbor(Location neighbor){
        neighbors.add(neighbor);
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
