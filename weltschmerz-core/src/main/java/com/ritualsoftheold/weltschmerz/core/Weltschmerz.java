package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.noise.Configuration;
import com.ritualsoftheold.weltschmerz.noise.WeltschmerzNoise;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Weltschmerz {
    public static void main(String[] args) {
        Configuration configuration = MapIO.loadMapConfig();
        WeltschmerzNoise noise = new WeltschmerzNoise(configuration);
        World world = new World(configuration, noise);
        world.firstGeneration();
        BufferedImage image = new BufferedImage(configuration.width, configuration.height, BufferedImage.TYPE_INT_ARGB);

        Graphics g = image.getGraphics();
        for (Location location : world.getLocations()) {
            g.setColor(location.getShape().color);
            g.fillPolygon(location.getPolygon());
        }
        MapIO.saveHeightmap(image);

        System.out.println("Map generated");
    }
}
