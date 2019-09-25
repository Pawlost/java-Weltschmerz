package com.ritualsoftheold.weltschmerz.maps.humidity;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.typesafe.config.Config;

import javax.swing.*;
import java.awt.*;

public class Humidity {
    public static void main(String... args) {
        Weltschmerz weltschmerz = new Weltschmerz(true);
        new Humidity(weltschmerz);
    }

    private Humidity(Weltschmerz weltschmerz) {
        Config config = weltschmerz.getConfiguration();

        int latitude = config.getInt("map.latitude");
        int longitude = config.getInt("map.longitude");

        //Creates frame for heigh map
        JFrame worldFrame = new JFrame("World humidity");

        worldFrame.setPreferredSize(new Dimension(longitude, latitude));

        WorldHumidityCanvas worldTemperatureCanvas = new WorldHumidityCanvas(longitude, latitude, weltschmerz);

        worldFrame.add(worldTemperatureCanvas);

        worldFrame.setVisible(true);

        worldFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        worldTemperatureCanvas.updateImage();

        worldFrame.pack();

        worldFrame.setLocationRelativeTo(null);
    }
}
