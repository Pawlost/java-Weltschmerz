package com.ritualsoftheold.weltschmerz.maps.precipitation;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;

import javax.swing.*;
import java.awt.*;

public class Precipitation {
    public static void main(String... args) {
        Weltschmerz weltschmerz = new Weltschmerz();
        new Precipitation(weltschmerz);
    }

    public Precipitation(Weltschmerz weltschmerz) {
        Configuration configuration = weltschmerz.getConfiguration();

        int width = configuration.longitude;
        int height = configuration.latitude;

        //Creates frame for heigh map
        JFrame worldFrame = new JFrame("World precipitation");

        worldFrame.setPreferredSize(new Dimension(width, height));

        WorldPrecipitationCanvas worldTemperatureCanvas = new WorldPrecipitationCanvas(width, height, weltschmerz.world);

        worldFrame.add(worldTemperatureCanvas);

        worldFrame.setVisible(true);

        worldFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        worldTemperatureCanvas.updateImage();

        worldFrame.pack();

        worldFrame.setLocationRelativeTo(null);
    }
}
