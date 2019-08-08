package com.ritualsoftheold.weltschmerz.maps.world;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;

import javax.swing.*;
import java.awt.*;

public class Biomes {

    public static void main(String... args) {
        Weltschmerz weltschmerz = new Weltschmerz();
        new Biomes(weltschmerz);
    }

    public Biomes(Weltschmerz weltschmerz){
        Configuration configuration = weltschmerz.getConfiguration();

        int width = configuration.longitude;
        int height = configuration.latitude;

        //Creates frame for heigh map
        JFrame frame = new JFrame("Bioms");

        frame.setPreferredSize(new Dimension(width, height));

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        WorldBiomesCanvas canvas = new WorldBiomesCanvas(width, height, weltschmerz.world);

        frame.add(canvas);

        canvas.updateImage();

        frame.pack();

        frame.setLocationRelativeTo(null);
    }
}
