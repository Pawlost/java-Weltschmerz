package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.environment.*;
import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;
import com.ritualsoftheold.weltschmerz.geometry.misc.Utils;
import com.ritualsoftheold.weltschmerz.geometry.units.Vector;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

public class World {

    private Equator equator;
    private Precipitation precipitation;
    private WorldNoise noise;
    private Circulation circulation;
    private int dirtID;
    private int grassID;
    private boolean isDifferent;
    private ArrayList<BiomDefinition> bioms;

    public World(Configuration configuration) {
        try {
            System.out.println("Preparation");
            this.noise = new WorldNoise(configuration);
            this.equator = new Equator(noise, configuration);
            this.circulation = new Circulation(equator);
            this.precipitation = new Precipitation(equator, circulation, noise);
            bioms = MapIO.loadBiomMap(configuration);
            System.out.println("Preparation done");
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public Biom getBiom(int posX, int posY){
        double elevation = noise.getNoise(posX, posY);
        double temperature = getTemperature(posX, posY);
        double precipitation = this.precipitation.getPrecipitation(posX, posY);
        Vector airFlow = getAirFlow(posX, posY);

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

        System.out.println(definition.key + " Biom generated at posX " + posX + " posY " + posY);

        return new Biom(temperature, precipitation, airFlow, definition,  definition.color);
    }

    public double getHumidity(int posX, int posY){
        return precipitation.getHumidity(posX, posY);
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

    public WorldNoise getWorldNoise() {
        return noise;
    }
}