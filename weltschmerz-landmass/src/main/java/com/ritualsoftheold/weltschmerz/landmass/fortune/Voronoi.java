package com.ritualsoftheold.weltschmerz.landmass.fortune;

import com.google.common.collect.Multimap;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Vertex;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;

import java.util.ArrayList;
import java.util.HashSet;

public class Voronoi {
    private Multimap<Centroid, Border> allborders;

    public Voronoi( Multimap<Centroid, Border> map) {
        allborders = map;
    }

    public void getVoronoiArea(ArrayList<Location> locations, int width, int height) {
        for (Location location : locations) {
            for(Border border:allborders.get(location.getCentroid())) {
                if (border.getVertexA().getX() < width && border.getVertexA().getY() < height &&
                        border.getVertexB().getX() < width && border.getVertexB().getY() < height &&
                        border.getVertexA().getX() > 0 && border.getVertexA().getY() > 0 &&
                        border.getVertexB().getX() > 0 && border.getVertexB().getY() > 0) {
                    location.add(border);
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
            }
        }
    }
}
