package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.land.Legend;
import com.ritualsoftheold.weltschmerz.noise.Shape;
import com.typesafe.config.Config;

import java.util.HashMap;

public class ConfigParser {
    public static Configuration parseConfig(Config conf){
        Configuration configuration = new Configuration();
        configuration.width = Integer.parseInt(conf.getString("map.width"));
        configuration.height = Integer.parseInt(conf.getString("map.height"));
        configuration.detail = Long.parseLong(conf.getString("map.detail"));
        configuration.smooth = Integer.parseInt(conf.getString("map.smooth"));

        configuration.seed = Long.parseLong(conf.getString("noise.seed"));
        configuration.octaves = Integer.parseInt(conf.getString("noise.octaves"));
        configuration.frequency = Float.parseFloat(conf.getString("noise.frequency"));


        configuration.volcanoes = Integer.parseInt(conf.getString("elevation.volcanoes"));
        configuration.tectonicPlates = Integer.parseInt(conf.getString("elevation.tectonicPlates"));
        configuration.tectonicMovement = Integer.parseInt(conf.getString("elevation.tectonicMovement"));
        configuration.tectonicMovement = Integer.parseInt(conf.getString("elevation.islandSize"));

        configuration.shapes = parseShape(conf);
        return configuration;
    }

    private static HashMap<String, Shape> parseShape(Config config){
        HashMap<String, Shape> shapes = new HashMap<>();
        Shape shape = new Shape();
        shape.min = 0;
        shape.max = config.getDouble("level.OCEAN");
        shape.color = Legend.OCEAN.color;
        shape.land = false;
        shape.position = 0;
        shape.key ="OCEAN";
        shapes.put("OCEAN", shape);

        shape = new Shape();
        shape.min = shapes.get("OCEAN").max;
        shape.max = config.getDouble("level.SEA");
        shape.color = Legend.SEA.color;
        shape.land = false;
        shape.position = 1;
        shape.key ="SEA";
        shapes.put("SEA", shape);

        shape = new Shape();
        shape.min = shapes.get("SEA").max;
        shape.max = config.getDouble("level.SHORELINE");
        shape.color = Legend.SHORELINE.color;
        shape.land = true;
        shape.position = 2;
        shape.key ="SHORELINE";
        shapes.put("SHORELINE", shape);

        shape = new Shape();
        shape.min = shapes.get("SHORELINE").max;
        shape.max = config.getDouble("level.PLAIN");
        shape.color = Legend.PLAIN.color;
        shape.land = true;
        shape.position = 3;
        shape.key ="PLAIN";
        shapes.put("PLAIN", shape);

        shape = new Shape();
        shape.min = shapes.get("PLAIN").max;
        shape.max = config.getDouble("level.HILL");
        shape.color = Legend.HILL.color;
        shape.land = true;
        shape.position = 4;
        shape.key ="HILL";
        shapes.put("HILL", shape);

        shape = new Shape();
        shape.min = shapes.get("HILL").max;
        shape.max = config.getDouble("level.MOUNTAIN");
        shape.color = Legend.MOUNTAIN.color;
        shape.land = true;
        shape.position = 5;
        shape.key ="MOUNTAIN";
        shapes.put("MOUNTAIN", shape);

        shape = new Shape();
        shape.min = shapes.get("HILL").max;
        shape.max = config.getDouble("level.MOUNTAIN");
        shape.color = Legend.VOLCANO.color;
        shape.land = true;
        shape.position = 5;
        shape.key ="VOLCANO";
        shapes.put("VOLCANO", shape);

        return shapes;
    }
}
