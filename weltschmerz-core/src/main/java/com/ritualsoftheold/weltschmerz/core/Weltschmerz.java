package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;

import com.ritualsoftheold.weltschmerz.noise.generators.WorldNoise;

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
            g.drawPolygon(location.position.getSwingPolygon());
        }
        MapIO.saveHeightmap(image);
    }

    private Configuration configuration;
    public final World world;
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
        //currentSector = world.getLocations()[Math.abs(x)][Math.abs(z)];
    }

    public String getSectorName(){
        return currentSector.getName();
    }

    public float setChunk(int x, int z){
        float y = currentSector.setChunk(x, z);
        currentSector.generateNoise();
        return y;
    }

    //For future use
    public void setMaterialID(int grassID, int dirtID){
        this.grassID = grassID;
        this.dirtID = dirtID;
    }

    public int generateVoxel(int x, int y, int z) {
        /*if (y < currentSector.getMin()){
            return dirtID;
        }

        if (y > currentSector.getMax()){
            return 1;
        }*/
        long size = Math.round(currentSector.getNoise(x, z));
        if (size > y) {
            return dirtID;
        } else if (size == y) {
            return grassID;
        }else {
            return 1;
        }
    }

    public double getSectorPostionX(){
        return currentSector.position.centroid.x;
    }

    public double getSectorPostionZ(){
        return currentSector.position.centroid.y;
    }
}
