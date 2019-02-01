package com.ritualsoftheold.weltschmerz.maps.world;

import com.ritualsoftheold.weltschmerz.core.World;
import com.ritualsoftheold.weltschmerz.experimental.Lithosphere;

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

        World world = new World(300, 300, 789, 4, 5);
        Canvas canvas = new Canvas(400);
        world.generateWorld(10);
        canvas.updateImage(world.getMap());
        frame.add(canvas);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
