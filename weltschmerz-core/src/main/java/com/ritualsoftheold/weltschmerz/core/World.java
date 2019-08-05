package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;
import com.ritualsoftheold.weltschmerz.geometry.misc.PrecisionMath;
import com.ritualsoftheold.weltschmerz.geometry.units.Point;
import com.ritualsoftheold.weltschmerz.geometry.units.Vector;
import com.ritualsoftheold.weltschmerz.landmass.Circulation;
import com.ritualsoftheold.weltschmerz.landmass.Equator;
import com.ritualsoftheold.weltschmerz.landmass.Fortune;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.noise.generators.WorldNoise;
import org.apache.commons.collections4.map.MultiKeyMap;
import squidpony.squidmath.XoRoRNG;

import java.nio.ByteBuffer;
import java.util.*;

public class World {

    private Equator equator;
    private HashMap<Point, Location> map;
    private WorldNoise noise;
    private Circulation circulation;
    private int dirtID;
    private int grassID;
    private boolean isDifferent;
    private int ortographicsEffect;

    public World(Configuration configuration) {
        System.out.println("Starting generation");
        this.noise = new WorldNoise(configuration);

        this.equator = new Equator(noise, configuration);
        circulation = new Circulation(equator);
        map = new HashMap<>();

        XoRoRNG random = new XoRoRNG(configuration.seed);

        for (int spread = 0; spread <= configuration.detail; spread++) {
            double x = random.nextInt(-1, configuration.longitude) + random.nextDouble();
            double y = random.nextInt(-1, configuration.latitude) + random.nextDouble();
            Point point = new Point(x, y);
            Location location = new Location(point, random.nextLong());
            map.put(point, location);
        }

        map = Fortune.ComputeGraph(map.keySet()).smoothLocation(map, 10);
        Fortune.ComputeGraph(map.keySet()).getVoronoiArea(map, noise);
        System.out.println("Locations set");

        generateLand();

        System.out.println("Generation finished");
    }

    private void generateLand() {
        Set<Location> anotherLocations = new HashSet<>();
        Set<Location> done = new HashSet<>();
        for (Location location : map.values()) {
            for (Point point : location.position.getNeighborPoints()) {
                if (map.get(point) != null) {
                    location.add(map.get(point));
                }
            }

            if (location.setShape(noise.makeLand(location.getShape(), location.position.centroid))) {
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

    public double getTemperature(int posX, int posY) {
        return equator.getTemperature(posX, posY);
    }

    public void setMaterials(int dirtID, int grassID) {
        this.dirtID = dirtID;
        this.grassID = grassID;
    }

    public ByteBuffer getChunk(int posX, int posY, int posZ, int bufferSize) {
        ByteBuffer blockBuffer = ByteBuffer.allocate(bufferSize);
        byte[] fill = new byte[bufferSize];
        Arrays.fill(fill, (byte) 1);
        blockBuffer.put(fill);

        MultiKeyMap<Integer, ByteBuffer> blocks = new MultiKeyMap<>();
        isDifferent = false;

        for (int z = 0; z < 64; z++) {
            for (int x = 0; x < 64; x++) {
                ByteBuffer bb = ByteBuffer.allocate(64);
                int y = (int) Math.round(noise.getNoise(x + posX * 64, z + posZ * 64));
                if (y / 64 > posY / 64) {
                    byte[] underArray = new byte[64];
                    Arrays.fill(underArray, (byte) dirtID);
                    bb.put(underArray);
                    blocks.put(z, x, bb);
                } else if (y / 64 == posY / 64) {
                    byte[] underArray = new byte[(y % 64)];
                    Arrays.fill(underArray, (byte) dirtID);
                    bb.put(underArray);

                    byte[] upperArray = new byte[64 - (y % 64)];
                    Arrays.fill(upperArray, (byte) 1);
                    bb.put(upperArray);

                    bb.put(y % 64, (byte) grassID);

                    isDifferent = true;
                    blocks.put(z, x, bb);
                }
            }
        }

        if (isDifferent) {
            for (int z = 0; z < 64; z++) {
                for (int x = 0; x < 64; x++) {
                    if (blocks.containsKey(z, x)) {
                        for (int y = 0; y < 64; y++) {
                            blockBuffer.put(x + (y * 64) + (z * 4096), blocks.get(z, x).get(y));
                        }
                    }
                }
            }
        } else if (blocks.size() > 0) {
            blockBuffer.clear();
            blockBuffer.rewind();
            Arrays.fill(fill, (byte) dirtID);
            blockBuffer.put(fill);
        }

        blocks.clear();
        blockBuffer.rewind();
        return blockBuffer;
    }

    public double getMoisture(int posY){
        double verticality = equator.getDistance(posY);
        double mix = PrecisionMath.mix(-Math.cos(Math.toRadians(verticality*3*Math.PI*2)), -Math.cos(Math.toRadians(verticality*Math.PI*2)), 0.33);
        return PrecisionMath.toUnsignedRange(mix);
    }

    public double getOrographicEffect(double elevation, Vector wind, boolean land){
       /* float slope = land? 1.0 - dot(elevation_gradient, vec3(0,1,0)) : 0.0;
        float uphill = max(dot(wind_direction, -elevation_gradient.xz), 0.0);
        return uphill * slope * uOrograpicEffect; |*/
       return 0.0;
    }

    public double getPressure(int posX, int posY) {
        return circulation.calculateDensity(posX, posY);
    }

    public Vector getAirFlow(int posX, int posY) {
        return circulation.getAirFlow(posX, posY);
    }

    public boolean isDifferent() {
        return isDifferent;
    }

    public Collection<Location> getLocations() {
        return map.values();
    }

    public HashMap<Point, Location> getMap() {
        return map;
    }

    public WorldNoise getWorldNoise() {
        return noise;
    }
}