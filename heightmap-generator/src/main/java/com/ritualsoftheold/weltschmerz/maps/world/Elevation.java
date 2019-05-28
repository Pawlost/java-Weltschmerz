package com.ritualsoftheold.weltschmerz.maps.world;

import com.ritualsoftheold.weltschmerz.core.MapIO;
import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.ritualsoftheold.weltschmerz.noise.Configuration;

import javax.swing.*;
import java.awt.*;

public class Elevation {
    public static void main(String... args) {
        Configuration configuration = MapIO.loadMapConfig();
        int width = configuration.width;
        int height = configuration.height;

        //Creates frame for heigh map
        JFrame frame = new JFrame("Elevation");
        JFrame framePlate = new JFrame("Tectonic plate");

        framePlate.setPreferredSize(new Dimension(width, height));
        frame.setPreferredSize(new Dimension(width, height));

        frame.setVisible(true);
        framePlate.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        framePlate.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Weltschmerz weltschmerz = new Weltschmerz();
        Canvas canvas = new Canvas(width, height, weltschmerz.world);
        TectonicCanvas tectonicCanvas = new TectonicCanvas(configuration.width, configuration.height,
                weltschmerz.world);

        canvas.fillWorld();

        tectonicCanvas.fill();

        framePlate.add(tectonicCanvas);
        frame.add(canvas);

        frame.pack();
        framePlate.pack();

        frame.setLocationRelativeTo(null);
        framePlate.setLocationRelativeTo(null);
    }
}
