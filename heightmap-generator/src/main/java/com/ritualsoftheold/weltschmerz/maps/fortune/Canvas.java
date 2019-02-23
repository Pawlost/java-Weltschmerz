package com.ritualsoftheold.weltschmerz.maps.fortune;

import com.ritualsoftheold.weltschmerz.core.World;
import com.ritualsoftheold.weltschmerz.landmass.Location;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel {
    private BufferedImage image;
    Polygon polygon = new Polygon();
    private World world;
    private int size;
    private Location[] locations;

    public Canvas(int size, World world) {
        this.image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        this.world = world;
        locations = world.generateLand();
        this.size = size;
    }

    public void paintWorld() {
        drawWorld(locations);
    }

    public void paintOnce(int index) {

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

        System.out.println(border.getVertexA().getX() + " " + border.getVertexA().getY());
        System.out.println(border.getVertexB().getX() + " " + border.getVertexB().getY());

        this.repaint();
    }

    public void changeWorld() {
        drawWorld(world.reshapeWorld(0));
    }

    private void fillPolygon(Location location) {
        Graphics g = image.getGraphics();

        g.setColor(Color.GREEN);

        Polygon polygon = new Polygon();
        for (Vertex vertex : location.getVertice()) {
            polygon.addPoint((int) vertex.getX(), (int) vertex.getY());
            System.out.println((int) vertex.getX() +" "+ (int) vertex.getY());
        }

        System.out.println("-----------END----------");
        g.fillPolygon(polygon);

        this.repaint();
    }


    public void fillPart(int index) {
        Graphics g = image.getGraphics();

        g.setColor(Color.GREEN);

        Vertex vertex  = locations[0].getVertice()[index];
        polygon.addPoint((int) vertex.getX(), (int) vertex.getY());
        g.fillPolygon(polygon);

        this.repaint();
    }

    public void fillWorld() {
        for (Location location : locations) {
            fillPolygon(location);
        }
    }

    public void fillOnce(int index) {
        fillPolygon(locations[index]);
    }

    private void drawWorld(Location[] locations) {

        image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

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
}
