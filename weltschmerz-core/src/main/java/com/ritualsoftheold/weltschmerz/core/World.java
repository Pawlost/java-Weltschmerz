package com.ritualsoftheold.weltschmerz.core;

import com.google.common.collect.HashMultimap;
import com.ritualsoftheold.weltschmerz.environment.*;
import com.ritualsoftheold.weltschmerz.misc.misc.Utils;
import com.ritualsoftheold.weltschmerz.misc.units.Vector;
import com.typesafe.config.Config;
import org.apache.commons.collections4.map.MultiKeyMap;

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
    private byte[][][] tree;
    private boolean isDifferent;
    private ArrayList<BiomDefinition> bioms;
    private static final String EARTH_FILE =  "earth.png";

    public World(Config config) {
        System.out.println("Preparation");
        this.config = config;
        BufferedImage earth = MapIO.loadMap(EARTH_FILE);
        this.noise = new WorldNoise(config, earth);
        this.equator = new Equator(config);
        this.circulation = new Circulation(equator, noise, config);
        this.precipitation = new Precipitation(equator, circulation, noise, config);
        bioms = MapIO.loadBiomMap(config);
        System.out.println("Preparation done");
    }

    void setMaterials(int dirtID, int grassID, int grassMeshID) {
        this.dirtID = dirtID;
        this.grassID = grassID;
        this.grassMeshID = grassMeshID;
    }

    void setObject(byte[][][] tree){
        this.tree = tree;
    }

    ByteBuffer getChunk(int posX, int posY, int posZ, int bufferSize) {
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
                    y = Math.abs(y%64);
                    byte[] underArray = new byte[(y)];
                    Arrays.fill(underArray, (byte) dirtID);
                    bb.put(underArray);

                    byte[] upperArray = new byte[64 - y];
                    Arrays.fill(upperArray, (byte) 1);
                    bb.put(upperArray);

                    bb.put(y, (byte) grassID);
                    bb.put(y + 1, (byte) grassMeshID);

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

            if (posX == 1 && posZ == 1){
                int posy = (int) Math.round(noise.getNoise(posX * 64, posZ * 64));
                if(posy / 64 == posY / 64) {
                    for (int z = 0; z < tree.length; z++) {
                        for (int y = 0; y < tree[z].length; y++) {
                            for (int x = 0; x < tree[z][y].length; x ++) {
                                blockBuffer.put(x + ((y + posy) * 64) + (z * 4096), tree[z][y][x]);
                            }
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

    public Biom getBiom(int posX, int posY){
        double elevation = noise.getNoise(posX, posY);
        double temperature = equator.getTemperature(posY, elevation);
        Vector airFlow = circulation.getAirFlow(posX, posY);
        double precipitation = this.precipitation.getPrecipitation(posX, posY, elevation, temperature, airFlow);

        BiomDefinition definition = null;
        if (Utils.isLand(elevation)) {
            for (BiomDefinition biom : bioms) {
                if (biom.define(precipitation, temperature)) {
                    definition = biom;
                }
            }
        }

        if(definition == null){
            definition = BiomDefinition.selectDefault(temperature, elevation);
        }

        return new Biom(temperature, precipitation, airFlow, definition,  definition.color);
    }
    
    public double getPressure(int posX, int posY) {
        return circulation.calculateDensity(posX, posY);
    }

    boolean isDifferent() {
        return isDifferent;
    }

    public double getMoisture(int posY){
        return precipitation.getMoisture(posY);
    }

    public double getElevation(int posX, int posY){
        return noise.getNoise(posX, posY);
    }

    public WorldNoise getWorldNoise() {
        return noise;
    }

    public void changeConfiguration(Config config){
        this.equator.changeConfiguration(config);
        this.noise.changeConfiguration(config);
        this.circulation.changeConfiguration(config);
        this.precipitation.changeConfiguration(config);
    }
}