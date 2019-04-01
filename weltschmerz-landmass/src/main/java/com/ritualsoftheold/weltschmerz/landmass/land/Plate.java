package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.landmass.Generation;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Plate extends ArrayList<Location>{
    private Location centroid;
    private ArrayList<Plate> neighborPlates;
    private Set<Location> borderLocations;

    public Plate(Location centroid) {
        this.centroid = centroid;
        neighborPlates = new ArrayList<>();
        borderLocations = new LinkedHashSet<>();
    }

    public void generateTectonic(ArrayList<Location> world, int range) {
        this.add(centroid);
        main:
        while (this.size() < range && Generation.isFreeTectonicPlate(range, world)) {
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
}