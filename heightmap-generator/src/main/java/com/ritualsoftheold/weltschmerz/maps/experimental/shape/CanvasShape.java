package com.ritualsoftheold.weltschmerz.maps.experimental.shape;

import com.ritualsoftheold.weltschmerz.experimental.Calculations;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CanvasShape extends JPanel {
    private BufferedImage image;
    private int size;

    CanvasShape(int size) {
        this.size = size - 1;
        this.image = new BufferedImage(size , size, BufferedImage.TYPE_INT_ARGB);
    }

    void updateImage() {
        float[] tmp = new float[size * size];
        tmp = Calculations.sqrdmd(tmp, size - 1, 0.5f);

        System.out.println(size);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                   float  f = tmp[x];
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

}