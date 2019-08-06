package com.ritualsoftheold.weltschmerz.maps.humidity;

import com.ritualsoftheold.weltschmerz.core.MapIO;
import com.ritualsoftheold.weltschmerz.core.World;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class WorldHumidityCanvas extends JPanel implements Scrollable {

    private static final float SCALE = 1.0f;
    private BufferedImage image;
    private int width;
    private int height;

    public WorldHumidityCanvas(int width, int height) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.width = width;
        this.height = height;
    }

    public void updateImage(World world) {
        int width = this.image.getWidth();
        int height = this.image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float humidity = (float) world.getHumidity(x, y);
                this.image.setRGB(x, y, new Color((float) Math.abs(humidity), (float) Math.abs(humidity), (float) Math.abs(humidity)).getRGB());
            }
        }

        MapIO.saveImage(image);
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

