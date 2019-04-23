package com.ritualsoftheold.weltschmerz.maps.noise;

import com.ritualsoftheold.weltschmerz.landmass.Shape;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.noise.ChunkNoise;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ChunkNoiseCanvas extends JPanel {
    private static final float SCALE = 1.0f;
    private BufferedImage image;

    public ChunkNoiseCanvas(int width, int height) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public void updateImage() {
        Location location = new Location(0, 0, 1635);
        location.setShape(new Shape(0, 1));
        ChunkNoise noise = new ChunkNoise(location);
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
