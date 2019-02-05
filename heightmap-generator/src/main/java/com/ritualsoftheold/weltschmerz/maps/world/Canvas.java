package com.ritualsoftheold.weltschmerz.maps.world;

import com.ritualsoftheold.weltschmerz.core.Shape;
import com.ritualsoftheold.weltschmerz.experimental.Lithosphere;
import com.sudoplay.joise.module.ModuleAutoCorrect;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel {
    private BufferedImage image;

    Canvas(int size) {
        this.image = new BufferedImage(size , size, BufferedImage.TYPE_INT_ARGB);
    }

    void updateImage(double[][] map) {
        for (int y = 0; y < map.length; y++) {
            for(int x = 0; x < map[y].length; x++) {
                double f = map[y][x];
                float r = (float) f;
                System.out.println(r);
                if (f < Shape.SEA.min) {
                    image.setRGB(x, y, new Color(0f, 0f, 0.25f).getRGB());
                } else if( f > Shape.SEA.min && f < Shape.SEA.max) {
                    r -= Shape.SEA.min;
                    image.setRGB(x, y, new Color(0f, 0f, 1.0f, r).getRGB());
                }else if ( f > Shape.SHORELINE.min && f < Shape.SHORELINE.max) {
                    r -= Shape.SHORELINE.min;
                    image.setRGB(x, y, new Color(1.0f, 1.0f, 0, r).getRGB());
                } else if (f > Shape.PLAINS.min && f < Shape.PLAINS.max) {
                    r -= Shape.PLAINS.min;
                    image.setRGB(x, y, new Color(0, 1.0f, 0, r).getRGB());
                } else if (f > Shape.HILLS.min && f < Shape.HILLS.max) {
                    r -= Shape.HILLS.min;
                    image.setRGB(x, y, new Color(1.0f, 0.5f, 0, r).getRGB());
                } else if (f > Shape.MOUNTAINS.min) {
                    r -= Shape.MOUNTAINS.min;
                    image.setRGB(x, y, new Color(0.38f, 0.38f, 0.38f, r).getRGB());
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