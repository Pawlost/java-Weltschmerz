package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.Configuration;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class MapIO {
    public static String outputFile= "map";
    public static String configFile = "config";

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
        Config conf = ConfigFactory.load(configFile);
        return ConfigParser.parseConfig(conf);
    }
}
