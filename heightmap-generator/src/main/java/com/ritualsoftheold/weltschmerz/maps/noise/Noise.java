package com.ritualsoftheold.weltschmerz.maps.noise;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.typesafe.config.Config;

import javax.swing.*;
import java.awt.*;

public class Noise {
    public static void main(String... args) {
        Weltschmerz weltschmerz = new Weltschmerz();
        new Noise(weltschmerz);
    }

    private Noise(Weltschmerz weltschmerz){
        Config config = weltschmerz.getConfiguration();
        int latitude = config.getInt("map.latitude");
        int longitude = config.getInt("map.longitude");

        //Creates frame for heigh map
        JFrame worldFrame = new JFrame("World Noise");
        worldFrame.setPreferredSize(new Dimension(longitude, latitude));
        WorldNoiseCanvas worldNoiseCanvas = new WorldNoiseCanvas(longitude, latitude, weltschmerz);
        worldFrame.add(worldNoiseCanvas);
        worldFrame.setVisible(true);
        worldFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        worldNoiseCanvas.updateImage();

        worldFrame.pack();
        worldFrame.setLocationRelativeTo(null);
    }
}
