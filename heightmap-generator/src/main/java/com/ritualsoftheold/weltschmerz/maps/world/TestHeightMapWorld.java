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
        JFrame framePlate = new JFrame("Tectonic plate");

        framePlate.setPreferredSize(new Dimension(width, height));
        frame.setPreferredSize(new Dimension(width, height));

        frame.setVisible(true);
        framePlate.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        framePlate.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        WeltschmerzNoise noise = new WeltschmerzNoise(7987099, 3, 0.01);
        ModuleAutoCorrect module = noise.generateNoise();
        World world = new World(600, 15000, 0, 30, 1000,  module);
        world.firstGeneration();
        Canvas canvas = new Canvas(600, world);
        TectonicCanvas tectonicCanvas = new TectonicCanvas(600, world);

        canvas.fillWorld();
        canvas.drawWorld();

        tectonicCanvas.fill();

        JButton btnStart = new JButton("Reverse");
        JButton btnFill = new JButton("Fill");
        JButton btnFill2 = new JButton("Fill");

        btnStart.addActionListener(new ActionListener() {
            private int i = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                tectonicCanvas.drawPart(i);
                i ++;
            }
        });

        btnFill.addActionListener(new ActionListener() {
            private int i = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                tectonicCanvas.fillOnce(i);
                i ++;
            }
        });

        btnFill2.addActionListener(new ActionListener() {
            private int i = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.fillOnce(i);
                canvas.drawOnce(i);
                i ++;
            }
        });

        JButton btnCheck = new JButton("Reshape");
        btnCheck.addActionListener(e -> {
            canvas.reshapeWorld();
            tectonicCanvas.fill();
        });

        JButton btnMove= new JButton("Move Tectonict Plates");
        btnMove.addActionListener(e -> {
            canvas.moveWorld();
            tectonicCanvas.fill();
        });

        tectonicCanvas.add(btnStart);
        tectonicCanvas.add(btnFill);
        canvas.add(btnCheck);
        canvas.add(btnMove);
        canvas.add(btnFill2);

        framePlate.add(tectonicCanvas);
        frame.add(canvas);

        frame.pack();
        framePlate.pack();

        frame.setLocationRelativeTo(null);
        framePlate.setLocationRelativeTo(null);
    }
}
