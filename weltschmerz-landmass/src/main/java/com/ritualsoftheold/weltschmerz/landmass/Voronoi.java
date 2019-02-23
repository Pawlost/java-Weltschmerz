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

    public void getVoronoiArea(HashSet<Location> locations, int width, int height) {
        for (Location location : locations) {
            for (Border border : getBorderArray()) {
                if(border.getVertexA().getX() < width && border.getVertexA().getY() < height &&
                        border.getVertexB().getX() < width && border.getVertexB().getY() < height &&
                        border.getVertexA().getX() > 0 && border.getVertexA().getY() > 0 &&
                        border.getVertexB().getX() > 0 && border.getVertexB().getY() > 0) {
                    if (border.getDatumA() == location.getCentroid() || border.getDatumB() == location.getCentroid()) {
                        location.add(border);
                    }
                }else{
                    voronoiBorders.remove(border);
                }
            }
        }

        optimalizeLocations(locations);

        for (Location location : locations) {
            location.circularize();
        }

        optimalizeLocations(locations);
    }

    private void optimalizeLocations(HashSet<Location> locations){
        Location[] locationsCopy = new Location[locations.size()];
        locations.toArray(locationsCopy);

        for (Location location : locationsCopy) {
            if (location.getBorders().length < 2) {
                locations.remove(location);
            }
        }
    }
}
