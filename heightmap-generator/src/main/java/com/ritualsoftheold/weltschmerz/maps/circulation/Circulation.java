package com.ritualsoftheold.weltschmerz.maps.circulation;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;

import javax.swing.*;
import java.awt.*;

public class Circulation {
    public static void main(String... args) {
        Weltschmerz weltschmerz = new Weltschmerz();
        new Circulation(weltschmerz);
    }

    public Circulation(Weltschmerz weltschmerz) {
        Configuration configuration = weltschmerz.getConfiguration();

        int width = configuration.longitude;
        int height = configuration.latitude;

        //Creates frame for heigh map
        JFrame worldFrame = new JFrame("World Pressure");

        worldFrame.setPreferredSize(new Dimension(width, height));

        WorldCirculationCanvas worldTemperatureCanvas = new WorldCirculationCanvas(width, height);

        worldFrame.add(worldTemperatureCanvas);

        worldFrame.setVisible(true);

        worldFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        worldTemperatureCanvas.updateImage(weltschmerz.world);

        worldFrame.pack();

        worldFrame.setLocationRelativeTo(null);
    }
}
