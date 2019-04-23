package com.ritualsoftheold.weltschmerz.landmass.land;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Plate extends ArrayList<Location>{

    //middle of plate
    private Location centroid;
    private ArrayList<Plate> neighborPlates;

    //Location on edge of tectonic plate
    private Set<Location> borderLocations;

    public Plate(Location centroid) {
        this.centroid = centroid;
        neighborPlates = new ArrayList<>();
        borderLocations = new LinkedHashSet<>();
    }

    public void generateTectonic(ArrayList<Location> world, int range) {
        this.add(centroid);
        main:
        while (this.size() < range && isFreeTectonicPlate(range, world)) {
            Location[] neighbors = centroid.getNeighbors();
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

    public void makeNeighborPlates(){
        for(Location location:this) {
            Location[] neighbors = location.getNeighbors();
            for(Location neighbor:neighbors){
                if(neighbor.getTectonicPlate() != this) {
                    borderLocations.add(location);
                    if (!neighborPlates.contains(neighbor.getTectonicPlate())){
                        neighborPlates.add(neighbor.getTectonicPlate());
                    }
                }
            }
        }
    }

    public ArrayList<Plate> getNeighborPlates() {
        return neighborPlates;
    }

    public Set<Location> getBorderLocations() {
        return borderLocations;
    }

    public void reset(){
        borderLocations.clear();
        neighborPlates.clear();
    }

    private boolean isFreeTectonicPlate(int range, ArrayList<Location> world) {
        int count = 0;
        for (Location location : world) {
            if (location.getTectonicPlate() == null) {
                count++;
            }
        }

        return count > range;
    }
}