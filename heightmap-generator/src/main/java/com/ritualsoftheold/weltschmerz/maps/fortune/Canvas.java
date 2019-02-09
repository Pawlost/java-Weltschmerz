package com.ritualsoftheold.weltschmerz.maps.fortune;

import com.ritualsoftheold.weltschmerz.core.World;
import com.ritualsoftheold.weltschmerz.landmass.Location;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Centroid;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
public class Canvas extends JPanel {
    private BufferedImage image;
    private World world;
    private int size;

    public Canvas(int size, World world) {
        this.image = new BufferedImage(size , size, BufferedImage.TYPE_INT_ARGB);
        this.size = size;
        this.world = world;
    }

    public void paintWorld() {

        Location[] locations = world.generateLand();

        drawWorld(locations);

        this.repaint();
    }

    public void changeWorld() {

        Location[] locations = world.reshapeWorld(2);
        drawWorld(locations);

        this.repaint();
    }

    private void drawWorld(Location[] locations){

        image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics g = image.getGraphics();

        g.setColor(Color.BLACK);

        for(Location polygon: locations){
            for(Border border:polygon.getBorders()){
                g.drawLine((int) border.getVertexA().getX(), (int) border.getVertexA().getY(),
                        (int) border.getVertexB().getX(), (int) border.getVertexB().getY());
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.image, null, null);
        g2.dispose();
    }
}
