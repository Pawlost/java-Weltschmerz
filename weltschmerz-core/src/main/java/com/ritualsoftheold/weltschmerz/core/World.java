package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.land.Plate;
import com.ritualsoftheold.weltschmerz.noise.Configuration;
import com.ritualsoftheold.weltschmerz.noise.generator.WorldNoise;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class World {
    private Configuration conf;
    private Location[][] world;
    private ArrayList<Plate> plates;
    private WorldNoise noise;

    public World(Configuration configuration, WorldNoise noise) {
        System.out.println("Seting locations");
        this.noise = noise;
        this.conf = configuration;

        world = new Location[conf.width + 1][conf.height + 1];
        plates = new ArrayList<>();

        for (int x = 0; x <= conf.width; x++) {
            for (int y = 0; y <= conf.height; y ++) {
                Location location = new Location(x, y, configuration.seed + 100 + x + y);
                world[x][y] = location;
            }
        }

        generateLand();
        connectNeighbors();

        for(Plate plate:getPlates()) {
            connectPlate(plate);
        }

        System.out.println("Locations set");
    }

    private void connectNeighbors(){
        for (int x = 0; x <= conf.width; x++) {
            for (int z = 0; z <= conf.height; z++) {
                Location location = world[x][z];
                if (x > 0 && x < conf.width) {
                    location.addNeighbor(world[x - 1][z], 0);
                    location.addNeighbor(world[x + 1][z], 2);
                } else if (x == 0){
                    location.addNeighbor(world[conf.width][z], 0);
                } else {
                    location.addNeighbor(world[0][z], 2);
                }

                if (z > 0 && z < conf.height) {
                    location.addNeighbor(world[x][z - 1], 1);
                    location.addNeighbor(world[x][z + 1], 3);
                } else if (z == 0){
                    location.addNeighbor(world[x][conf.height], 1);
                } else {
                    location.addNeighbor(world[x][0], 3);
                }
            }
        }
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

    private void generateLand() {
        for (Location[] locations : world) {
            for(Location location:locations) {
                location.setShape(noise.makeLand(location.getShape(), location.getPosition().x, location.getPosition().z));
            }
        }

       /* while (isLocationEmpty()) {
            fillEmptyLocations();
        }*/
        System.out.println("Generated Land");
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
        for (Location[] locations : world) {
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

                    if (location.getNeighbors().length - 1 <= index) {
                        break;
                    }

                    neighbors = location.getNeighbors()[index].getNeighbors();
                }
            }
        }
    }

    private void generatePlates() {
        int range = world.length;
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = conf.tectonicPlates; i > 1; i--) {
            Location location;

            int part = range / i;
            range -= part;

          /*  do {
                int position = random.nextInt(world.length);
                location = locations.get(position);
            } while (location.getTectonicPlate() != null);


            Plate plate = new Plate(location);
            location.setTectonicPlate(plate);
            //plate.generateTectonic(locations, part);
            plates.add(plate);
           */
        }
    }

    public Plate[] getPlates() {
        Plate[] copyPlates = new Plate[plates.size()];
        plates.toArray(copyPlates);
        return copyPlates;
    }

    private boolean isLocationEmpty(){
        for (Location[] locations : world) {
            for(Location location:locations) {
                if(location.getTectonicPlate() == null){
                    return true;
                }
            }
        }
        return false;
    }

    public Location[][] getLocations() {
        return world;
    }
}