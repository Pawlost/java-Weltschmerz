package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.Location;
import com.ritualsoftheold.weltschmerz.landmass.Voronoi;
import com.ritualsoftheold.weltschmerz.landmass.algorithms.Fortune;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Centroid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class World extends ArrayList<Location> {
    private int size;
    private HashSet<Location> locations;

    public World(int density, double spread){
        this.size = (int) spread;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        locations = new HashSet<>();
        for(int i = 0; i < density; i++){
            double x = random.nextDouble(1, spread);
            double y = random.nextDouble(1, spread);
            Location location = new Location(x, y);
            locations.add(location);
        }
    }

    public Location[] generateLand(){
        ArrayList<Centroid> centroids = new ArrayList<>();

        for(Location location : locations){
            centroids.add(location.getCentroid());
        }

        Centroid[] centers = new Centroid[centroids.size()];
        centroids.toArray(centers);

        Voronoi voronoi = Fortune.ComputeGraph(centers);

        voronoi.getVoronoiArea(locations);

        Location[] copy = new Location[locations.size()];
        locations.toArray(copy);
        return copy;
    }

    public Location[] reshapeWorld(int relaxation){
        for(int i = 0; i <= relaxation; i++) {
            for (Location location : locations) {
                location.reset();
            }
        }
        return generateLand();
    }
}