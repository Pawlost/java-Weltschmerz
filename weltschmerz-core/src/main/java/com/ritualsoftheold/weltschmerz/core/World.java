package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Point;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.land.Plate;
import com.ritualsoftheold.weltschmerz.noise.Configuration;
import com.ritualsoftheold.weltschmerz.noise.generators.WorldNoise;
import squidpony.squidmath.XoRoRNG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class World {
    private Configuration conf;
    private HashMap<Point,Location> world;
    private ArrayList<Point> points;
    private ArrayList<Plate> plates;
    private WorldNoise noise;
    private XoRoRNG random;

    public World(Configuration configuration, WorldNoise noise) {
        System.out.println("Setting locations");
        this.noise = noise;
        this.conf = configuration;
        this.random = new XoRoRNG(conf.seed);

        world = new HashMap<Point, Location>();
        points = new ArrayList<>();
        plates = new ArrayList<>();

        for (int spread = 0; spread < conf.detail; spread++) {
            int x = random.nextInt();
            int y = random.nextInt();
            Point point = new Point(x, y);

            points.add(point);

            Location location = new Location(point, random.nextLong());

            world.put(point, location);
        }

        generateLand();
        /*
        for (Plate plate : getPlates()) {
            connectPlate(plate);
        }
*/
        System.out.println("Locations set");
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
            createIsland(location.getNeighbors()[0], movingPlate, used, collisionLocations, amount);
        }
    }

    private void generateLand() {
        for (Location location:world.values()) {
            location.setShape(noise.makeLand(location.getShape(), location.position));
        }
        world.get(points.get(0)).setShape(conf.shapes.get("MOUNTAIN"));
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
        }
    }*/

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
           */
        }
    }

    public Plate[] getPlates() {
        Plate[] copyPlates = new Plate[plates.size()];
        plates.toArray(copyPlates);
        return copyPlates;
    }

    private boolean isLocationEmpty() {
        for (Location[] locations : world) {
            for (Location location : locations) {
                if (location.getTectonicPlate() == null) {
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