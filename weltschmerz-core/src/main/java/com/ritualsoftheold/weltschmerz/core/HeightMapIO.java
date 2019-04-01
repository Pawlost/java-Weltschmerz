package com.ritualsoftheold.weltschmerz.core;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HeightMapIO {
    public static void saveHeightmap(BufferedImage image, String name){
        try {
            File file = new File("./"+name+".png");
            if(!file.exists()) {
                file.createNewFile();
            }
            ImageIO.write(image,"png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadMapConfig(){
        // Load our own config values from the default location, application.conf
        Config conf = ConfigFactory.load();
        System.out.println("The answer is: " + conf.getString("simple-app.answer"));
    }
}
