package com.ritualsoftheold.weltschmerz.core;

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
}
