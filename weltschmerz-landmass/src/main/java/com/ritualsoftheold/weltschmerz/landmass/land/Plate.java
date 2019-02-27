package com.ritualsoftheold.weltschmerz.landmass.land;

import com.ritualsoftheold.weltschmerz.core.World;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.Border;

import java.util.ArrayList;

public class Plate extends Area {
    private ArrayList<Location> locations;
    private Location centroid;

    public Plate(Location centroid) {
        locations = new ArrayList<>();
        this.centroid = centroid;
    }

    public void generateTectonic(ArrayList<Location> world, int range) {
        locations.add(centroid);
        int index = 0;
        while (locations.size() < range) {
            int oldSize = locations.size();
            Location[] neighbors = World.findNeighbors(centroid.getNeighbors(), world);
            for (Location location : neighbors) {
                if (location.getTectonicPlate() == null) {
                    locations.add(location);
                    location.setTectonicPlate(this);
                }
            }

            if (locations.size() == oldSize) {
                index++;
                try {
                    centroid = locations.get(index);
                }catch (IndexOutOfBoundsException e) {
                    index = 0;
                    for (Location location : world) {
                        if (location.getTectonicPlate() == null) {
                            location.setTectonicPlate(this);
                            locations.clear();
                            locations.add(location);
                            centroid = location;
                            break;
                        }
                    }
                }
            } else {
                centroid = locations.get(index);
            }
        }
        reduce(world);
    }

    @Override
    public void listVariables() {
        vertice.clear();
        for (Border border : borders) {
            vertice.add(border.getVertexA());
            vertice.add(border.getVertexB());
        }
    }

    @Override
    public void reset() {

    }

    private void reduce(ArrayList<Location> world) {
        borders.clear();
        for (Location location : locations) {
            borders.addAll(location.getBorders());
        }

        for (Location location : locations) {
            Location[] neighbors = World.findNeighbors(location.getNeighbors(), world);
            for (Location next : neighbors) {
                for (Border border : location.getBorders()) {
                    for (Border nextBorder : next.getBorders()) {
                        if (border.equals(nextBorder) && next.getTectonicPlate() == this ||
                                next.getBorders().contains(border) && next.getTectonicPlate() == this) {
                            borders.remove(border);
                        }
                    }
                }
            }
        }
        circularize();
    }

    public ArrayList<Location> getLocations (){
        return locations;
    }
}