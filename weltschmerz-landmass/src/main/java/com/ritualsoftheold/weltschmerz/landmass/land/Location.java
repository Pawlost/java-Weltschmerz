package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.landmass.Constants;
import com.ritualsoftheold.weltschmerz.noise.Shape;
import com.ritualsoftheold.weltschmerz.noise.generator.ChunkNoise;

import java.util.ArrayList;

public class Location {

    private long seed;
    private Plate tectonicPlate;
    private Shape shape;
    private boolean isLand;
    private Location[] neighbors;
    private Position position;

    public Location(int x, int z, long seed) {
        position = new Position(x, z, 1, 1);
        this.seed = seed;
        neighbors = new Location[4];
    }

    public Location[] getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(Location neighbor, int position){
        neighbors[position] = neighbor;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
        this.isLand = shape.land;
    }

    public Shape getShape() {
        return shape;
    }

    public double[][] getChunkValues(int posX, int posZ) {
        double[][] chunkValues = new double[64][64];
        ChunkNoise noise = new ChunkNoise(seed, shape.min, shape.max);
        for (int x = 0; x < 64; x++) {
            for (int z = 0; z < 64; z++) {
                chunkValues[x][z] = noise.getNoise(x + posX * 4, z + posZ * 4);
            }
        }
        return chunkValues;
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
}
