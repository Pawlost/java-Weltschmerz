package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.noise.Configuration;

import com.ritualsoftheold.weltschmerz.landmass.land.Position;
import com.ritualsoftheold.weltschmerz.noise.generator.WorldNoise;

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
                Position rectangle = location.position;
                g.fillRect(rectangle.x, rectangle.z, rectangle.width, rectangle.height);
            }
        }
        MapIO.saveHeightmap(image);
    }

    private Configuration configuration;
    public final World world;
    private double[][] chunkValues;
    private int grassID;
    private int dirtID;
    private Location currentSector;

    public Weltschmerz(){
        configuration = MapIO.loadMapConfig();
        WorldNoise noise = new WorldNoise(configuration);
        world = new World(configuration, noise);
        System.out.println("Map generated");
    }

    public void changeSector(int x, int z) {
        currentSector = world.getLocations()[Math.abs(x)][Math.abs(z)];
    }

    public String getSectorName(){
        return currentSector.getName();
    }

    public float setChunk(int x, int z){
        chunkValues = currentSector.getChunkValues(x, z);
        return currentSector.getY();
    }

    //For future use
    public void setMaterialID(int grassID, int dirtID){
        this.grassID = grassID;
        this.dirtID = dirtID;
    }

    public int generateVoxel(int x, int y, int z) {
        if (y < currentSector.getMin()){
            return dirtID;
        }

        if (y > currentSector.getMax()){
            return 1;
        }

        long size = Math.round(chunkValues[x][z]);
        if (size > y) {
            return dirtID;
        } else if (size == y) {
            return grassID;
        }
        return 1;
    }

    public int getSectorPostionX(){
        return currentSector.position.x;
    }

    public int getSectorPostionZ(){
        return currentSector.position.z;
    }
}
