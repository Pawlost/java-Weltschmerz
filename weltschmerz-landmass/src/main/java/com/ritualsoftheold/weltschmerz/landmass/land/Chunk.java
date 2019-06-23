package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.noise.generators.WorldNoise;

public class Chunk {
    private  Point position;
    private double elevation;
    private Chunk[] chunkNeighbor;
    private WorldNoise noise;
    public final String sectorName;

    public Chunk(Point position, double elevation, WorldNoise noise, String sectorName) {
        this.position = position;
        this.elevation = elevation;
        this.noise = noise;
        chunkNeighbor = new Chunk[4];
        this.sectorName = sectorName;
    }
    public double getNoise(int x, int z) {
        return (noise.getNoise(x + (int)position.x*64, z + (int)position.y*64))/1024;
}


    public int getElevation() {
        return ((int)elevation/1024);
    }


    public double getMax() {
        return noise.getMax();
    }
}
