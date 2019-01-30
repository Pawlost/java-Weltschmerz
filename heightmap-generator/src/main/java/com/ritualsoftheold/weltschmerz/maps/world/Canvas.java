package com.ritualsoftheold.weltschmerz.maps.world;

import com.ritualsoftheold.weltschmerz.core.Shape;
import com.ritualsoftheold.weltschmerz.experimental.Lithosphere;

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

                if (f < Shape.SEA.min) {
                    image.setRGB(x, y, new Color(0, 0, 64).getRGB());
                } else if( f > Shape.SEA.min && f < Shape.SEA.max) {
                    image.setRGB(x, y, new Color(0, 0, 255).getRGB());
                }else if ( f > Shape.SHORELINE.min && f < Shape.SHORELINE.max) {
                    image.setRGB(x, y, new Color(255, 255, 0).getRGB());
                } else if (f > Shape.PLAINS.min && f < Shape.PLAINS.max) {
                    image.setRGB(x, y, new Color(0, 255, 0).getRGB());
                } else if (f > Shape.HILLS.min && f < Shape.HILLS.max) {
                    image.setRGB(x, y, new Color(255, 128, 0).getRGB());
                } else if (f > Shape.MOUNTAINS.min) {
                    image.setRGB(x, y, new Color(96, 96, 96).getRGB());
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