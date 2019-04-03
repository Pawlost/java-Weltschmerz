package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.noise.WeltschmerzNoise;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Weltschmerz {
    public static void main(String[] args) {
        Configuration configuration = MapIO.loadMapConfig();
        WeltschmerzNoise noise = new WeltschmerzNoise(configuration.seed, configuration.octaves, configuration.frequency);
        World world = new World(configuration.size, configuration.detail, configuration.volcanoes, configuration.tectonicPlates,
                configuration.hills, configuration.islandSize, noise.generateNoise());

        world.firstGeneration();
        BufferedImage image = new BufferedImage(configuration.size, configuration.size, BufferedImage.TYPE_INT_ARGB);
        for(int s = 0; s < configuration.smooth; s++){
            world.reshapeWorld();
        }
        for(int m = 0; m < configuration.tectonicMovement; m++){
            world.moveTectonicPlates();
        }
        Graphics g = image.getGraphics();
        for (Location location : world.getLocations()) {
            g.setColor(location.getLegend().color);
            g.fillPolygon(location.getPolygon());
        }
        MapIO.saveHeightmap(image);

        System.out.println("Map generated");
    }
}
