package com.ritualsoftheold.weltschmerz.landmass;

import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.noise.generators.WorldNoise;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.nio.ByteBuffer;
import java.util.*;

public class Zone extends ArrayList<Location> {
    private WorldNoise worldNoise;
    private int dirtID;
    private int grassID;
    private boolean isDifferent;


    public Zone (WorldNoise worldNoise) {
        this.worldNoise = worldNoise;
    }

    public ByteBuffer getChunk(int posX, int posY, int posZ, int bufferSize) {
        ByteBuffer blockBuffer = ByteBuffer.allocate(bufferSize);
        byte[] fill = new byte[bufferSize];
        Arrays.fill(fill, (byte) 1);
        blockBuffer.put(fill);

        MultiKeyMap<Integer, ByteBuffer> blocks = new MultiKeyMap<>();
        isDifferent = false;

        for(int z = 0; z < 64; z++) {
            for (int x = 0; x < 64; x++) {
                ByteBuffer bb = ByteBuffer.allocate(64);
                int y = (int) Math.round(worldNoise.getNoise(x + posX * 64, z + posZ * 64));
                if (y / 64 > posY / 64) {
                    byte[] underArray = new byte[64];
                    Arrays.fill(underArray, (byte) dirtID);
                    bb.put(underArray);
                    blocks.put(z, x, bb);
                }else if(y / 64 == posY / 64){
                    byte[] underArray = new byte[(y%64)];
                    Arrays.fill(underArray, (byte) dirtID);
                    bb.put(underArray);

                    byte[] upperArray = new byte[64-(y%64)];
                    Arrays.fill(upperArray, (byte) 1);
                    bb.put(upperArray);

                    bb.put(y%64, (byte) grassID);

                    isDifferent = true;
                    blocks.put(z, x, bb);
                }
            }
        }

        if(isDifferent) {
            for (int z = 0; z < 64; z++) {
                for (int x = 0; x < 64; x++) {
                    if (blocks.containsKey(z, x)) {
                        for (int y = 0; y < 64; y++) {
                            blockBuffer.put(x + (y * 64) + (z * 4096), blocks.get(z, x).get(y));
                        }
                    }
                }
            }
        }else if(blocks.size() > 0){
            blockBuffer.clear();
            blockBuffer.rewind();
            Arrays.fill(fill, (byte) dirtID);
            blockBuffer.put(fill);
        }

        blocks.clear();
        blockBuffer.rewind();
        return blockBuffer;
    }

    public void setMaterials(int dirtID, int grassID){
        this.dirtID = dirtID;
        this.grassID = grassID;
    }

    public boolean isDifferent(){
        return isDifferent;
    }
}
