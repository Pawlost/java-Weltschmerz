package com.ritualsoftheold.weltschmerz.maps.world;

import com.ritualsoftheold.weltschmerz.core.MapIO;
import com.ritualsoftheold.weltschmerz.core.World;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.land.Sector;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Canvas extends JPanel {
    private BufferedImage image;
    private int width;
    private int height;
    private Location[] locations;

    public Canvas(int width, int height, World world) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        locations = world.getLocations();
        this.width = width;
        this.height = height;
    }

    private void fillRectangle(Location location) {
        Graphics g = image.getGraphics();

        g.setColor(location.getShape().color);
        Sector sector = location.getSector();
        g.fillRect(sector.x, sector.y, sector.width, sector.height);

        this.repaint();
    }

    public void fillWorld() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (Location location : locations) {
            fillRectangle(location);
        }
        MapIO.saveHeightmap(image);
    }

    public void fillOnce(int index) {
        fillRectangle(locations[index]);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.image, null, null);
        g2.dispose();
    }
}
