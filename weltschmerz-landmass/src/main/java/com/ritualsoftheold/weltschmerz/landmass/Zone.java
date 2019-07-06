package com.ritualsoftheold.weltschmerz.landmass;

import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.noise.generators.WorldNoise;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class Zone extends ArrayList<Location> {
    private WorldNoise worldNoise;
    private int dirtID;
    private int grassID;

    public Zone (WorldNoise worldNoise) {
        this.worldNoise = worldNoise;
    }

    public ByteBuffer getChunk(int posX, int posY, int posZ, int bufferSize) {
        ByteBuffer blockBuffer = ByteBuffer.allocate(bufferSize);


        for(int x = 0; x < 64; x++){
            for(int z = 0; z < 64; z++) {
                int y = (int) Math.round(worldNoise.getNoise(x + posX * 64, z + posZ * 64));

                if(y/64 > posY/64) {
                    byte[] underArray = new byte[64];
                    Arrays.fill(underArray, (byte) dirtID);
                    blockBuffer.put(underArray);
                }else  if(y/64 == posY/64) {
                    byte[] underArray = new byte[(y - posY) - 1];
                    Arrays.fill(underArray, (byte) dirtID);
                    blockBuffer.put(underArray);
                    blockBuffer.put((byte) grassID);
                    byte[] upperArray = new byte[64-(y - posY)];
                    Arrays.fill(upperArray, (byte) 1);
                    blockBuffer.put(upperArray);
                }else {
                    byte[] upperArray = new byte[64];
                    Arrays.fill(upperArray, (byte) 1);
                    blockBuffer.put(upperArray);
                }
            }
        }

        blockBuffer.rewind();
        return blockBuffer;
    }

    public void setMaterials(int dirtID, int grassID){
        this.dirtID = dirtID;
        this.grassID = grassID;
    }
}
