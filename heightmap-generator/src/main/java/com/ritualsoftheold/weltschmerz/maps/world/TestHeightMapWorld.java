package com.ritualsoftheold.weltschmerz.maps.world;

import com.ritualsoftheold.weltschmerz.noise.Configuration;
import com.ritualsoftheold.weltschmerz.core.MapIO;
import com.ritualsoftheold.weltschmerz.noise.WeltschmerzNoise;
import com.ritualsoftheold.weltschmerz.core.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestHeightMapWorld {
    public static void main(String... args) {
        Configuration configuration = MapIO.loadMapConfig();
        int width = configuration.width;
        int height = configuration.height;

        //Creates frame for heigh map
        JFrame frame = new JFrame("Weltschmerz");
        JFrame framePlate = new JFrame("Tectonic plate");

        framePlate.setPreferredSize(new Dimension(width, height));
        frame.setPreferredSize(new Dimension(width, height));

        frame.setVisible(true);
        framePlate.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        framePlate.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        WeltschmerzNoise noise = new WeltschmerzNoise(configuration);
        World world = new World(configuration, noise);
        world.firstGeneration();
        Canvas canvas = new Canvas(width, height, world);
        TectonicCanvas tectonicCanvas = new TectonicCanvas(configuration.width, configuration.height,
                world);

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

        tectonicCanvas.add(btnStart);
        tectonicCanvas.add(btnFill);
        canvas.add(btnFill2);

        framePlate.add(tectonicCanvas);
        frame.add(canvas);

        frame.pack();
        framePlate.pack();

        frame.setLocationRelativeTo(null);
        framePlate.setLocationRelativeTo(null);
    }
}
