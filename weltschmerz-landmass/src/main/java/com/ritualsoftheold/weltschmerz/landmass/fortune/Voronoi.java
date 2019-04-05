package com.ritualsoftheold.weltschmerz.landmass.fortune;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Vertex;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class Voronoi {
    private Multimap<Centroid, Border> allborders;

    public Voronoi( Multimap<Centroid, Border> map) {
        allborders = map;
    }

    public void getVoronoiArea(ArrayList<Location> locations, int width, int height) {
        HashMap<Centroid, Location> neighbors = new HashMap<>();
        for (Location location : locations) {
            for(Border border:allborders.get(location.getCentroid())) {
                if (border.getVertexA().getX() > width)
                {
                    border.setVertexA(new Vertex(width, border.getVertexA().getY()));
                }

                if (border.getVertexA().getY() > height)
                {
                    border.setVertexA(new Vertex(border.getVertexA().getX(), height));
                }

                if (border.getVertexB().getX() > width)
                {
                    border.setVertexB(new Vertex(width, border.getVertexB().getY()));
                }

                if (border.getVertexB().getY() > height)
                {
                    border.setVertexB(new Vertex(border.getVertexB().getX(), height));
                }

                if (border.getVertexA().getX() < 0)
                {
                    border.setVertexA(new Vertex(0, border.getVertexA().getY()));
                }

                if (border.getVertexA().getY() < 0)
                {
                    border.setVertexA(new Vertex(border.getVertexA().getX(), 0));
                }

                if (border.getVertexB().getX() < 0)
                {
                    border.setVertexB(new Vertex(0, border.getVertexB().getY()));
                }

                if (border.getVertexB().getY() < 0)
                {
                    border.setVertexB(new Vertex(border.getVertexB().getX(), 0));
                }
                location.add(border);
            }
        }

        for (Location location : locations) {
            location.circularize();
            neighbors.put(location.getCentroid(), location);
        }

        for(Location location:locations){
            for(Centroid centroid:location.getNearCentroids()){
                try {
                    neighbors.get(centroid).addNeighbor(location);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        }

        ArrayList<Location> cloneLocation = new ArrayList<>(locations);
        for (Location location : cloneLocation) {
            if (location.getBorders().length <= 2) {
                locations.remove(location);
            }
        }
    }
}
