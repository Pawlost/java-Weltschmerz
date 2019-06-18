package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;
import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.landmass.Fortune;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.land.Plate;
import com.ritualsoftheold.weltschmerz.noise.generators.WorldNoise;
import jdk.jshell.execution.LoaderDelegate;
import org.checkerframework.checker.units.qual.A;
import squidpony.squidmath.XoRoRNG;

import java.util.*;

public class World {
    private Configuration conf;
    private HashMap<Point, Location> world;
    private ArrayList<Plate> plates;
    private WorldNoise noise;
    private XoRoRNG random;

    public World(Configuration configuration, WorldNoise noise) {
        System.out.println("Setting locations");
        this.noise = noise;
        this.conf = configuration;
        this.random = new XoRoRNG(conf.seed);

        world = new HashMap<>();
        plates = new ArrayList<>();

        for (int spread = 0; spread <= conf.detail; spread++) {
            double x = random.nextInt(-1,conf.width) + random.nextDouble();
            double y = random.nextInt(-1, conf.height) + random.nextDouble();
            Point point = new Point(x, y);
            Location location = new Location(point, random.nextLong());
            world.put(point, location);
        }

        world = Fortune.ComputeGraph(world.keySet()).smoothLocation(world, 10);
        Fortune.ComputeGraph(world.keySet()).getVoronoiArea(world);
        System.out.println("Locations set");

        generateLand();
        /*
        for (Plate plate : getPlates()) {
            connectPlate(plate);
        }
*/
    }

    private void createIsland(Location location, Plate movingPlate, ArrayList<Location> used, ArrayList<Location> collisionLocations, int amount) {
        amount--;
       /* for (Location neighbor : location.getNeighbors()) {
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
        }*/

        if (amount > 0) {
           // createIsland(location.getNeighbors()[0], movingPlate, used, collisionLocations, amount);
        }
    }

    private void generateLand() {
        Set<Location> anotherLocations = new HashSet<>();
        Set<Location> done = new HashSet<>();
        for (Location location:world.values()) {
            for(Point point:location.position.getNeighborPoints()){
                if(world.get(point) != null) {
                    location.add(world.get(point));
                }
            }

            if(location.setShape(noise.makeLand(location.getShape(), location.position.centroid))){
                anotherLocations.addAll(location.getNeighbors());
                anotherLocations.add(location);
            }
        }

        while (!anotherLocations.isEmpty()) {
            for (Location location : new HashSet<>(anotherLocations)) {
                location.setElevation();
                anotherLocations.remove(location);
                done.add(location);
                for (Location neigbor : location.getNeighbors()) {
                    if (!done.contains(neigbor)) {
                        anotherLocations.add(neigbor);
                    }
                }
            }
        }
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
   /*     for (Location[] locations : world) {
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
        }*/
    }

    private void generatePlates() {
      /*  int range = world.length;
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
        }           */
    }

    public Plate[] getPlates() {
        Plate[] copyPlates = new Plate[plates.size()];
        plates.toArray(copyPlates);
        return copyPlates;
    }

    private boolean isLocationEmpty() {
        for (Location location : world.values()) {
            if (location.getTectonicPlate() == null) {
                return true;
            }
        }
        return false;
    }

    public Collection<Location> getLocations() {
        return world.values();
    }

    public HashMap<Point, Location> getWorld() {
        return world;
    }
}