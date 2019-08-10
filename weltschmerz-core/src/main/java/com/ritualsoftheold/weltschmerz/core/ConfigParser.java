package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.misc.misc.Configuration;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;

import java.util.HashMap;

public class ConfigParser {
    private static final String BIOMS_PATH = "bioms";

    public static Configuration parseConfig(Config conf){
        Configuration configuration = new Configuration();
        configuration.latitude = conf.getInt("map.height");
        configuration.longitude = conf.getInt("map.width");

        configuration.seed = conf.getLong("noise.seed");
        configuration.octaves = conf.getInt("noise.octaves");
        configuration.frequency = conf.getDouble("noise.frequency");
        configuration.samples = conf.getInt("noise.samples");

        configuration.shapes = parseShape(conf);
        return configuration;
    }

    private static HashMap<Integer, String> parseShape(Config conf){
        HashMap<Integer, String> shapes = new HashMap<>();
        ConfigObject bioms = conf.getObject(BIOMS_PATH);

        for(String biom : bioms.unwrapped().keySet()){

            String hexColor = conf.getString(BIOMS_PATH+"."+biom+".color");
            int color = Integer.parseInt(hexColor, 16);
            shapes.put(color, biom);
        }
        return shapes;
    }
}
