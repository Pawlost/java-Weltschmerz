package com.ritualsoftheold.weltschmerz.maps.circulation;

import com.ritualsoftheold.weltschmerz.core.MapIO;
import com.ritualsoftheold.weltschmerz.core.World;
import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.geometry.units.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class WorldCirculationCanvas  extends JPanel implements Scrollable {

    private static final float SCALE = 1.0f;
    private BufferedImage image;
    private int width;
    private int height;

    public WorldCirculationCanvas(int width, int height) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.width = width;
        this.height = height;
    }

    public void updateImage(World world) {
        int width = this.image.getWidth();
        int height = this.image.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float flow = (float) world.getPressure(x, y)/20;
               /* double valueX = flow.x;
                double valueY = flow.y;
                if (valueX > 1 || valueX < -1) {
                    valueX = 1;
                }

                if(valueY > 1 || valueY < -1) {
                    valueY = 1;
                }*/
                this.image.setRGB(x, y, new Color((float) Math.abs(flow), (float) Math.abs(1.0), (float) Math.abs(flow)).getRGB());
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

