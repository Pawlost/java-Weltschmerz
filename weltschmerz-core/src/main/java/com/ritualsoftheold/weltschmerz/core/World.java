package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.PrecisionMath;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.fortune.Voronoi;
import com.ritualsoftheold.weltschmerz.landmass.fortune.algorithms.Fortune;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.land.Plate;
import com.ritualsoftheold.weltschmerz.noise.Configuration;
import com.ritualsoftheold.weltschmerz.noise.WeltschmerzNoise;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class World {
    private static final int SPACING = 1;
    private Configuration conf;
    private ArrayList<Location> locations;
    private ArrayList<Centroid> centroids;
    private ArrayList<Plate> plates;
    private WeltschmerzNoise noise;

    public World(Configuration configuration,WeltschmerzNoise noise) {
        System.out.println("Seting locations");
        this.noise = noise;
        this.conf = configuration;

        locations = new ArrayList<>();
        centroids = new ArrayList<>();
        plates = new ArrayList<>();

        for (double x = -conf.detail; x < conf.width + 2 *conf.detail; x += conf.detail) {
            for (double y = -conf.detail; y < conf.height + 2*conf.detail; y += conf.detail) {
                Location location = new Location(x, y);
                centroids.add(location.getCentroid());
                locations.add(location);
            }
        }

        System.out.println("Locations set");
    }

    public void firstGeneration() {
        generateBorders();
        checkBorders();
        generatePlates();
        generateLand();

        for(Plate plate:getPlates()) {
            connectPlate(plate);
        }
       // createVolcanoes();
        createHills();
        createShoreline();
        System.out.println("First generation done");
    }

    private void createIsland(Location location, Plate movingPlate, ArrayList<Location> used, ArrayList<Location> collisionLocations, int amount){
        amount--;
        for (Location neighbor : location.getNeighbors()) {
            if (!neighbor.isLand() || neighbor.getKey().equals("SEA")) {
                Plate plate = neighbor.getTectonicPlate();
                neighbor.setLand(true);
                neighbor.setShape(noise.getShape("PLAIN"));
                neighbor.setTectonicPlate(movingPlate);
                plate.remove(neighbor);
                used.add(neighbor);
                collisionLocations.remove(neighbor);
                movingPlate.add(neighbor);
            }
        }

        if(amount > 0){
            createIsland(location.getNeighbors()[0], movingPlate, used, collisionLocations, amount);
        }
    }

    private void checkBorders() {
        for (Location location : locations) {
            for (Border border : location.getBorders()) {
                Location[] neighbors = location.getNeighbors();
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
        System.out.println("Generating borders");
        Centroid[] copyCenters = new Centroid[centroids.size()];
        centroids.toArray(copyCenters);
        Voronoi voronoi = Fortune.ComputeGraph(copyCenters);

        voronoi.getVoronoiArea(locations, conf.width, conf.height);

        System.out.println("Borders generated");
    }

    private void generateLand() {
        for (Location location : locations) {
            location.makeLand(noise, SPACING);
        }

        while (isLocationEmpty()) {
            fillEmptyLocations();
        }
        System.out.println("Generated Tectonic Plates");
    }

    private void connectPlate(Plate plate) {
        plate.makeNeighborPlates();
        int neighborPlateSize = plate.getNeighborPlates().size();
        if (neighborPlateSize < 2) {
            Plate newPlate = plate.getNeighborPlates().get(neighborPlateSize - 1);
            for (Location location : plate) {
                location.setTectonicPlate(newPlate);
                newPlate.add(location);
            }
            plates.remove(plate);
            newPlate.reset();
            connectPlate(newPlate);
        }
    }

    private void fillEmptyLocations() {
        for (Location location : locations) {
            int index = 0;
            Location[] neighbors = location.getNeighbors();

            check:
            while (location.getTectonicPlate() == null) {
                index++;
                for (Location neighbor : neighbors) {
                    if (neighbor.getTectonicPlate() != null) {
                        Plate plate = neighbor.getTectonicPlate();
                        plate.add(location);
                        location.setTectonicPlate(plate);
                        break check;
                    }
                }

                if (location.getNeighbors().length - 1 <= index){
                    break;
                }

                neighbors = location.getNeighbors()[index].getNeighbors();
            }
        }
    }

    private void generatePlates() {
        int range = locations.size();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = conf.tectonicPlates; i > 1; i--) {
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
        System.out.println("Creating shoreline");
        for (Location location : locations) {
            for (Location next : location.getNeighbors()) {
                if (next.isLand() != location.isLand()) {
                    if (location.isLand()) {
                        location.setShape(noise.getShape("SHORELINE"));
                    } else {
                        location.setShape(noise.getShape("SEA"));
                    }
                    break;
                }
            }
        }
        for (Location location : locations) {
            if (location.getKey().equals("SHORELINE") && checkShoreline(location, false)) {
                location.setShape(noise.getShape("PLAIN"));
            } else if (location.isLand() && checkShoreline(location, true)) {
                location.setShape(noise.getShape("OCEAN"));
                location.setLand(false);
                for(Location neighbor:location.getNeighbors()){
                    neighbor.setLand(false);
                    neighbor.setShape(noise.getShape("OCEAN"));
                }
            }
        }
        System.out.println("Shoreline created");
    }

    private boolean checkShoreline(Location location, boolean get) {
        for (Location next : location.getNeighbors()) {
            if (next.isLand() == get) {
                return false;
            }
        }
        return true;
    }

    private void createHills() {
        System.out.println("Creating Hills");
        for (Location location:locations){
            Location[] neighbors;
            switch (location.getKey()){
                case "MOUNTAIN":
                   neighbors = location.getNeighbors();
                    for(Location neighbor:neighbors){
                        if(neighbor.getShape().position < location.getShape().position){
                            neighbor.setShape(noise.getShape("HILL"));
                            neighbor.setLand(true);
                        }
                    }
                    break;
                case "HILL":
                    neighbors = location.getNeighbors();
                    for(Location neighbor:neighbors){
                        if(neighbor.getShape().position < location.getShape().position){
                            neighbor.setShape(noise.getShape("PLAIN"));
                            neighbor.setLand(true);
                        }
                    }
                    break;
            }
        }

        System.out.println("Hills created");
    }

    private void createVolcanoes() {
        System.out.println("Creating volcanoes");
        for (int v = 1; v <= conf.volcanoes; v++) {
            int plateIndex = (int)Math.round(PrecisionMath.sigmoid(conf.seed, v)*conf.volcanoes);
            ArrayList<Location> borderLocations = plates.get(plateIndex);
            int locationIndex = (int)Math.round(PrecisionMath.sigmoid(conf.seed, v)*borderLocations.size());
            Location location = borderLocations.get(locationIndex);
            location.setShape(noise.getShape("VOLCANO"));
            for (Location next : location.getNeighbors()) {
                next.setShape(noise.getShape("MOUNTAIN"));
                next.setLand(true);
            }
        }
        System.out.println("Volcanoes created");
    }

    public Plate[] getPlates() {
        Plate[] copyPlates = new Plate[plates.size()];
        plates.toArray(copyPlates);
        return copyPlates;
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
}