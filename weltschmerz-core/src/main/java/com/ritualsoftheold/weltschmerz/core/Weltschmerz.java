package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.Configuration;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.land.Sector;
import com.ritualsoftheold.weltschmerz.noise.ChunkNoise;
import com.ritualsoftheold.weltschmerz.noise.WorldNoise;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Weltschmerz {
    //Generate map image
    public static void main(String[] args) {
        Weltschmerz weltschmerz = new Weltschmerz(0, 0);

        Configuration configuration = weltschmerz.configuration;
        BufferedImage image = new BufferedImage(configuration.width, configuration.height, BufferedImage.TYPE_INT_ARGB);

        Graphics g = image.getGraphics();
        for (Location location : weltschmerz.world.getLocations()) {
            g.setColor(location.getShape().color);
            Sector rectangle = location.getSector();
            g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
        MapIO.saveHeightmap(image);
    }

    private Configuration configuration;
    private World world;
    private ChunkNoise noise;
    private int x;
    private int z;
    private int grassID;
    private int dirtID;

    public Weltschmerz(int grassID, int dirtID){
        this.grassID = grassID;
        this.dirtID = dirtID;
        configuration = MapIO.loadMapConfig();
        WorldNoise noise = new WorldNoise(configuration);
        world = new World(configuration, noise);
        world.firstGeneration();
        System.out.println("Map generated");
    }

    public boolean setChunk(int x, int y, int z){
        noise = new ChunkNoise(world.getLocations()[0]);
        this.x = x;
        this.z = z;
        return y >= 0;
    }

    //For future use
    public void setMaterialID(){
    }

    public int generateVoxel(int x, int y, int z) {
        long size = Math.round(noise.getNoise(x + this.x * 4, z + this.z * 4));
        if (size < y) {
            return 1;
        } else if (size == y) {
            return grassID;
        }
        return dirtID;
    }
}
