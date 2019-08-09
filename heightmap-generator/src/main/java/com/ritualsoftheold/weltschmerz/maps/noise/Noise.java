package com.ritualsoftheold.weltschmerz.maps.noise;

import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.ritualsoftheold.weltschmerz.misc.misc.Configuration;

import javax.swing.*;
import java.awt.*;

public class Noise {
    public static void main(String... args) {
        Weltschmerz weltschmerz = new Weltschmerz();
        new Noise(weltschmerz);
    }

    public Noise (Weltschmerz weltschmerz){
        Configuration configuration = weltschmerz.getConfiguration();
        int width = configuration.longitude;
        int height = configuration.latitude;

        //Creates frame for heigh map
        JFrame worldFrame = new JFrame("World Noise");
        worldFrame.setPreferredSize(new Dimension(width, height));
        WorldNoiseCanvas worldNoiseCanvas = new WorldNoiseCanvas(width, height, weltschmerz.world);
        worldFrame.add(worldNoiseCanvas);
        worldFrame.setVisible(true);
        worldFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        worldNoiseCanvas.updateImage();

        worldFrame.pack();
        worldFrame.setLocationRelativeTo(null);
    }
}
