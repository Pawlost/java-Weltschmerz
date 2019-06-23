package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.geometry.units.Border;
import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.geometry.misc.Shape;
import com.ritualsoftheold.weltschmerz.geometry.units.Polygon;
import com.ritualsoftheold.weltschmerz.geometry.units.Vector;
import com.ritualsoftheold.weltschmerz.noise.generators.ChunkNoise;
import com.ritualsoftheold.weltschmerz.noise.generators.WorldNoise;


import java.util.ArrayList;

public class Location {

    public final long seed;
    private double centerChunkElevation;
    private Plate tectonicPlate;
    private Shape shape;
    private ArrayList<Location> neighbors;
    public final Polygon position;
    private WorldNoise worldNoise;
    public static final float VOLATILITY = 2;

    public Location(Point point, long seed) {
        this(new Polygon(point, null), seed, null);
    }

    public Location(Polygon polygon, long seed, WorldNoise worldNoise) {
        this.worldNoise = worldNoise;
        this.position = polygon;
        this.seed = seed;
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

    public double getCenterChunkElevation() {
        return centerChunkElevation;
    }
}