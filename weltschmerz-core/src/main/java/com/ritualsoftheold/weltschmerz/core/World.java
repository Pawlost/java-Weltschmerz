package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.land.Area;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.fortune.Voronoi;
import com.ritualsoftheold.weltschmerz.landmass.fortune.algorithms.Fortune;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.land.Plate;
import com.sudoplay.joise.module.ModuleAutoCorrect;

import java.awt.font.ShapeGraphicAttribute;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class World extends ArrayList<Location> {
    private int size;
    private static final int DETAIL = 1;
    private int volcanoes;
    private int elevation;
    private ArrayList<Location> locations;
    private ArrayList<Centroid> centroids;
    private Plate[] plates;
    private ModuleAutoCorrect module;

    public World(int scale, double spread, int volcanoes, int tectonicPlates, int elevation,
                 ModuleAutoCorrect module){
        this.size = (int) spread;
        this.module = module;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        locations = new ArrayList<>();
        centroids = new ArrayList<>();
        plates = new Plate[tectonicPlates];

        this.volcanoes = volcanoes;
        this.elevation = random.nextInt(elevation);

        for(int i = 0; i < scale; i++){
            double x = random.nextDouble(1, spread);
            double y = random.nextDouble(1, spread);
            Location location = new Location(x, y);
            centroids.add(location.getCentroid());
            locations.add(location);
        }

        for(int i = 0; i < tectonicPlates; i++){
            double x = random.nextDouble(1, spread);
            double y = random.nextDouble(1, spread);
            Plate plate = new Plate(x, y);
            plates[i] = plate;
        }

        System.out.println("Set locations");
    }

    public Location[] generateFirstLand(){
        generateLand();

        createShoreline();
        basicHills();
        createVolcanos();

        return getLocation();
    }

    private void generateLand(){
        Centroid[] copyCenters = new Centroid[centroids.size()];
        centroids.toArray(copyCenters);
        Voronoi voronoi = Fortune.ComputeGraph(copyCenters);

        ArrayList<Area> locationAreas = new ArrayList<>(locations);

        voronoi.getVoronoiArea(locationAreas, size, size);
        System.out.println("Generated borders");

        ArrayList<Location> copyLocations = new ArrayList<>(locations);
        for (Location location : copyLocations) {
            if (location.getBorders().length < 2) {
                locations.remove(location);
            }
        }

        for(Location location:locations){
            location.setLand(module, DETAIL);
        }
        System.out.println("Generated land");
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

        generateLand();
        System.out.println("done");
        return getLocation();
    }

    private Location[] getLocation() {
        Location[] copy = new Location[locations.size()];
        locations.toArray(copy);
        return copy;
    }

   /* public Location[] reverse(){
        for(Location location:locations){
            location.setLand(!location.isLand());
        }

        createShoreline();

        Location[] copy = new Location[locations.size()];
        locations.toArray(copy);
        return copy;
    }*/

    private void createShoreline() {
        for (Location location : locations) {
            for (Location next : findNeighbors(location.getNeighbors())) {
                if (next.isLand() != location.isLand()) {
                    if (location.isLand()) {
                        location.setShape(Shape.SHORELINE);
                    } else {
                        location.setShape(Shape.SEA);
                    }
                    break;
                }
            }
        }
        System.out.println("Created shoreline");
    }

    private void basicHills(){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for(int v = 0; v < elevation; v++) {
            int position = random.nextInt(locations.size() - 1);
            Location location = locations.get(position);

            if (location.isLand() && location.getShape() != Shape.SHORELINE) {
                for (Location next : findNeighbors(location.getNeighbors())) {
                    if (next.getShape() != Shape.SHORELINE && next.getShape() != Shape.SEA) {
                        location.setShape(Shape.HILL);
                    }
                }
            }
        }
        System.out.println("Created basic hills");
    }

    private void createVolcanos(){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for(int v = 0; v < volcanoes; v++) {
            int position = random.nextInt(locations.size() - 1);
            Location location = locations.get(position);
            location.setShape(Shape.VOLCANO);

            for (Location next: findNeighbors(location.getNeighbors())){
                next.setShape(Shape.HILL);
            }

        }
        System.out.println("Created Volcanos");
    }

    private Location[] findNeighbors(Centroid[] centroids){
        ArrayList<Location> neighbors = new ArrayList<>();
        for (Centroid centroid : centroids) {
            for (Location next : locations) {
                if(centroid == next.getCentroid()) {
                    neighbors.add(next);
                }
            }
        }

        Location[] copy = new Location[neighbors.size()];
        neighbors.toArray(copy);
        return copy;
    }

    public Plate[] getPlates() {
        return plates;
    }
}