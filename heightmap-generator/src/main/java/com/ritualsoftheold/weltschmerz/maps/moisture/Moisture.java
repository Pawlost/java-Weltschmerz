package com.ritualsoftheold.weltschmerz.maps.moisture;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.typesafe.config.Config;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputFilter;

public class Moisture {
    public static void main(String... args) {
        Weltschmerz weltschmerz = new Weltschmerz();
        new Moisture(weltschmerz);
    }

    public Moisture(Weltschmerz weltschmerz) {
        Config config = weltschmerz.getConfiguration();

        int latitude = config.getInt("map.latitude");
        int longitude = config.getInt("map.longitude");
        //Creates frame for heigh map
        JFrame worldFrame = new JFrame("World moisture");

        worldFrame.setPreferredSize(new Dimension(longitude, latitude));

        WorldMoistureCanvas worldTemperatureCanvas = new WorldMoistureCanvas(longitude, latitude, weltschmerz.world);

        worldFrame.add(worldTemperatureCanvas);

        worldFrame.setVisible(true);

        worldFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        worldTemperatureCanvas.updateImage();

        worldFrame.pack();

        worldFrame.setLocationRelativeTo(null);
    }
}
