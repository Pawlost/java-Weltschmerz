package com.ritualsoftheold.weltschmerz.maps.pressure;

import com.ritualsoftheold.weltschmerz.core.MapIO;
import com.ritualsoftheold.weltschmerz.core.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class WorldPressureCanvas extends JPanel implements Scrollable, ActionListener {

    private static final float SCALE = 1.0f;
    private BufferedImage image;
    private int width;
    private int height;
    private World world;

    public WorldPressureCanvas(int width, int height, World world) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.width = width;
        this.height = height;
        this.world = world;
    }

    public void updateImage() {
        int width = this.image.getWidth();
        int height = this.image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float pressure = (float)world.getPressure(x, y)/world.conf.pressure;
                this.image.setRGB(x, y, new Color((float) Math.abs(pressure), (float) Math.abs(pressure), (float) Math.abs(pressure)).getRGB());
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

    @Override
    public void actionPerformed(ActionEvent e) {
        updateImage();
    }
}

