package com.ritualsoftheold.weltschmerz.maps.noise;

import com.ritualsoftheold.weltschmerz.noise.generators.ChunkNoise;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ChunkNoiseCanvas extends JPanel implements Scrollable {
    private BufferedImage image;
    private int width;
    private int height;

    public ChunkNoiseCanvas(int width, int height) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.width = width;
        this.height = height;
    }

    public void updateImage() {
        ChunkNoise noise = new ChunkNoise(1635, "", 1);
        int width = this.image.getWidth();
        int height = this.image.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float c = (float) (noise.getNoise(x, y));
                this.image.setRGB(x, y, new Color(c/14, c/14, c/14).getRGB());
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

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(width, height);
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 0;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 0;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
