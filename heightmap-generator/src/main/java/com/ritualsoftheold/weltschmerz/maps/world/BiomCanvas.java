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

public class BiomCanvas extends JPanel implements Scrollable {
    private BufferedImage image;
    private int width;
    private int height;
    private ArrayList<Location> world;

    public BiomCanvas(int width, int height, World world) {
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

        for(Border border : location.position.getBorders().values()){
          g.drawLine((int)border.vertexA.x, (int)border.vertexA.y, (int)border.vertexB.x, (int)border.vertexB.y);
        }

       g.setColor(Color.RED);
        g.drawOval((int)location.position.center.x, (int)location.position.center.y, 8, 8);

        this.repaint();
    }

    public void fillWorld() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (Location location : world) {
            fillRectangle(location);
            drawBorders(location);
        }
        MapIO.saveImage(image);
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
