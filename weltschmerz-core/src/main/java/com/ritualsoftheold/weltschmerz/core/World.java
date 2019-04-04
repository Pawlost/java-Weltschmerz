package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.Legend;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.fortune.Voronoi;
import com.ritualsoftheold.weltschmerz.landmass.fortune.algorithms.Fortune;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Centroid;
import com.ritualsoftheold.weltschmerz.landmass.land.Plate;
import com.ritualsoftheold.weltschmerz.noise.WeltschmerzNoise;
import com.sudoplay.joise.module.ModuleAutoCorrect;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class World {
    private int size;
    private static final int SPACING = 1;
    private int volcanoes;
    private int elevation;
    private int tectonicPlates;
    private int islandSize;
    private ArrayList<Location> locations;
    private ArrayList<Centroid> centroids;
    private ArrayList<Plate> plates;
    private WeltschmerzNoise noise;

    public World(int size, long detail, int volcanoes, int tectonicPlates, int hills, int islandSize,
                 WeltschmerzNoise noise) {
        System.out.println("Seting locations");
        ThreadLocalRandom random = ThreadLocalRandom.current();
        this.size = size;
        this.noise = noise;
        this.tectonicPlates = tectonicPlates;
        this.volcanoes = volcanoes;
        this.islandSize = islandSize;
        this.elevation = random.nextInt(hills);

        locations = new ArrayList<>();
        centroids = new ArrayList<>();
        plates = new ArrayList<>();

        for (int i = 0; i < detail; i++) {
            double x = random.nextDouble(-10, size + 10);
            double y = random.nextDouble(-10, size + 10);
            Location location = new Location(x, y);
            centroids.add(location.getCentroid());
            locations.add(location);
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
        makeElevation();
        createShoreline();
        createVolcanos();
        createHills();
        System.out.println("First generation done");
    }

    public void moveTectonicPlates(){
        System.out.println("Moving Tectonic plate");
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int randomIndex = random.nextInt(0, plates.size() -1);
        Plate movingPlate = plates.get(randomIndex);
        Plate collisionPlate = movingPlate.getNeighborPlates().get(0);

        for(Plate plate:movingPlate.getNeighborPlates()){
            if(plate.size() > collisionPlate.size()){
                collisionPlate = plate;
            }
        }

        Set<Location> collisionLocations = collisionPlate.getBorderLocations();
        Location[] borderLocations = new Location[collisionLocations.size()];
        collisionLocations.toArray(borderLocations);
        for(Location location:borderLocations){
            Location[] neighbors = location.getNeighbors();
            if(!isLocationBorder(movingPlate, neighbors)){
                collisionLocations.remove(location);
            }
        }

        borderLocations = new Location[collisionLocations.size()];
        collisionLocations.toArray(borderLocations);
        ArrayList<Location> used = new ArrayList<>();
        for (Location location : borderLocations) {
            if(!used.contains(location)) {
                if (location.isLand()) {
                    location.setLegend(Legend.MOUNTAIN);
                    location.setTectonicPlate(movingPlate);
                    collisionPlate.remove(location);
                    movingPlate.add(location);
                } else {
                    location.setLand(true);
                    location.setLegend(Legend.PLAIN);
                    location.setTectonicPlate(movingPlate);
                    collisionPlate.remove(location);
                    movingPlate.add(location);
                    createIsland(location, movingPlate, used, collisionPlate, islandSize);
                }
            }
        }

        for(Plate plate:getPlates()){
            plate.reset();
            connectPlate(plate);
        }

        createHills();
        createShoreline();
        System.out.println("Tectonic plates moved");
    }

    private void createIsland(Location location, Plate movingPlate, ArrayList<Location> used, ArrayList<Location> collisionLocations, int amount){
        amount--;
        for (Location neighbor : location.getNeighbors()) {
            if (!neighbor.isLand() || neighbor.getLegend() == Legend.SEA) {
                Plate plate = neighbor.getTectonicPlate();
                neighbor.setLand(true);
                neighbor.setLegend(Legend.PLAIN);
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

    private boolean isLocationBorder(Plate plate, Location[] neighbors){
        for(Location neighbor:neighbors){
            if(neighbor.getTectonicPlate() == plate){
                return true;
            }
        }
        return false;
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

        voronoi.getVoronoiArea(locations, size, size);

        for(Location location:locations){
            location.makeNeighbors(locations);
        }
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

    public Location[] reshapeWorld() {
        System.out.println("Reshaping world");
        System.out.println("Changeding Centroids");
        for (Location location : locations) {
            for (int i = 0; i < centroids.size(); i++) {
                if (location.getCentroid() == centroids.get(i)) {
                    location.reset();
                    centroids.set(i, location.getCentroid());
                }
            }
        }

        System.out.println("Centroids changed");

        generateBorders();
        checkBorders();

        for(Plate plate:plates){
            plate.clear();
            for(Location location:locations){
                if(location.getTectonicPlate() == plate && location.getPolygon() != null) {
                    plate.add(location);
                }
            }
        }

        generateLand();
        System.out.println("World reshaped");
        return getLocations();
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
        System.out.println("Creating shoreline");
        for (Location location : locations) {
            for (Location next : location.getNeighbors()) {
                if (next.isLand() != location.isLand()) {
                    if (location.isLand()) {
                        location.setLegend(Legend.SHORELINE);
                    } else {
                        location.setLegend(Legend.SEA);
                    }
                    break;
                }
            }
        }

        for (Location location : locations) {
            if(location.getLegend() == Legend.SHORELINE && checkShoreline(location)){
                location.setLegend(Legend.PLAIN);
            }
        }

        System.out.println("Shoreline created");
    }

    private boolean checkShoreline(Location location) {
        for (Location next : location.getNeighbors()) {
            if (!next.isLand()) {
                return false;
            }
        }
        return true;
    }

    private void makeElevation(){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int v = 0; v < elevation; v++) {
            int position = 0;
            try {
                position = random.nextInt(locations.size() - 1);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            Location location = locations.get(position);

            if (location.isLand() && location.getLegend() != Legend.SHORELINE) {
                for (Location next : location.getNeighbors()) {
                    if (next.getLegend() != Legend.SHORELINE && next.getLegend() != Legend.SEA) {
                        location.setLegend(Legend.HILL);
                    }
                }
            }
        }
    }

    private void createHills() {
        System.out.println("Creating Hills");
        for (Location location:locations){
            Location[] neighbors;
            switch (location.getLegend()){
                case MOUNTAIN:
                   neighbors = location.getNeighbors();
                    for(Location neighbor:neighbors){
                        if(neighbor.getLegend().position < location.getLegend().position){
                            neighbor.setLegend(Legend.HILL);
                        }
                    }
                    break;
                case HILL:
                    neighbors = location.getNeighbors();
                    for(Location neighbor:neighbors){
                        if(neighbor.getLegend().position < location.getLegend().position){
                            neighbor.setLegend(Legend.PLAIN);
                        }
                    }
                    break;
            }
        }

        System.out.println("Hills created");
    }

    private void createVolcanos() {
        System.out.println("Creating volcanoes");
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int v = 0; v < volcanoes; v++) {
            int plateIndex = random.nextInt(plates.size() - 1);
            ArrayList<Location> borderLocations = plates.get(plateIndex);
            int locationIndex = random.nextInt(borderLocations.size() - 1);
            Location location = borderLocations.get(locationIndex);
            location.setLegend(Legend.VOLCANO);

            for (Location next : location.getNeighbors()) {
                next.setLegend(Legend.MOUNTAIN);
            }
        }
        System.out.println("Volcanoes created");
    }

    public Plate[] getPlates() {
        Plate[] copyPlates = new Plate[plates.size()];
        plates.toArray(copyPlates);
        return copyPlates;
    }
}