package com.ritualsoftheold.weltschmerz.maps.world;

import com.ritualsoftheold.weltschmerz.core.World;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.land.Plate;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class TectonicCanvas extends JPanel{
        private BufferedImage image;
        private World world;
        private int size;
        private Location location;

        public TectonicCanvas(int size, World world) {
            this.image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            this.world = world;
            this.size = size;
        }

        public void drawWorld() {
            Graphics g = image.getGraphics();

            g.setColor(Color.BLACK);

            for (Plate plate : world.getPlates()) {
                for (Border border : plate.getBorders()) {
                    g.drawLine((int) border.getVertexA().getX(), (int) border.getVertexA().getY(),
                            (int) border.getVertexB().getX(), (int) border.getVertexB().getY());
                }
            }

            this.repaint();
        }

    public void drawPart(int index) {
        Graphics g = image.getGraphics();

        g.setColor(Color.BLACK);

        Plate plate = world.getPlates().get(0);
        Border border = plate.getBorders().get(index);

        g.drawLine((int) border.getVertexA().getX(), (int) border.getVertexA().getY(),
                        (int) border.getVertexB().getX(), (int) border.getVertexB().getY());

        this.repaint();
    }


        public void fill(){
            Graphics g = image.getGraphics();

            Random rand = new Random();

            for (Plate plate : world.getPlates()) {
                float r = rand.nextFloat();
                float z = rand.nextFloat();
                float b = rand.nextFloat();
                g.setColor(new Color(r, z, b));
                g.fillPolygon(plate.getPolygon());
            }
            drawWorld();
            repaint();
        }

        public void fillOnce(int index) {

            Graphics g = image.getGraphics();

            g.setColor(Color.BLACK);

            Random rand = new Random();

            Plate plate = world.getPlates().get(0);
            float r = rand.nextFloat();
            float z = rand.nextFloat();
            float b = rand.nextFloat();
            g.setColor(new Color(r, z, b));
            location = plate.getLocations().get(index);
            g.fillPolygon(location.getPolygon());

            drawWorld();

            this.repaint();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(this.image, null, null);
            g2.dispose();
        }
}
