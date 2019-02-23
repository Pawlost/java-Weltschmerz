package com.ritualsoftheold.weltschmerz.landmass;

import com.ritualsoftheold.weltschmerz.landmass.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Vertex;

import java.util.HashSet;

public class Voronoi {
    private HashSet<Border> voronoiBorders;
    private HashSet<Vertex> voroniVertices;

    public Voronoi(HashSet<Border> e) {
        voroniVertices = null;
        voronoiBorders = e;
        listVertices();
    }

    public Border[] getBorderArray() {
        Border[] vEdges = new Border[voronoiBorders.size()];
        voronoiBorders.toArray(vEdges);
        return vEdges;
    }

    private void listVertices() {
        voroniVertices = new HashSet<>();
        for (Border VE : voronoiBorders) {
            voroniVertices.add(VE.getVertexA());
            voroniVertices.add(VE.getVertexB());
        }
    }

    public void getVoronoiArea(HashSet<Location> locations) {
        for (Location location : locations) {
            for (Border border : getBorderArray()) {
                if (border.getDatumA() == location.getCentroid() || border.getDatumB() == location.getCentroid()) {
                    location.add(border);
                }
            }
        }
        /*Location[] copy = new Location[locations.size()];
        locations.toArray(copy);
        while (voronoiBorders.size() > 0) {
            System.out.println("here");
            for (Border border : getBorderArray()) {
                for (Location location : copy) {
                /*if(border.getVertexA().getX() > width || border.getVertexA().getY() > height || border.getVertexB().getX() > width
                        || border.getVertexB().getY() > height || border.getVertexA().getX() < width || border.getVertexA().getY() < height
                        || border.getVertexB().getX() < width || border.getVertexB().getY() < height){
                    voronoiBorders.remove(border);
                }else
                    if (border.getDatumA() != location.getCentroid() && border.getDatumB() == location.getCentroid()) {
                        location.add(border);
                    } else if (border.getDatumB() == location.getCentroid() && border.getDatumB() != location.getCentroid()) {
                        location.add(border);
                    }
                }
                voronoiBorders.remove(border);
            }
        }*/
        Location[] locationsCopy = new Location[locations.size()];
        locations.toArray(locationsCopy);

        for (Location location : locationsCopy) {
            if (location.getBorders().length < 1) {
                locations.remove(location);
            }
        }

        for (Location location : locationsCopy) {
               location.circularize();
            }
        for (Location location : locationsCopy) {
            if (location.getBorders().length <= 2) {
                locations.remove(location);
            }
        }
    }
}
