package com.ritualsoftheold.weltschmerz.maps.fortune;


import javax.swing.*;
import java.awt.*;

public class TestHeightMapFortune {
    public static void main(String... args) {
        int width = 1600;
        int height = 900;
        //Creates frame for heigh map
        JFrame frame = new JFrame("Joise Example 02");
        frame.setPreferredSize(new Dimension(width, height));

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Canvas canvas = new Canvas(400);
        canvas.updateImage();

        frame.add(canvas);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
