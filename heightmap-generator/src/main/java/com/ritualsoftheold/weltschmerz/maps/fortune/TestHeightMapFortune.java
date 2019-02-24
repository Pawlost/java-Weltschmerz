package com.ritualsoftheold.weltschmerz.maps.fortune;

import com.ritualsoftheold.weltschmerz.WeltschmerzNoise;
import com.ritualsoftheold.weltschmerz.core.World;
import com.sudoplay.joise.module.ModuleAutoCorrect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestHeightMapFortune {
    public static void main(String... args) {
        int width = 500;
        int height = 500;

        //Creates frame for heigh map
        JFrame frame = new JFrame("Weltschmerz");
        frame.setPreferredSize(new Dimension(width, height));

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        World world = new World(10000, 400);
        WeltschmerzNoise noise = new WeltschmerzNoise(7987099, 3, 0.01);
        ModuleAutoCorrect module = noise.generateNoise();
        Canvas canvas = new Canvas(600, world, module);

        canvas.fillWorld();
        canvas.drawWorld();

        JButton btnStart = new JButton("Start");
        btnStart.addActionListener(new ActionListener() {
            private int i = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.fillOnce(i);
                i ++;
            }
        });

        JButton btnCheck = new JButton("Reshape");
        btnCheck.addActionListener(e -> canvas.reshapeWorld());

        btnStart.setBounds(10, 10, 110, 100);
        btnCheck.setBounds(10, 110, 110, 100);

        canvas.add(btnStart);
        canvas.add(btnCheck);

        frame.add(canvas);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
