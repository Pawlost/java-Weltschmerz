package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;
import com.ritualsoftheold.weltschmerz.environment.BiomDefinition;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MapIO {
    private static final String OUTPUT_FILE = "map.png";
    private static final String CONFIG_FILE = "config";
    private static final String SIMULATION_FILE = "simulation";
    private static final String BIOM_DISTRIBUTION_FILE = "Biome_distribution.png";

    //Save image into file
    public static void saveImage(BufferedImage image) {
        try {
            File file = new File("./" + OUTPUT_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load our own config values from the default location
    public static Configuration loadMapConfig() {
        Config conf = ConfigFactory.load(CONFIG_FILE);
        return ConfigParser.parseConfig(conf);
    }

    public static BufferedImage loadMap(String path){
        try {
            File file = new File(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(path)).getFile());
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<BiomDefinition> loadBiomMap(Configuration conf){
        HashMap<Integer, BiomDefinition> biomMap = new HashMap<>();
        BufferedImage image = loadMap(BIOM_DISTRIBUTION_FILE);

        assert image != null;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int color = image.getRGB(x, y);
                BiomDefinition distribution = biomMap.get(color);
                if(color != Color.WHITE.getRGB()) {
                    if (distribution == null) {
                        distribution = new BiomDefinition(conf.shapes.get(color), color);
                        biomMap.put(color, distribution);
                    }
                    distribution.addPoint(x, y);
                }
            }
        }
        return new ArrayList<>(biomMap.values());
    }
}