package com.ritualsoftheold.weltschmerz.maps.precipitation;

import com.ritualsoftheold.weltschmerz.core.MapIO;
import com.ritualsoftheold.weltschmerz.core.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class WorldPrecipitationCanvas extends JPanel implements Scrollable, ActionListener {

    private BufferedImage image;
    private int width;
    private int height;
    private World world;
    private final static int PRECIPITATION = 400;

    public WorldPrecipitationCanvas(int width, int height, World world) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.width = width;
        this.height = height;
        this.world = world;
    }

    public void updateImage() {
        int width = this.image.getWidth();
        int height = this.image.getHeight();

       /* for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float humidity = (float) world.getPrecipitation(x, y)/PRECIPITATION;
                this.image.setRGB(x, y, new Color((float) Math.abs(humidity), (float) Math.abs(humidity), (float) Math.abs(humidity)).getRGB());
            }
        }

        */

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

