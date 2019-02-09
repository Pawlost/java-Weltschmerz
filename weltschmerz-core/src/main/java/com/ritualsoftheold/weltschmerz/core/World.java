package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.Location;
import com.ritualsoftheold.weltschmerz.landmass.Voronoi;
import com.ritualsoftheold.weltschmerz.landmass.algorithms.Fortune;
import com.ritualsoftheold.weltschmerz.landmass.geometry.Centroid;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class World extends ArrayList<Location> {
    private int size;
    private Location[] locations;

    public World(int size){
        this.size = size;

        ThreadLocalRandom random = ThreadLocalRandom.current();
        locations = new Location[size];
        for(int i = 0; i < size; i++){
            double x = random.nextDouble(1, (double) locations.length);
            double y = random.nextDouble(1, (double) locations.length);
            Location location = new Location(x, y);
            locations[i] = location;
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
        return locations;
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