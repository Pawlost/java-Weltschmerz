package com.ritualsoftheold.weltschmerz.maps.world;

import com.ritualsoftheold.weltschmerz.WeltschmerzNoise;
import com.ritualsoftheold.weltschmerz.core.World;
import com.sudoplay.joise.module.ModuleAutoCorrect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestHeightMapWorld {
    public static void main(String... args) {
        int width = 600;
        int height = 700;

        //Creates frame for heigh map
        JFrame frame = new JFrame("Weltschmerz");
        frame.setPreferredSize(new Dimension(width, height));

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        WeltschmerzNoise noise = new WeltschmerzNoise(7987099, 3, 0.01);
        ModuleAutoCorrect module = noise.generateNoise();
        World world = new World(10000, 600, 6, 10, 1000, module);
        Canvas canvas = new Canvas(600, world);
        TectonicCanvas tectonicCanvas = new TectonicCanvas(600, world);

        tectonicCanvas.drawWorld();
        canvas.fillWorld();
        canvas.drawWorld();

        JButton btnStart = new JButton("Reverse");
        btnStart.addActionListener(new ActionListener() {
            private int i = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                //canvas.reverse();
                i ++;
            }
        });

        JButton btnCheck = new JButton("Reshape");
        btnCheck.addActionListener(e -> canvas.reshapeWorld());

        btnStart.setBounds(10, 10, 110, 100);
        btnCheck.setBounds(10, 110, 110, 100);

        canvas.add(btnStart);
        canvas.add(btnCheck);

       // frame.add(tectonicCanvas);
        frame.add(canvas);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
