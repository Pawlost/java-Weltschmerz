package com.ritualsoftheold.weltschmerz.maps.world;

import com.ritualsoftheold.weltschmerz.core.MapIO;
import com.ritualsoftheold.weltschmerz.core.World;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel {
    private BufferedImage image;
    private World world;
    private int width;
    private int height;
    private Location[] locations;

    public Canvas(int width, int height, World world) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.world = world;
        locations = world.getLocations();
        this.width = width;
        this.height = height;
    }

    public void drawOnce(int index) {

        Graphics g = image.getGraphics();

        g.setColor(Color.BLACK);

        Location polygon = locations[index];

        for (Border border : polygon.getBorders()) {
            g.drawLine((int) border.getVertexA().getX(), (int) border.getVertexA().getY(),
                    (int) border.getVertexB().getX(), (int) border.getVertexB().getY());
        }

        this.repaint();
    }

    public void paintPart(int index) {

        Graphics g = image.getGraphics();

        g.setColor(Color.BLACK);

        Location polygon = locations[0];

        Border border = polygon.getBorders()[index];
        g.drawLine((int) border.getVertexA().getX(), (int) border.getVertexA().getY(),
                (int) border.getVertexB().getX(), (int) border.getVertexB().getY());

        this.repaint();
    }

    private void fillPolygon(Location location) {
        Graphics g = image.getGraphics();

        g.setColor(location.getShape().color);

        g.fillPolygon(location.getPolygon());

        this.repaint();
    }

    public void fillWorld() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (Location location : locations) {
            fillPolygon(location);
        }
        MapIO.saveHeightmap(image);
        //drawWorld();
    }

    public void fillOnce(int index) {
        fillPolygon(locations[index]);
    }

    public void drawWorld() {

        Graphics g = image.getGraphics();

        g.setColor(Color.BLACK);

        for (Location polygon : locations) {
            for (Border border : polygon.getBorders()) {
                g.drawLine((int) border.getVertexA().getX(), (int) border.getVertexA().getY(),
                        (int) border.getVertexB().getX(), (int) border.getVertexB().getY());
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

    public BufferedImage getImage() {
        return image;
    }
}
