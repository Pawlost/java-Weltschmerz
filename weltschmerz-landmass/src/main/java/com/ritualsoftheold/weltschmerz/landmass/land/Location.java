package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.geometry.misc.Shape;
import com.ritualsoftheold.weltschmerz.geometry.units.Polygon;
import com.ritualsoftheold.weltschmerz.noise.generators.WorldNoise;


import java.util.ArrayList;

public class Location {

    public final long id;
    private double centerChunkElevation;
    private Plate tectonicPlate;
    private Shape shape;
    private ArrayList<Location> neighbors;
    public final Polygon position;
    public static final float VOLATILITY = 2;

    public Location(Point point, long id) {
        this(new Polygon(point, null), id);
    }

    public Location(Polygon polygon, long id) {
        this.position = polygon;
        this.id = id;
        neighbors = new ArrayList<>();
    }

    public ArrayList<Location> getNeighbors() {
        return neighbors;
    }

    public void add(Location neighbor) {
        this.neighbors.add(neighbor);
    }

    public boolean setShape(Shape shape) {
        this.shape = shape;
        centerChunkElevation = shape.position * WorldNoise.MAX_SECTOR_HEIGHT_DIFFERENCE;
        return shape.key.equals("MOUNTAIN") || shape.key.equals("OCEAN");
    }

    public void setElevation() {
        for (Location neighbor : neighbors) {
            double dist = position.center.dist(neighbor.position.center);
            centerChunkElevation += (neighbor.centerChunkElevation * ((dist * VOLATILITY) - dist)) / (dist * VOLATILITY);
        }
        centerChunkElevation = centerChunkElevation/(neighbors.size()-1);
    }

    public Shape getShape() {
        return shape;
    }

    public String getName() {
        return shape.key;
    }

    public void setTectonicPlate(Plate tectonicPlate) {
        this.tectonicPlate = tectonicPlate;
    }

    public Plate getTectonicPlate() {
        return tectonicPlate;
    }
}