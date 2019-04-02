package com.ritualsoftheold.weltschmerz.core;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MapIO {
    public static String outputFile= "map";
    public static String inputFile = "config";

    public static void saveHeightmap(BufferedImage image){
        try {
            File file = new File("./"+outputFile+".png");
            if(!file.exists()) {
                file.createNewFile();
            }
            ImageIO.write(image,"png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Configuration loadMapConfig(){
        // Load our own config values from the default location, application.conf
        Configuration configuration = new Configuration();
        Config conf = ConfigFactory.load(inputFile);
        configuration.size = Integer.parseInt(conf.getString("map.size"));
        configuration.detail = Long.parseLong(conf.getString("map.detail"));
        configuration.smooth = Integer.parseInt(conf.getString("map.smooth"));

        configuration.seed = Long.parseLong(conf.getString("noise.seed"));
        configuration.octaves = Integer.parseInt(conf.getString("noise.octaves"));
        configuration.frequency = Float.parseFloat(conf.getString("noise.frequency"));


        configuration.volcanoes = Integer.parseInt(conf.getString("elevation.volcanoes"));
        configuration.tectonicPlates = Integer.parseInt(conf.getString("elevation.tectonicPlates"));
        configuration.hills = Integer.parseInt(conf.getString("elevation.hills"));
        configuration.tectonicMovement = Integer.parseInt(conf.getString("elevation.tectonicMovement"));

        return configuration;
    }
}
