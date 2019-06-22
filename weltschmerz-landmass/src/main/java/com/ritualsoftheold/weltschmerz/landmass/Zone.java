package com.ritualsoftheold.weltschmerz.landmass;

import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.landmass.land.Chunk;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.noise.generators.WorldNoise;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.util.ArrayList;
import java.util.HashMap;

public class Zone extends ArrayList<Location> {
    private HashMap<Point, Location> world;
    public static final int PRELOAD = 5;
    private Chunk currentChunk;
    private MultiKeyMap<Integer, Chunk> chunks;
    private WorldNoise worldNoise;
    Point currentPosition;

    public Zone (HashMap<Point, Location> world) {
        this.world = world;
        chunks = new MultiKeyMap<>();
        currentPosition = new Point(0, 0);
        System.out.println("Setting zone");
        for (int x = 0; x < PRELOAD; x++) {
            for (int z = 0; z < PRELOAD; z++) {
                for (Location location : world.values()) {
                    if (location.position.contains(x, z)) {
                        chunks.put(x, z, location.setChunk(x, z));
                    }
                }
            }
        }
        System.out.println("Zone set");
    }

    public void updatePlayerPosition(int posX, int posZ) {
        for (Location location : world.values()) {
            if (!chunks.containsKey(posX + 1, posZ)) {
                if (location.position.contains(posX + 1, posZ)) {
                    chunks.put(posX + 1, posZ, location.setChunk(posX + 1, posZ));
                }
            }

            if (!chunks.containsKey(posX - 1, posZ)) {
                if (location.position.contains(posX - 1, posZ)) {
                    chunks.put(posX - 1, posZ, location.setChunk(posX - 1, posZ));
                }
            }

            if (!chunks.containsKey(posX, posZ + 1)) {
                if (location.position.contains(posX, posZ + 1)) {
                    chunks.put(posX, posZ + 1, location.setChunk(posX, posZ + 1));
                }
            }

            if (!chunks.containsKey(posX, posZ - 1)) {
                if (location.position.contains(posX, posZ - 1)) {
                    chunks.put(posX, posZ - 1, location.setChunk(posX, posZ - 1));
                }
            }

        }
    }

    public int getChunkLocation (int x, int z){
        currentChunk = chunks.get(x, z);
        currentChunk.findNeighbors(chunks);
        return currentChunk.getElevation();
    }

    public String getSectorName(){
        return currentChunk.sectorName;
    }

    public double getNoise(int x, int z){
        return currentChunk.getNoise(x, z);
    }
}
