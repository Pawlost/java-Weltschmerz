package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.Configuration;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.land.Position;
import com.ritualsoftheold.weltschmerz.noise.ChunkNoise;
import com.ritualsoftheold.weltschmerz.noise.WorldNoise;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Weltschmerz {
    //Generate map image
    public static void main(String[] args) {
        Weltschmerz weltschmerz = new Weltschmerz();

        Configuration configuration = weltschmerz.configuration;
        BufferedImage image = new BufferedImage(configuration.width, configuration.height, BufferedImage.TYPE_INT_ARGB);

        Graphics g = image.getGraphics();
        for (Location[] locations : weltschmerz.world.getLocations()) {
            for (Location location : locations) {
                g.setColor(location.getShape().color);
                Position rectangle = location.getPosition();
                g.fillRect(rectangle.x, rectangle.z, rectangle.width, rectangle.height);
            }
        }
        MapIO.saveHeightmap(image);
    }

    private Configuration configuration;
    public final World world;
    private ChunkNoise noise;
    private Position chunkPosition;
    public static final int DEFAULT_MAX_SECTOR_X = 256;
    public static final int DEFAULT_MAX_SECTOR_Z = 256;
    private int grassID;
    private int dirtID;
    private Location currentSector;

    public Weltschmerz(){
        configuration = MapIO.loadMapConfig();
        WorldNoise noise = new WorldNoise(configuration);
        world = new World(configuration, noise);
        world.firstGeneration();
        System.out.println("Map generated");
    }

    public void changeSector(int x, int z){
        currentSector = world.getLocations()[x][z];
        if(z == 1) {
            currentSector = new Location(0, 0, 1);
            currentSector.setShape(configuration.shapes.get("MOUNTAIN"));
        }
        noise = new ChunkNoise(currentSector);
    }

    public String getSectorName(){
        return currentSector.getKey();
    }

    public void setChunk(int x, int z){
        chunkPosition = new Position(x, z, 16 + x, 16 + z);
    }

    //For future use
    public void setMaterialID(int grassID, int dirtID){
        this.grassID = grassID;
        this.dirtID = dirtID;
    }

    public int generateVoxel(int x, int y, int z) {
        if (y < noise.getMin()){
            return dirtID;
        }

        if (y > noise.getMax()){
            return 1;
        }

        long size = Math.round(noise.getNoise(x + chunkPosition.x * 4, z + chunkPosition.z * 4));
        if (size > y) {
            return dirtID;
        } else if (size == y) {
            return grassID;
        }
        return 1;
    }

    public double getY(){
        return currentSector.getShape().position * 16;
    }
}
