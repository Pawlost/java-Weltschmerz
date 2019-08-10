package com.ritualsoftheold.weltschmerz.maps.circulation;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.typesafe.config.Config;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputFilter;

public class Circulation {
    public static void main(String... args) {
        Weltschmerz weltschmerz = new Weltschmerz();
        new Circulation(weltschmerz);
    }

    public Circulation(Weltschmerz weltschmerz) {
        Config config = weltschmerz.getConfiguration();

        int latitude = config.getInt("map.latitude");
        int longitude = config.getInt("map.longitude");

        //Creates frame for heigh map
        JFrame worldFrame = new JFrame("World Pressure");

        worldFrame.setPreferredSize(new Dimension(longitude, latitude));

        WorldCirculationCanvas worldTemperatureCanvas = new WorldCirculationCanvas(longitude, latitude, weltschmerz.world);

        worldFrame.add(worldTemperatureCanvas);

        worldFrame.setVisible(true);

        worldFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        worldTemperatureCanvas.updateImage();

        worldFrame.pack();

        worldFrame.setLocationRelativeTo(null);
    }
}
