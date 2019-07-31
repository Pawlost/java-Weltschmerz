package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class MapIO {
    public static String outputFile= "map";
    public static String configFile = "config";
    public static String simulationFile = "simulation";

    //Save image into file
    public static void saveImage(BufferedImage image){
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

    //Save current simulation state into file
    public void saveCurrentState(Location[] locations, Configuration conf){
        File file = new File("./"+simulationFile+".conf");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Config config = ConfigFactory.empty();
        config.withValue("seed", ConfigValueFactory.fromAnyRef(conf.seed));
    }

    // Load our own config values from the default location
    public static Configuration loadMapConfig(){
        Config conf = ConfigFactory.load(configFile);
        return ConfigParser.parseConfig(conf);
    }
}
