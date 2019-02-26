package com.ritualsoftheold.weltschmerz.maps.world;

import com.ritualsoftheold.weltschmerz.core.World;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.land.Plate;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TectonicCanvas extends JPanel{
        private BufferedImage image;
        private World world;
        private int size;

        public TectonicCanvas(int size, World world) {
            this.image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            this.world = world;
            this.size = size;
            super.setAlignmentX(size);;
        }

        public void drawWorld() {

            Graphics g = image.getGraphics();

            g.setColor(Color.BLACK);

            for (Plate polygon : world.getPlates()) {
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
