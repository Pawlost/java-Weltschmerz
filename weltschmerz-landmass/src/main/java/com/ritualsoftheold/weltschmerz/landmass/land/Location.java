package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.landmass.Shape;

import java.util.ArrayList;

public class Location {

    private Plate tectonicPlate;
    private Shape shape;
    private boolean isLand;
    private long seed;
    private ArrayList<Location> neighbors;
    private Position position;

    public Location(int x, int z, long seed) {
        position = new Position(x, z, 1, 1);
        this.seed = seed;
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

    public Position getPosition(){
        return position;
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
