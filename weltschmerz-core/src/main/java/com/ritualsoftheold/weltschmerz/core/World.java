package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.fortune.Voronoi;
import com.ritualsoftheold.weltschmerz.landmass.fortune.algorithms.Fortune;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Centroid;
import com.sudoplay.joise.module.ModuleAutoCorrect;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class World extends ArrayList<Location> {
    private int size;
    private static final int DETAIL = 1;
    private HashSet<Location> locations;
    private ArrayList<Centroid> centroids;
    private ModuleAutoCorrect module;

    public World(int density, double spread, ModuleAutoCorrect module){
        this.size = (int) spread;
        this.module = module;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        locations = new HashSet<>();
        centroids = new ArrayList<>();

        for(int i = 0; i < density; i++){
            double x = random.nextDouble(1, spread);
            double y = random.nextDouble(1, spread);
            Location location = new Location(x, y);
            centroids.add(location.getCentroid());
            locations.add(location);
        }
    }

    public Location[] generateLand(){
        Centroid[] centers = new Centroid[centroids.size()];
        centroids.toArray(centers);

        Voronoi voronoi = Fortune.ComputeGraph(centers);

        voronoi.getVoronoiArea(locations, size, size);

        for(Location location:locations){
            location.setLand(module, DETAIL);
        }

        createShoreline();

        Location[] copy = new Location[locations.size()];
        locations.toArray(copy);
        return copy;
    }

    public Location[] reshapeWorld(){
        for (Location location : locations) {
            for (int i = 0; i < centroids.size(); i++) {
                if (location.getCentroid() == centroids.get(i)) {
                    location.reset();
                    centroids.set(i, location.getCentroid());
                }
            }
        }

        return generateLand();
    }

    public Location[] reverse(){
        for(Location location:locations){
            location.setLand(!location.isLand());
        }

        createShoreline();

        Location[] copy = new Location[locations.size()];
        locations.toArray(copy);
        return copy;
    }

    private void createShoreline(){
        for(Location location: locations){
            Centroid[] centroids = location.getNeighbors();
            for(Location next:locations){
                for(Centroid centroid:centroids) {
                    if (next.getCentroid() == centroid) {
                        if(next.isLand() != location.isLand()){
                            if(location.isLand()) {
                                location.setShape(Shape.SHORELINE);
                            }else{
                                location.setShape(Shape.SEA);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}