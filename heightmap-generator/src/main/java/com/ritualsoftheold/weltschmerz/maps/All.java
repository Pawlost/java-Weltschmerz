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

        //Creates frame for heigh map
        JFrame chunkFrame = new JFrame("Chunk Noise");
        JFrame worldFrame = new JFrame("World Noise");

        framePlate.setPreferredSize(new Dimension(width, height));
        frame.setPreferredSize(new Dimension(width, height));

        chunkFrame.setPreferredSize(new Dimension(width, height));
        worldFrame.setPreferredSize(new Dimension(width, height));

        frame.setVisible(true);
        framePlate.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        framePlate.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Weltschmerz weltschmerz = new Weltschmerz();

        frame.setLocationRelativeTo(null);
        framePlate.setLocationRelativeTo(null);


        ElevationCanvas elevationCanvas = new ElevationCanvas(width, height, weltschmerz.world);
        TectonicCanvas tectonicCanvas = new TectonicCanvas(width, height, weltschmerz.world);

        elevationCanvas.fillWorld();
        tectonicCanvas.fill();

        //WorldNoiseCanvas chunkNoiseCanvas = new WorldNoiseCanvas(width, height);
        ChunkNoiseCanvas chunkNoiseCanvas = new ChunkNoiseCanvas(width, height);
        WorldNoiseCanvas worldNoiseCanvas = new WorldNoiseCanvas(width, height);

        JScrollPane tectonicPane = new JScrollPane(tectonicCanvas);
        JScrollPane elevationPane = new JScrollPane(elevationCanvas);
        JScrollPane worldNoisePane = new JScrollPane(worldNoiseCanvas);
        JScrollPane chunkNoisePane = new JScrollPane(chunkNoiseCanvas);

        tectonicPane.getViewport().setPreferredSize(new Dimension(width, height));
        elevationPane.getViewport().setPreferredSize(new Dimension(width, height));
        worldNoisePane.getViewport().setPreferredSize(new Dimension(width, height));
        chunkNoisePane.getViewport().setPreferredSize(new Dimension(width, height));

        tectonicPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        tectonicPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        elevationPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        elevationPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        worldNoisePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        worldNoisePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chunkNoisePane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        chunkNoisePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        framePlate.add(tectonicPane);
        frame.add(elevationPane);

        chunkFrame.add(chunkNoisePane);
        worldFrame.add(worldNoisePane);

        chunkFrame.setVisible(true);
        worldFrame.setVisible(true);

        chunkFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        worldFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        chunkNoiseCanvas.updateImage();
        worldNoiseCanvas.updateImage(configuration);

        frame.pack();
        framePlate.pack();

        chunkFrame.pack();
        worldFrame.pack();

        chunkFrame.setLocationRelativeTo(null);
        worldFrame.setLocationRelativeTo(null);
    }
}
