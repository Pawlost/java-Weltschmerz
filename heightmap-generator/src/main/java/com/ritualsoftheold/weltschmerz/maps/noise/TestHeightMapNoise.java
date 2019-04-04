package com.ritualsoftheold.weltschmerz.maps.noise;

import com.ritualsoftheold.weltschmerz.noise.WeltschmerzNoise;

import javax.swing.*;
import java.awt.*;


public class TestHeightMapNoise {
    public static void main(String... args) {
        int width = 640;
        int height = 480;
        //Creates frame for heigh map
        JFrame frame = new JFrame("Joise Example 01");
        frame.setPreferredSize(new Dimension(width, height));

        Canvas canvas = new Canvas(width, height);
        frame.add(canvas);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        WeltschmerzNoise noise = new WeltschmerzNoise(7987099, 5, 5);
        canvas.updateImage(noise.generateNoise());

        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
