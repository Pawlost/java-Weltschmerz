package com.ritualsoftheold.weltschmerz.maps.fortune;

import com.ritualsoftheold.weltschmerz.landmass.Area;
import com.ritualsoftheold.weltschmerz.landmass.Graph;
import com.ritualsoftheold.weltschmerz.landmass.algorithms.Fortune;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Centroid;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

public class Canvas extends JPanel {
    private BufferedImage image;
    private int size;

    Canvas(int size) {
        this.image = new BufferedImage(size , size, BufferedImage.TYPE_INT_ARGB);
        this.size = size;
    }

    void updateImage() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        Graphics g = image.getGraphics();
        g.setColor(Color.BLACK);

        Area[] areas = new Area[size];
        Centroid[] centroids = new Centroid[areas.length];
        for(int i = 0; i < size; i++){
            double x = random.nextDouble(1, (double) areas.length);
            double y = random.nextDouble(1, (double) areas.length);
            Area area = new Area(x, y);
            centroids[i] = area.getCentroid();
            areas[i] = area;
        }

/*
        for(Vertex vertex : vertices){
            if(vertex.getY() > 0 && vertex.getX() > 0 && vertex.getX() < size && vertex.getY() < size) {
                g.drawOval((int) vertex.getX() - 2, (int) vertex.getY() - 2, 2, 2);
            }
        }*/

        Graph graph = Fortune.ComputeAreas(centroids);
        graph.getVoronoiArea(areas);

        for(Area polygon: areas){
            for(Border border:polygon.getBorders()){
                System.out.println(border.getVertexA().getX()+" "+border.getVertexA().getY());
                System.out.println(border.getVertexB().getX()+" "+border.getVertexB().getY());
                g.drawLine((int) border.getVertexA().getX(), (int) border.getVertexA().getY(),
                        (int) border.getVertexB().getX(), (int) border.getVertexB().getY());
            }
        }

       /* for (int i = 0; i < 4; i++){
            DataEvent vertex = voronoi.getDataNode()[i];
            g.drawOval((int) vertex.getX() - 2, (int) vertex.getY() - 2, 2, 2);
        }


        for(Vertex vertex : voronoi.getVertexArray()){
            if(vertex.getY() > 0 && vertex.getX() > 0 && vertex.getX() < size && vertex.getY() < size) {
                g.drawOval((int) vertex.getX() - 2, (int) vertex.getY() - 2, 2, 2);
            }
        }


        g.setColor(Color.BLACK);
        for(Border border : voronoi.getEdgeArray()) {

           g.drawLine((int) border.getDatumA().getX(), (int) border.getDatumA().getY(),
                    (int) border.getDatumB().getX(), (int) border.getDatumB().getY());
        }*/
        this.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.image, null, null);
        g2.dispose();
    }
}