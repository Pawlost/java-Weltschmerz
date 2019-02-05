package com.ritualsoftheold.weltschmerz.maps.experimental.shape;

import javax.swing.*;
import java.awt.*;

public class TestHeightMapExperimentShape {
    public static void main(String... args) {
        int width = 500;
        int height = 500;
        //Creates frame for heigh map
        JFrame frame = new JFrame("Weltschmerz");
        frame.setPreferredSize(new Dimension(width, height));

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        CanvasShape canvasShape = new CanvasShape(600);
        canvasShape.updateImage();
        frame.add(canvasShape);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
