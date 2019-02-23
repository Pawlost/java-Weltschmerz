package com.ritualsoftheold.weltschmerz.maps.world;

import com.ritualsoftheold.weltschmerz.WeltschmerzNoise;

import javax.swing.*;
import java.awt.*;

public class TestHeightMapWorld {
    public static void main(String... args) {
        int width = 500;
        int height = 500;
        //Creates frame for heigh map
        JFrame frame = new JFrame("Weltschmerz");
        frame.setPreferredSize(new Dimension(width, height));

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Canvas canvas = new Canvas(600, 600);
        WeltschmerzNoise noise = new WeltschmerzNoise(213, 9, 32);
        frame.add(canvas);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
