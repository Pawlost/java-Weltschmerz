package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.Configuration;
import com.ritualsoftheold.weltschmerz.landmass.Shape;
import com.typesafe.config.Config;

import java.awt.*;
import java.util.HashMap;

public class ConfigParser {
    public static Configuration parseConfig(Config conf){
        Configuration configuration = new Configuration();
        configuration.width = conf.getInt("map.width");
        configuration.height = conf.getInt("map.height");

        configuration.seed = conf.getLong("noise.seed");
        configuration.octaves = conf.getInt("noise.octaves");
        configuration.frequency = conf.getDouble("noise.frequency");
        configuration.samples = conf.getInt("noise.samples");


        configuration.volcanoes = conf.getInt("elevation.volcanoes");
        configuration.tectonicPlates = conf.getInt("elevation.tectonicPlates");
        configuration.islandSize = conf.getInt("elevation.mountainLenght");

        configuration.shapes = parseShape(conf);
        return configuration;
    }

    private static HashMap<String, Shape> parseShape(Config config){
        HashMap<String, Shape> shapes = new HashMap<>();
        Shape shape = new Shape(0, config.getDouble("level.OCEAN"),
                false,"OCEAN", Color.BLUE);
        shapes.put("OCEAN", shape);

        shape = new Shape(shapes.get("OCEAN").max, config.getDouble("level.SEA"),
                false,"SEA", Color.CYAN);
        shapes.put("SEA", shape);

        shape = new Shape(shapes.get("SEA").max, config.getDouble("level.SHORELINE"),
                true,"SHORELINE", Color.YELLOW);
        shapes.put("SHORELINE", shape);

        shape = new Shape(shapes.get("SHORELINE").max, config.getDouble("level.PLAIN"),
                true,"PLAIN", Color.GREEN);
        shapes.put("PLAIN", shape);

        shape = new Shape(shapes.get("PLAIN").max, config.getDouble("level.HILL"),
                true,"HILL", Color.ORANGE);
        shapes.put("HILL", shape);

        shape =  new  Shape(shapes.get("HILL").max, config.getDouble("level.MOUNTAIN"),
                true,"MOUNTAIN", Color.GRAY);
        shapes.put("MOUNTAIN", shape);

        shape = new  Shape(shapes.get("HILL").max, config.getDouble("level.MOUNTAIN"),
                true,"VOLCANO", Color.RED);
        shapes.put("VOLCANO", shape);

        return shapes;
    }
}
