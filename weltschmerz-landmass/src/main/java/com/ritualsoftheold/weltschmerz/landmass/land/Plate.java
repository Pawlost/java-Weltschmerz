package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.core.Generation;
import com.ritualsoftheold.weltschmerz.core.World;

import java.util.ArrayList;

public class Plate{
    private ArrayList<Location> locations;
    private Location centroid;

    public Plate(Location centroid) {
        locations = new ArrayList<>();
        this.centroid = centroid;
    }

    public void generateTectonic(ArrayList<Location> world, int range) {
        locations.add(centroid);
        main:
        while (locations.size() < range && Generation.isFreeTectonicPlate(range, world)) {
            Location[] neighbors = World.findNeighbors(centroid.getNeighbors(), world);

            int loop = 0;

            while (locations.contains(centroid)) {
                if (loop >= locations.size()) {
                    break main;
                }
                centroid = locations.get(loop);
                for (Location location : neighbors) {
                    if (!locations.contains(location) && location.getTectonicPlate() == null) {
                        centroid = location;
                    }
                }
                loop++;
            }

            for (Location location : neighbors) {
                if (location.getTectonicPlate() == null) {
                    locations.add(location);
                    location.setTectonicPlate(this);
                }
            }
        }
    }

    public void addLocation(Location location) {
        locations.add(location);
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }
}