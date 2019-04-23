package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.Configuration;
import com.ritualsoftheold.weltschmerz.landmass.land.Sector;
import com.ritualsoftheold.weltschmerz.noise.WorldNoise;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
    private World world;

    public Weltschmerz(){
        configuration = MapIO.loadMapConfig();
        WorldNoise noise = new WorldNoise(configuration);
        world = new World(configuration, noise);
        world.firstGeneration();
        System.out.println("Map generated");
    }

    //For future use
    public void setMaterialID(){

    }

    public ArrayList<Integer> getChunk(int X, int Z){
        return null;
    }
}
