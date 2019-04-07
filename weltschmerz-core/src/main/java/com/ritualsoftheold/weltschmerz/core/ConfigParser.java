package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.noise.Configuration;
import com.ritualsoftheold.weltschmerz.noise.Shape;
import com.typesafe.config.Config;

import java.awt.*;
import java.util.HashMap;

public class ConfigParser {
    public static Configuration parseConfig(Config conf){
        Configuration configuration = new Configuration();
        configuration.width = conf.getInt("map.width");
        configuration.height = conf.getInt("map.height");
        configuration.detail = conf.getDouble("map.detail");

        configuration.seed = conf.getLong("noise.seed");
        configuration.octaves = conf.getInt("noise.octaves");
        configuration.frequency = conf.getDouble("noise.frequency");
        configuration.samples = conf.getInt("noise.samples");


        configuration.volcanoes = conf.getInt("elevation.volcanoes");
        configuration.tectonicPlates = conf.getInt("elevation.tectonicPlates");
        configuration.tectonicMovement = conf.getInt("elevation.tectonicMovement");
        configuration.islandSize = conf.getInt("elevation.mountainLenght");

        configuration.shapes = parseShape(conf);
        return configuration;
    }

    private static HashMap<String, Shape> parseShape(Config config){
        HashMap<String, Shape> shapes = new HashMap<>();
        Shape shape = new Shape();
        shape.min = 0;
        shape.max = config.getDouble("level.OCEAN");
        shape.color = Color.BLUE;
        shape.land = false;
        shape.position = 0;
        shape.key ="OCEAN";
        shapes.put("OCEAN", shape);

        shape = new Shape();
        shape.min = shapes.get("OCEAN").max;
        shape.max = config.getDouble("level.SEA");
        shape.color = Color.CYAN;
        shape.land = false;
        shape.position = 1;
        shape.key ="SEA";
        shapes.put("SEA", shape);

        shape = new Shape();
        shape.min = shapes.get("SEA").max;
        shape.max = config.getDouble("level.SHORELINE");
        shape.color = Color.YELLOW;
        shape.land = true;
        shape.position = 2;
        shape.key ="SHORELINE";
        shapes.put("SHORELINE", shape);

        shape = new Shape();
        shape.min = shapes.get("SHORELINE").max;
        shape.max = config.getDouble("level.PLAIN");
        shape.color = Color.GREEN;
        shape.land = true;
        shape.position = 3;
        shape.key ="PLAIN";
        shapes.put("PLAIN", shape);

        shape = new Shape();
        shape.min = shapes.get("PLAIN").max;
        shape.max = config.getDouble("level.HILL");
        shape.color = Color.ORANGE;
        shape.land = true;
        shape.position = 4;
        shape.key ="HILL";
        shapes.put("HILL", shape);

        shape = new Shape();
        shape.min = shapes.get("HILL").max;
        shape.max = config.getDouble("level.MOUNTAIN");
        shape.color = Color.GRAY;
        shape.land = true;
        shape.position = 5;
        shape.key ="MOUNTAIN";
        shapes.put("MOUNTAIN", shape);

        shape = new Shape();
        shape.min = shapes.get("HILL").max;
        shape.max = config.getDouble("level.MOUNTAIN");
        shape.color = Color.RED;
        shape.land = true;
        shape.position = 6;
        shape.key ="VOLCANO";
        shapes.put("VOLCANO", shape);

        return shapes;
    }
}
