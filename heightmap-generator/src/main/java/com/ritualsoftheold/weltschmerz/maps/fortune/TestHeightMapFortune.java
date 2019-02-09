package com.ritualsoftheold.weltschmerz.maps.fortune;

import com.ritualsoftheold.weltschmerz.core.World;

import javax.swing.*;
import java.awt.*;

public class TestHeightMapFortune {
    public static void main(String... args) {
        int width = 500;
        int height = 500;
        //Creates frame for heigh map
        JFrame frame = new JFrame("Weltschmerz");
        frame.setPreferredSize(new Dimension(width, height));

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        World world = new World(500);
        Canvas canvas = new Canvas(300, world);
        canvas.paintWorld();

        JButton btnStart = new JButton("Start");
        btnStart.addActionListener(e -> canvas.changeWorld());

        btnStart.setBounds(10, 10, 110, 100);

        canvas.add(btnStart);
        frame.add(canvas);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
