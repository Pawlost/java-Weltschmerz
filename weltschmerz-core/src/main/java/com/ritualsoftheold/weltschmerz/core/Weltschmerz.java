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
        Weltschmerz weltschmerz = new Weltschmerz();

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
    public final World world;
    private ChunkNoise noise;
    private int x;
    private int z;
    private int grassID;
    private int dirtID;

    public Weltschmerz(){
        configuration = MapIO.loadMapConfig();
        WorldNoise noise = new WorldNoise(configuration);
        world = new World(configuration, noise);
        world.firstGeneration();
        System.out.println("Map generated");
    }

    public void setSector(){
        noise = new ChunkNoise(world.getLocations()[0]);
    }

    public void setChunk(int x, int z){
        this.x = x;
        this.z = z;
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

        long size = Math.round(noise.getNoise(x + this.x * 4, z + this.z * 4));
        if (size > y) {
            return dirtID;
        } else if (size == y) {
            return grassID;
        }
        return 1;
    }

    public double getY(){
        return noise.getY();
    }
}
