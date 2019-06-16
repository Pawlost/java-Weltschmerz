package com.ritualsoftheold.weltschmerz.maps;

import com.ritualsoftheold.weltschmerz.core.MapIO;
import com.ritualsoftheold.weltschmerz.core.Weltschmerz;
import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;
import com.ritualsoftheold.weltschmerz.maps.noise.ChunkNoiseCanvas;
import com.ritualsoftheold.weltschmerz.maps.noise.WorldNoiseCanvas;
import com.ritualsoftheold.weltschmerz.maps.world.ElevationCanvas;
import com.ritualsoftheold.weltschmerz.maps.world.TectonicCanvas;

import javax.swing.*;
import java.awt.*;

public class All {
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
        ElevationCanvas canvas = new ElevationCanvas(width, height, weltschmerz.world);
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
