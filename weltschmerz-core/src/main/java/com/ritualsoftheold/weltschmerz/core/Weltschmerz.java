package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.noise.Configuration;
import com.ritualsoftheold.weltschmerz.landmass.Constants;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
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
                Position rectangle = location.getPosition();
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

    public void changeSector(int x, int z){
        currentSector = world.getLocations()[x][z];
        if(z == 1) {
            currentSector = new Location(0, 0, 1);
            currentSector.setShape(configuration.shapes.get("MOUNTAIN"));
        }
    }

    public String getSectorName(){
        return currentSector.getKey();
    }

    public void setChunk(int x, int z){
        chunkValues = currentSector.getChunkValues(x, z);
    }

    //For future use
    public void setMaterialID(int grassID, int dirtID){
        this.grassID = grassID;
        this.dirtID = dirtID;
    }

    public int generateVoxel(int x, int y, int z) {
        if (y < currentSector.getShape().min){
            return dirtID;
        }

        if (y > currentSector.getShape().max){
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

    public double getY(){
        return currentSector.getShape().position * Constants.MAX_SECTOR_HEIGHT_DIFFERENCE;
    }
}
