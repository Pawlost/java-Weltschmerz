package com.ritualsoftheold.weltschmerz.maps.fortune;

import com.ritualsoftheold.weltschmerz.landmass.Graph;
import com.ritualsoftheold.weltschmerz.landmass.fortune.Fortune;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Edge;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.SimpleTimeZone;
import java.util.concurrent.ThreadLocalRandom;

public class Canvas extends JPanel {
    private BufferedImage image;
    private int size;
    private Graph graph;

    Canvas(int size) {
        this.image = new BufferedImage(size , size, BufferedImage.TYPE_INT_ARGB);
        this.size = size;
    }

    void updateImage() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Point[] points = new Point[size];
        for(int i=0; i < points.length; i++){
            double x = random.nextDouble(1, (double) points.length);
            double y = random.nextDouble(1, (double) points.length);
            if(x > 150.0  || y > 150.0){
                System.out.println(x +" "+ y);
            }
            points[i] = new Point(x, y);
        }
        graph = Fortune.ComputeVoronoiGraph(points);
        for(Point point:graph.getVertexArray()){
            if(point.getY() > 0 && point.getX() > 0 && point.getX() < size && point.getY() < size) {
                image.setRGB((int) point.getX(), (int) point.getY(), new Color(0, 0, 0).getRGB());
            }
        }

        Fortune.FilterVG(graph, 2);
        Graphics g = image.getGraphics();
        g.setColor(Color.BLACK);
        for(Edge edge:graph.getEdgeArray()) {
            g.drawLine((int) edge.getVertexA().getX(), (int) edge.getVertexA().getY(),
                    (int) edge.getVertexB().getX(), (int) edge.getVertexB().getY());
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