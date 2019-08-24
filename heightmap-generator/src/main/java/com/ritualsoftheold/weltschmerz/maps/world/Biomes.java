package com.ritualsoftheold.weltschmerz.maps.world;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.typesafe.config.Config;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputFilter;

public class Biomes {

    public static void main(String... args) {
        Weltschmerz weltschmerz = new Weltschmerz();
        new Biomes(weltschmerz);
    }

    private Biomes(Weltschmerz weltschmerz){
        Config config = weltschmerz.getConfiguration();

        int latitude = config.getInt("map.latitude");
        int longitude = config.getInt("map.longitude");

        //Creates frame for heigh map
        JFrame frame = new JFrame("Bioms");

        frame.setPreferredSize(new Dimension(longitude, latitude));

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        WorldBiomesCanvas canvas = new WorldBiomesCanvas(longitude, latitude, weltschmerz);

        frame.add(canvas);

        canvas.updateImage();

        frame.pack();

        frame.setLocationRelativeTo(null);
    }
}
