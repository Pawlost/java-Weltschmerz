package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.landmass.Shape;

import java.util.ArrayList;

public class Location {

    private Plate tectonicPlate;
    private Shape shape;
    private boolean isLand;
    private long seed;

    private ArrayList<Double> elevation;
    ;
    private ArrayList<Location> neighbors;
    private Sector sector;

    public Location(int x, int y, long seed) {
        sector = new Sector(x, y, 1, 1);
        this.seed = seed;
        elevation = new ArrayList<>();
        neighbors = new ArrayList<>();
    }

    public Location[] getNeighbors() {
        Location[] copyNeighbors = new Location[neighbors.size()];
        neighbors.toArray(copyNeighbors);
        return copyNeighbors;
    }

    public void addNeighbor(Location neighbor){
        neighbors.add(neighbor);
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

    public Sector getSector(){
        return sector;
    }

    public void addElevation(double elevation){
        this.elevation.add(elevation);
    }

    public ArrayList<Double> getElevation() {
        return elevation;
    }

    public void setTectonicPlate(Plate tectonicPlate) {
        this.tectonicPlate = tectonicPlate;
    }

    public Plate getTectonicPlate() {
        return tectonicPlate;
    }

    public boolean isLand(){
        return isLand;
    }

    public void setLand(boolean land) {
        isLand = land;
    }

    public long getSeed() {
        return seed;
    }
}
