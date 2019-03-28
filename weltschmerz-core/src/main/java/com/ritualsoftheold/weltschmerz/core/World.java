package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.fortune.Voronoi;
import com.ritualsoftheold.weltschmerz.landmass.fortune.algorithms.Fortune;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.land.Plate;
import com.sudoplay.joise.module.ModuleAutoCorrect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class World {
    private int size;
    private static final int DETAIL = 1;
    private int volcanoes;
    private int elevation;
    private int tectonicPlates;
    private ArrayList<Location> locations;
    private ArrayList<Centroid> centroids;
    private ArrayList<Plate> plates;
    private ModuleAutoCorrect module;

    public World(int scale, double spread, int volcanoes, int tectonicPlates, int elevation,
                 ModuleAutoCorrect module) {
        this.size = (int) spread;
        this.module = module;
        this.tectonicPlates = tectonicPlates;

        ThreadLocalRandom random = ThreadLocalRandom.current();
        locations = new ArrayList<>();
        centroids = new ArrayList<>();
        plates = new ArrayList<>();

        this.volcanoes = volcanoes;
        this.elevation = random.nextInt(elevation);

        for (int i = 0; i < scale; i++) {
            double x = random.nextDouble(1, spread);
            double y = random.nextDouble(1, spread);
            Location location = new Location(x, y);
            centroids.add(location.getCentroid());
            locations.add(location);
        }

        System.out.println("Set locations");
    }

    public void generateFirstLand() {
        generateBorders();
        checkBorders();
        generatePlates();
        generateLand();

        createShoreline();
        basicHills();
        createVolcanos();

        getLocations();
    }

    private void checkBorders() {
        for (Location location : locations) {
            for (Border border : location.getBorders()) {
                Location[] neighbors = findNeighbors(new Centroid[]{border.getDatumA(), border.getDatumB()}, locations);
                if (neighbors.length == 1) {
                    if (location.getCentroid().equals(border.getDatumA())) {
                        border.setDatumB(null);
                    } else if (location.getCentroid().equals(border.getDatumB())) {
                        border.setDatumA(null);
                    }
                }
            }
        }
    }

    private void generateBorders() {
        Centroid[] copyCenters = new Centroid[centroids.size()];
        centroids.toArray(copyCenters);
        Voronoi voronoi = Fortune.ComputeGraph(copyCenters);

        voronoi.getVoronoiArea(locations, size, size);
        System.out.println("Generated borders");
    }


    private void generateLand() {

        for (Location location : locations) {
            location.setLand(module, DETAIL);
        }


        while (isLocationEmpty()) {
            checkEmptyLocations();
        }

        System.out.println("Generated Tectonic Plates");
    }

    public Location[] reshapeWorld() {
        for (Location location : locations) {
            for (int i = 0; i < centroids.size(); i++) {
                if (location.getCentroid() == centroids.get(i)) {
                    location.reset();
                    centroids.set(i, location.getCentroid());
                }
            }
        }

        generateLand();
        System.out.println("Reshaped");
        return getLocations();
    }

    private void checkEmptyLocations() {
        for (Location location1 : locations) {
            Centroid[] centroids = location1.getNeighbors();
            int index = 0;

            check:
            while (location1.getTectonicPlate() == null) {
                Location[] neighbors = World.findNeighbors(centroids, locations);
                if (neighbors.length != centroids.length) {
                    neighbors = World.findNeighbors(centroids[index], locations);
                }

                index++;
                for (Location neighbor : neighbors) {
                    if (neighbor.getTectonicPlate() != null) {
                        Plate plate = neighbor.getTectonicPlate();
                        plate.addLocation(location1);
                        location1.setTectonicPlate(plate);
                        break check;
                    }
                }

                if (centroids.length - 1 <= index){
                    break;
                }
            }
        }
    }

    private boolean isLocationEmpty(){
        for (Location location1 : locations) {
            if(location1.getTectonicPlate() == null){
                return true;
            }
        }
        return false;
    }
    public Location[] getLocations() {
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

    private void generatePlates() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int range = locations.size();

        for (int i = tectonicPlates; i > 1; i--) {
            Location location;

            int part = range / i;
            range -= part;

            do {
                int position = random.nextInt(locations.size());
                location = locations.get(position);
            } while (location.getTectonicPlate() != null);

            Plate plate = new Plate(location);
            location.setTectonicPlate(plate);
            plate.generateTectonic(locations, part);
            plates.add(plate);
        }
    }

    private void createShoreline() {
        for (Location location : locations) {
            for (Location next : findNeighbors(location.getNeighbors(), locations)) {
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

    private void basicHills() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int v = 0; v < elevation; v++) {
            int position = 0;
            try {
                position = random.nextInt(locations.size() - 1);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            Location location = locations.get(position);

            if (location.isLand() && location.getShape() != Shape.SHORELINE) {
                for (Location next : findNeighbors(location.getNeighbors(), locations)) {
                    if (next.getShape() != Shape.SHORELINE && next.getShape() != Shape.SEA) {
                        location.setShape(Shape.HILL);
                    }
                }
            }
        }
        System.out.println("Created basic hills");
    }

    private void createVolcanos() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int v = 0; v < volcanoes; v++) {
            int position = random.nextInt(locations.size() - 1);
            Location location = locations.get(position);
            location.setShape(Shape.VOLCANO);

            for (Location next : findNeighbors(location.getNeighbors(), locations)) {
                next.setShape(Shape.HILL);
            }

        }
        System.out.println("Created Volcanos");
    }

    public static Location[] findNeighbors(Centroid[] centroids, ArrayList<Location> locations) {
        ArrayList<Location> neighbors = new ArrayList<>();
        for (Centroid centroid : centroids) {
            for (Location next : locations) {
                if (centroid == next.getCentroid()) {
                    neighbors.add(next);
                }
            }
        }

        Location[] copy = new Location[neighbors.size()];
        neighbors.toArray(copy);
        return copy;
    }

    public static Location[] findNeighbors(Centroid centroid, ArrayList<Location> locations) {
        ArrayList<Location> neighbors = new ArrayList<>();
            for (Location next : locations) {
                ArrayList<Centroid> centroids = new ArrayList<>();
                Collections.addAll(centroids, next.getNeighbors());
                if (centroids.contains(centroid)) {
                    neighbors.add(next);
                }
        }

        Location[] copy = new Location[neighbors.size()];
        neighbors.toArray(copy);
        return copy;
    }

    public ArrayList<Plate> getPlates() {
        return plates;
    }
}