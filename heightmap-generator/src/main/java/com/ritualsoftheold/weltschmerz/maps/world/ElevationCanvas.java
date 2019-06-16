package com.ritualsoftheold.weltschmerz.maps.world;

import com.ritualsoftheold.weltschmerz.core.MapIO;
import com.ritualsoftheold.weltschmerz.core.World;
import com.ritualsoftheold.weltschmerz.geometry.units.Border;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.geometry.units.Polygon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ElevationCanvas extends JPanel {
    private BufferedImage image;
    private int width;
    private int height;
    private ArrayList<Location> world;

    public ElevationCanvas(int width, int height, World world) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.world = new ArrayList<>(world.getLocations());
        this.width = width;
        this.height = height;
    }

    private void fillRectangle(Location location) {
        Graphics g = image.getGraphics();

        g.setColor(location.getShape().color);
        Polygon position = location.position;
        g.fillPolygon(position.getSwingPolygon());

        this.repaint();
    }

    public void drawBorders(Location location){
        Graphics g = image.getGraphics();
        g.setColor(Color.BLACK);

        for(Border border : location.position.getBorders()){

            g.drawLine((int)border.getVertexA().x, (int)border.getVertexA().y, (int)border.getVertexB().x, (int)border.getVertexB().y);
        }

        this.repaint();    }

    public void fillWorld() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (Location location : world) {
            fillRectangle(location);
            drawBorders(location);
        }
        MapIO.saveHeightmap(image);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.image, null, null);
        g2.dispose();
    }
}
