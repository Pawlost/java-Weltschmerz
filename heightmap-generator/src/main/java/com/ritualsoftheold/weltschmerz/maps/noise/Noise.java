package com.ritualsoftheold.weltschmerz.maps.noise;

import com.ritualsoftheold.weltschmerz.core.MapIO;
import com.ritualsoftheold.weltschmerz.noise.Configuration;

import javax.swing.*;
import java.awt.*;


public class Noise {
    public static void main(String... args) {
        Configuration configuration = MapIO.loadMapConfig();
        int width = configuration.width;
        int height = configuration.height;

        //Creates frame for heigh map
        JFrame chunkFrame = new JFrame("Chunk Noise");
        JFrame worldFrame = new JFrame("World Noise");

        chunkFrame.setPreferredSize(new Dimension(width, height));
        worldFrame.setPreferredSize(new Dimension(width, height));

        //WorldNoiseCanvas chunkNoiseCanvas = new WorldNoiseCanvas(width, height);
        ChunkNoiseCanvas chunkNoiseCanvas = new ChunkNoiseCanvas(width, height);
        WorldNoiseCanvas worldNoiseCanvas = new WorldNoiseCanvas(width, height);

        chunkFrame.add(chunkNoiseCanvas);
        worldFrame.add(worldNoiseCanvas);

        chunkFrame.setVisible(true);
        worldFrame.setVisible(true);

        chunkFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        worldFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        chunkNoiseCanvas.updateImage();
        worldNoiseCanvas.updateImage(configuration);

        chunkFrame.pack();
        worldFrame.pack();

        chunkFrame.setLocationRelativeTo(null);
        worldFrame.setLocationRelativeTo(null);
    }
}
