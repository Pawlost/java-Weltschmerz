package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.land.Legend;
import com.ritualsoftheold.weltschmerz.noise.WeltschmerzNoise;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Weltschmerz {
    public static void main(String[] args) {
        Configuration configuration = MapIO.loadMapConfig();
        WeltschmerzNoise noise = new WeltschmerzNoise(configuration.seed, configuration.octaves, configuration.frequency,
                configuration.width, configuration.height, configuration.shapes);
        World world = new World(configuration.width, configuration.height, configuration.detail, configuration.volcanoes, configuration.tectonicPlates,
                configuration.islandSize, noise);
        world.firstGeneration();
        BufferedImage image = new BufferedImage(configuration.width, configuration.height, BufferedImage.TYPE_INT_ARGB);
        for(int s = 0; s < configuration.smooth; s++){
            world.reshapeWorld();
        }
        for(int m = 0; m < configuration.tectonicMovement; m++){
            world.moveTectonicPlates();
        }
        Graphics g = image.getGraphics();
        for (Location location : world.getLocations()) {
            g.setColor(location.getShape().color);
            g.fillPolygon(location.getPolygon());
        }
        MapIO.saveHeightmap(image);

        System.out.println("Map generated");
    }
}
