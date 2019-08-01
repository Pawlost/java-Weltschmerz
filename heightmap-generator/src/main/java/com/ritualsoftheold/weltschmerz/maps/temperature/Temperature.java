package com.ritualsoftheold.weltschmerz.maps.temperature;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;

import javax.swing.*;
import java.awt.*;


public class Temperature {
    public static void main(String... args) {
        Weltschmerz weltschmerz = new Weltschmerz();
        new Temperature(weltschmerz);
    }

    public Temperature(Weltschmerz weltschmerz){
        Configuration configuration = weltschmerz.getConfiguration();

        int width = configuration.longitude;
        int height = configuration.latitude;

        //Creates frame for heigh map
        JFrame worldFrame = new JFrame("World Temperature");

        worldFrame.setPreferredSize(new Dimension(width, height));

        WorldTemperatureCanvas worldTemperatureCanvas = new WorldTemperatureCanvas(width, height, configuration.maxTemperature);

        worldFrame.add(worldTemperatureCanvas);

        worldFrame.setVisible(true);

        worldFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        worldTemperatureCanvas.updateImage(weltschmerz.world);

        worldFrame.pack();

        worldFrame.setLocationRelativeTo(null);
    }
}
