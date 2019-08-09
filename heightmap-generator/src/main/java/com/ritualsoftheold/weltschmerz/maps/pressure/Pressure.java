package com.ritualsoftheold.weltschmerz.maps.pressure;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.ritualsoftheold.weltschmerz.misc.misc.Configuration;

import javax.swing.*;
import java.awt.*;

public class Pressure {
    public static void main(String... args) {
        Weltschmerz weltschmerz = new Weltschmerz();
        new Pressure(weltschmerz);
    }

    public Pressure(Weltschmerz weltschmerz) {
        Configuration configuration = weltschmerz.getConfiguration();

        int width = configuration.longitude;
        int height = configuration.latitude;

        //Creates frame for heigh map
        JFrame worldFrame = new JFrame("World Pressure");

        worldFrame.setPreferredSize(new Dimension(width, height));

        WorldPressureCanvas worldTemperatureCanvas = new WorldPressureCanvas(width, height, weltschmerz.world);

        worldFrame.add(worldTemperatureCanvas);

        worldFrame.setVisible(true);

        worldFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        worldTemperatureCanvas.updateImage();

        worldFrame.pack();

        worldFrame.setLocationRelativeTo(null);
    }
}
