package com.ritualsoftheold.weltschmerz.maps.noise;

import com.ritualsoftheold.weltschmerz.core.MapIO;
import com.ritualsoftheold.weltschmerz.landmass.Configuration;

import javax.swing.*;
import java.awt.*;


public class TestHeightMapNoise {
    public static void main(String... args) {
        Configuration configuration = MapIO.loadMapConfig();
        int width = configuration.width;
        int height = configuration.height;
        //Creates frame for heigh map
        JFrame frame = new JFrame("Joise Example 01");
        frame.setPreferredSize(new Dimension(width, height));

        //WorldNoiseCanvas noiseCanvas = new WorldNoiseCanvas(width, height);
        ChunkNoiseCanvas noiseCanvas = new ChunkNoiseCanvas(width, height);
        frame.add(noiseCanvas);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        noiseCanvas.updateImage();

        frame.pack();
        frame.setLocationRelativeTo(null);
    }
}
