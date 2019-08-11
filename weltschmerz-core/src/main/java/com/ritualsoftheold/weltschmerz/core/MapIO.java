package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.environment.BiomDefinition;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import jodd.util.ClassLoaderUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MapIO {
    private static final String BIOMS_PATH = "biomes";
    private static final String OUTPUT_FILE = "map.png";
    private static final String CONFIG_FILE = "config";
    private static final String SIMULATION_FILE = "simulation";
    private static final String BIOME_DISTRIBUTION_FILE = "Biome_distribution.png";

    //Save image into file
    public static void saveImage(BufferedImage image) {
        try {
            File file = new File("./" + OUTPUT_FILE);
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load our own config values from the default location
    static Config loadMapConfig() {
        return  ConfigFactory.load(CONFIG_FILE);
    }

    static BufferedImage loadMap(String path){
        try {
            InputStream file = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            assert file != null;
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static ArrayList<BiomDefinition> loadBiomMap(Config conf){
        HashMap<Integer, BiomDefinition> biomesMap = new HashMap<>();
        HashMap<Integer, String> colorDefinitios = parseColorDefinitions(conf);
        BufferedImage image = loadMap(BIOME_DISTRIBUTION_FILE);

        assert image != null;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int color = image.getRGB(x, y);
                BiomDefinition distribution = biomesMap.get(color);
                if(color != Color.WHITE.getRGB()) {
                    if (distribution == null) {
                        distribution = new BiomDefinition(colorDefinitios.get(color), color);
                        biomesMap.put(color, distribution);
                    }
                    distribution.addPoint(x, y);
                }
            }
        }
        return new ArrayList<>(biomesMap.values());
    }

    static void saveConfiguration(Config config){
        config.getString("");
    }

    private static HashMap<Integer, String> parseColorDefinitions(Config conf){
        HashMap<Integer, String> colorDefinitions = new HashMap<>();
        ConfigObject biomes = conf.getObject(BIOMS_PATH);

        for(String biom : biomes.unwrapped().keySet()){

            String hexColor = conf.getString(BIOMS_PATH+"."+biom+".color");
            int color = Integer.parseInt(hexColor, 16);
            colorDefinitions.put(color, biom);
        }
        return colorDefinitions;
    }
}