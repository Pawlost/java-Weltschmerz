package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.fortune.Voronoi;
import com.ritualsoftheold.weltschmerz.landmass.fortune.algorithms.Fortune;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.land.Plate;
import com.sudoplay.joise.module.ModuleAutoCorrect;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class World extends ArrayList<Location> {
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
                 ModuleAutoCorrect module){
        this.size = (int) spread;
        this.module = module;
        this.tectonicPlates = tectonicPlates;

        ThreadLocalRandom random = ThreadLocalRandom.current();
        locations = new ArrayList<>();
        centroids = new ArrayList<>();
        plates = new ArrayList<>();

        this.volcanoes = volcanoes;
        this.elevation = random.nextInt(elevation);

        for(int i = 0; i < scale; i++){
            double x = random.nextDouble(1, spread);
            double y = random.nextDouble(1, spread);
            Location location = new Location(x, y);
            centroids.add(location.getCentroid());
            locations.add(location);
        }

        System.out.println("Set locations");
    }

    public Location[] generateFirstLand(){
        generateLand();

        createShoreline();
        basicHills();
        createVolcanos();

        return getLocations();
    }

    private void generateLand(){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Centroid[] copyCenters = new Centroid[centroids.size()];
        centroids.toArray(copyCenters);
        Voronoi voronoi = Fortune.ComputeGraph(copyCenters);

        voronoi.getVoronoiArea(locations, size, size);
        System.out.println("Generated borders");

        for(Location location:locations){
            location.setLand(module, DETAIL);
        }

        int range = locations.size();


        for(int i = tectonicPlates; i > 7; i--){
            Location location;
            do {
                int position = random.nextInt(locations.size());
                location = locations.get(position);
            }while (location.getTectonicPlate() != null);

            int lol = range/i;
            Plate plate = new Plate(location);
            range -= lol;
            location.setTectonicPlate(plate);
            plates.add(plate);

            plate.generateTectonic(locations, lol);
            smoothPlate(plate);
        }

        System.out.println("Generated Tectonic Plates");
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
        return getLocations();
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

    private void basicHills(){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for(int v = 0; v < elevation; v++) {
            int position = 0;
            try {
                position = random.nextInt(locations.size() - 1);
            }catch (IllegalArgumentException e){
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

    private void createVolcanos(){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for(int v = 0; v < volcanoes; v++) {
            int position = random.nextInt(locations.size() - 1);
            Location location = locations.get(position);
            location.setShape(Shape.VOLCANO);

            for (Location next: findNeighbors(location.getNeighbors(), locations)){
                next.setShape(Shape.HILL);
            }

        }
        System.out.println("Created Volcanos");
    }

    private void smoothPlate(Plate plate) {
        for (Location location : plate.getLocations()) {
            Location[] neighbors = findNeighbors(location.getNeighbors(), locations);
            for (Location next : neighbors) {
                for (Border border : location.getBorders()) {
                    for (Border nextBorder : next.getBorders()) {
                        if (border.getDatumB() == null && border.getDatumA() == null ||
                                border.equals(nextBorder) && next.getTectonicPlate() == plate ||
                                next.getBorders().contains(border) && next.getTectonicPlate() == plate) {
                            plate.getBorders().remove(border);
                        }
                    }
                }
            }
        }
    }

    public static Location[] findNeighbors(Centroid[] centroids, ArrayList<Location> locations){
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

    public ArrayList<Plate> getPlates() {
        return plates;
    }
}