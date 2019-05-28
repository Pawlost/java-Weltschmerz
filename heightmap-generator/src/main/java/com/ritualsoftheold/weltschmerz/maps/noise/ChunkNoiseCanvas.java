package com.ritualsoftheold.weltschmerz.maps.noise;

import com.ritualsoftheold.weltschmerz.noise.Shape;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.noise.generator.ChunkNoise;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ChunkNoiseCanvas extends JPanel {
    private BufferedImage image;

    public ChunkNoiseCanvas(int width, int height) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public void updateImage() {
        ChunkNoise noise = new ChunkNoise(1635, 0, 1);
        int width = this.image.getWidth();
        int height = this.image.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float c = (float) (noise.getNoise(x, y));
                this.image.setRGB(x, y, new Color(c, c, c).getRGB());
            }
        }

        //MapIO.saveHeightmap(image);
        this.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.image, null, null);
        g2.dispose();
    }
}
