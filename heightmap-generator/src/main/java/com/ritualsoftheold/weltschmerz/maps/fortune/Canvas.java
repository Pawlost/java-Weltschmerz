package com.ritualsoftheold.weltschmerz.maps.fortune;

import com.ritualsoftheold.weltschmerz.core.World;
import com.ritualsoftheold.weltschmerz.landmass.Location;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Border;
import com.sudoplay.joise.module.ModuleAutoCorrect;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel {
    private BufferedImage image;
    private World world;
    private int size;
    private Location[] locations;
    private ModuleAutoCorrect module;

    public Canvas(int size, World world, ModuleAutoCorrect module) {
        this.image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        this.world = world;
        locations = world.generateLand();
        this.size = size;
        this.module = module;
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

        this.repaint();
    }

    public void reshapeWorld() {
        image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        locations = world.reshapeWorld();
        fillWorld();
        drawWorld();
    }

    private void fillPolygon(Location location) {
        Graphics g = image.getGraphics();

        g.setColor(location.setShape(module, 1));

        g.fillPolygon(location.getPolygon());

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
}
