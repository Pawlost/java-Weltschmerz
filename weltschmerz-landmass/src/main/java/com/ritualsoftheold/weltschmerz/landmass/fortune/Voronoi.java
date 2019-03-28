package com.ritualsoftheold.weltschmerz.landmass.fortune;

import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Vertex;

import java.util.ArrayList;
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

    public void getVoronoiArea(ArrayList<Location> locations, int width, int height) {
        for (Border border : getBorderArray()) {
            for (Location location : locations) {
                if (border.getVertexA().getX() < width && border.getVertexA().getY() < height &&
                        border.getVertexB().getX() < width && border.getVertexB().getY() < height &&
                        border.getVertexA().getX() > 0 && border.getVertexA().getY() > 0 &&
                        border.getVertexB().getX() > 0 && border.getVertexB().getY() > 0) {
                    if (border.getDatumA() == location.getCentroid() || border.getDatumB() == location.getCentroid()) {
                        location.add(border);
                    }
                } else {
                    voronoiBorders.remove(border);
                }
            }
        }

        for (Location location : locations) {
            location.circularize();
        }

        ArrayList<Location> cloneLocation = new ArrayList<>(locations);
        for (Location location : cloneLocation) {
            if (location.getBorders().length <= 2) {
                locations.remove(location);
                for (Border border : location.getBorders()) {
                    if (border.getDatumA() == location.getCentroid()) {
                        border.setDatumA(null);
                    } else if (border.getDatumB() == location.getCentroid()) {
                        border.setDatumB(null);
                    }
                }
            }
        }
    }
}
