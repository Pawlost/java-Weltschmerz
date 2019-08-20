package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.environment.*;
import com.ritualsoftheold.weltschmerz.misc.misc.Constants;
import com.ritualsoftheold.weltschmerz.misc.misc.Utils;
import com.ritualsoftheold.weltschmerz.misc.units.Vector;
import com.typesafe.config.Config;
import squidpony.squidmath.XoRoRNG;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.*;

public class World {

    public final Config config;
    private Equator equator;
    private Precipitation precipitation;
    private WorldNoise noise;
    private Circulation circulation;
    private int dirtID;
    private int grassID;
    private int grassMeshID;
    private boolean isDifferent;
    private Biom[][] bioms;
    private static final String EARTH_FILE = "earth.png";

    private int treeDistanceX;
    private int treeDistanceY;
    private int treeDistanceZ;
    private byte treeID;
    private XoRoRNG xoRoRNG;

    public World(Config config) {
        System.out.println("Preparation");
        this.config = config;
        BufferedImage earth = MapIO.loadMap(EARTH_FILE);
        this.noise = new WorldNoise(config, earth);
        this.equator = new Equator(config);
        this.circulation = new Circulation(equator, noise, config);
        this.precipitation = new Precipitation(equator, circulation, noise, config);
        this.xoRoRNG = new XoRoRNG(config.getLong("map.seed"));
        bioms = MapIO.loadBiomMap(config);
        System.out.println("Preparation done");
    }

    void setMaterials(int dirtID, int grassID, int grassMeshID) {
        this.dirtID = dirtID;
        this.grassID = grassID;
        this.grassMeshID = grassMeshID;
    }

    void setObject(int treeDistanceX, int treeDistanceY, int treeDistanceZ, byte value) {
        this.treeDistanceX = treeDistanceX;
        this.treeDistanceY = treeDistanceY;
        this.treeDistanceZ = treeDistanceZ;

        this.treeID = value;
    }

    ByteBuffer getChunk(int posX, int posY, int posZ, int bufferSize) {
        ByteBuffer blockBuffer = ByteBuffer.allocate(bufferSize);
        byte[] fill = new byte[bufferSize];
        Arrays.fill(fill, (byte) 1);
        blockBuffer.put(fill);

        isDifferent = false;

        if(posX >= 5 && posY/64 >= 0 && posZ >= 5 && treeDistanceX > 0 && treeDistanceY > 0 && treeDistanceZ > 0) {

            int sizeX;
            boolean accross = false;
            if (treeDistanceX / 64 > 0) {
                sizeX = 64;
                treeDistanceX -= 64;
                accross = true;
            } else {
                sizeX = treeDistanceX % 64;
            }

            int sizeY;
            if (treeDistanceY / 64 > 0) {
                sizeY = 64;
                treeDistanceY -= 64;
                accross = true;
            } else {
                sizeY = treeDistanceY % 64;
            }

            int sizeZ;
            if (treeDistanceZ / 64 > 0) {
                sizeZ = 64;
                treeDistanceZ -= 64;
                accross = true;
            } else {
                sizeZ = treeDistanceZ % 64;
            }

            if (!accross) {
                treeDistanceX -= treeDistanceX % 64;
                treeDistanceY -= treeDistanceY % 64;
                treeDistanceZ -= treeDistanceZ % 64;
            }

            int additionY = 0;
            for (int z = 0; z < sizeZ; z++) {
                for (int x = 0; x < sizeX; x++) {
                    int elevation = (int) Math.round(noise.getNoise(x + posX * 64, z + posZ * 64));
                    if (posY / 64 == elevation / 64) {
                        elevation += 1;
                        if(elevation%64 > additionY){
                            additionY = elevation%64;
                        }
                        for (int y = elevation % 64; y < 64; y++) {
                            blockBuffer.put(x + (y * 64) + (z * 4096), treeID);
                        }
                    } else {
                        for (int y = 0; y < sizeY; y++) {
                            blockBuffer.put(x + (y * 64) + (z * 4096), treeID);
                        }
                    }
                }
            }
            treeDistanceY += additionY;
            isDifferent = true;
        }

        for (int z = 0; z < 64; z++) {
            for (int x = 0; x < 64; x++) {
                int elevation = (int) Math.round(noise.getNoise(x + posX * 64, z + posZ * 64));
                for (int y = 0; y < 64; y++) {
                    if ((elevation / 64) > (posY / 64)) {
                        blockBuffer.put(x + (y * 64) + (z * 4096), (byte) dirtID);
                    } else if (elevation / 64 == (posY / 64)) {
                        if (Math.abs(elevation % 64) >= y) {
                            blockBuffer.put(x + (y * 64) + (z * 4096), (byte) dirtID);
                            isDifferent = true;
                        }
                    }
                }
                if (isDifferent) {
                    blockBuffer.put(x + Math.abs((elevation % 64) * 64) + (z * 4096), (byte) grassID);
                    if(xoRoRNG.nextBoolean()) {
                      blockBuffer.put(x + Math.abs(((elevation + 1)% 64) * 64) + (z * 4096), (byte) grassMeshID);
                    }
                }
            }
        }

        blockBuffer.rewind();
        return blockBuffer;
    }

    public Biom getBiom(int posX, int posY) {
        double elevation = noise.getNoise(posX, posY);
        double temperature = equator.getTemperature(posY, elevation);
        Vector airFlow = circulation.getAirFlow(posX, posY);
        double precipitation = this.precipitation.getPrecipitation(posX, posY, elevation, temperature, airFlow);

        Biom biom = null;
        int y = (int) (precipitation * (1000 / Constants.MAXIMUM_PRECIPITATION));
        if (temperature >= 0 && temperature < 40) {
            int x = (int) ((temperature * 20) + Constants.MAXIMUM_TEMPERATURE_DIFFERENCE);
            biom = bioms[x][y];
        } else if (temperature > -10) {
            int x = (int) (Constants.MAXIMUM_TEMPERATURE_DIFFERENCE / Math.max(Math.abs(temperature), 1));
            biom = bioms[x][y];
        }

        if (biom == null || !Utils.isLand(elevation)) {
            biom = selectDefault(temperature, elevation);
        }

        return biom;
    }

    private static Biom selectDefault(double temperature, double elevation) {
        if (elevation <= 0) {
            if (elevation < Constants.OCEAN_DEPTH) {
                return new Biom("OCEAN", Integer.parseInt("000066", 16));
            } else {
                return new Biom("SEA", Integer.parseInt("0099FF", 16));
            }
        } else {
            if (temperature <= 0) {
                return new Biom("ICELAND", Integer.parseInt("FFFFFF", 16));
            } else {
                return new Biom("DESERT", Integer.parseInt("FFCC00", 16));
            }
        }
    }

    public double getPressure(int posX, int posY) {
        return circulation.calculateDensity(posX, posY);
    }

    boolean isDifferent() {
        return isDifferent;
    }

    public double getMoisture(int posY) {
        return precipitation.getMoisture(posY);
    }

    public double getElevation(int posX, int posY) {
        return noise.getNoise(posX, posY);
    }

    public WorldNoise getWorldNoise() {
        return noise;
    }

    public void changeConfiguration(Config config) {
        this.equator.changeConfiguration(config);
        this.noise.changeConfiguration(config);
        this.circulation.changeConfiguration(config);
        this.precipitation.changeConfiguration(config);
    }
}