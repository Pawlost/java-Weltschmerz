package com.ritualsoftheold.weltschmerz.maps.plates;

import com.ritualsoftheold.weltschmerz.plates.Lithosphere;
import com.sudoplay.joise.module.ModuleAutoCorrect;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel {
    private static final float SCALE = 1.0f;
    private BufferedImage image;
    private Lithosphere world;
    private int map_side;

    Canvas(int size, Lithosphere world) {
        this.image = new BufferedImage(size , size, BufferedImage.TYPE_INT_ARGB);
        this.world = world;
        map_side = size;
    }

    void updateImage() {
        float[] map = world.getTopography();
        for(float f:map){
            System.out.println(f);
        }
        // Satellite color map:
        // 0.0:   0,   0,  64
        // 0.5:   0,   0, 255
        // 1.0:   0, 255, 255
        //
        // 0.0:   0, 128,   0
        // 1.0:   0, 255,   0
        // 1.5: 255, 255,   0
        // 2.0: 255, 128,   0
        // 3.0  128,  64,   0
        // 5.0:  96,  96,  96
        // 8.0: 255, 255, 255 // Snow capped mountains
        int index = 0;
        for (int y = 0; y < map_side; y++) {
            for(int x = 0; x < map_side; x++){
                float f = map[index];
                  if (f < 0.5f){
                      image.setRGB(x, y, new Color(0 , 0, 64).getRGB());
                  }else if(f < 1.0){
                      image.setRGB(x, y, new Color(0 , 0, 255).getRGB());
                  }else{
                      f -= 1.0f;
                      if(f < 1.0f){
                          image.setRGB(x, y, new Color(0 , 128, 0).getRGB());
                      }else if(f < 1.5f){
                          image.setRGB(x, y, new Color(0 , 255, 0).getRGB());
                      }else if(f < 2.0){
                          image.setRGB(x, y, new Color(255 , 255, 0).getRGB());
                      }else if(f < 3.0){
                          image.setRGB(x, y, new Color(255 , 128, 0).getRGB());
                      }else if(f < 5.0){
                          image.setRGB(x, y, new Color(96 , 96, 96).getRGB());
                      }else{
                          image.setRGB(x, y, new Color(255, 255, 255).getRGB());
                      }
                  }
                index++;
            }
        }
        this.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.image, null, null);
        g2.dispose();
    }

    private void update() {
            if (world.getPlateCount() == 0) {
                //TODO exeption
            }

            world.update();
        }
}