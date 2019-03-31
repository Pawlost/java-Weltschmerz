package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.core.Generation;
import com.ritualsoftheold.weltschmerz.core.World;

import java.util.ArrayList;

public class Plate extends ArrayList<Location>{
    private Location centroid;
    private ArrayList<Plate> neighborPlates;

    public Plate(Location centroid) {
        this.centroid = centroid;
        neighborPlates = new ArrayList<>();
    }

    public void generateTectonic(ArrayList<Location> world, int range) {
        this.add(centroid);
        main:
        while (this.size() < range && Generation.isFreeTectonicPlate(range, world)) {
            Location[] neighbors = World.findNeighbors(centroid.getNeighbors(), world);
            int loop = 0;

            while (this.contains(centroid)) {
                if (loop >= this.size()) {
                    break main;
                }
                centroid = this.get(loop);
                for (Location location : neighbors) {
                    if (!this.contains(location) && location.getTectonicPlate() == null) {
                        centroid = location;
                    }
                }
                loop++;
            }

            for (Location location : neighbors) {
                if (location.getTectonicPlate() == null) {
                    this.add(location);
                    location.setTectonicPlate(this);
                }
            }
        }
    }

    public void makeNeighborPlates(ArrayList<Location> world){
        for(Location location:this) {
            Location[] neighbors = World.findNeighbors(location.getNeighbors(), world);
            for(Location neighbor:neighbors){
                if(neighbor.getTectonicPlate() != this && !neighborPlates.contains(neighbor.getTectonicPlate())){
                    neighborPlates.add(neighbor.getTectonicPlate());
                }
            }
        }
    }

    public ArrayList<Plate> getNeighborPlates() {
        return neighborPlates;
    }
}