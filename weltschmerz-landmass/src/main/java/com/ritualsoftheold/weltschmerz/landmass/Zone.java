package com.ritualsoftheold.weltschmerz.landmass;

import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.noise.generators.WorldNoise;

import java.util.ArrayList;

public class Zone extends ArrayList<Location> {
    private WorldNoise worldNoise;
    private int posX;
    private int posY;
    private int posZ;

    public Zone (WorldNoise worldNoise) {
        this.worldNoise = worldNoise;
    }

    public void updatePlayerPosition(int posX, int posY, int posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public double getNoise(int x, int z){
        double voxelElevation = (worldNoise.getNoise(x + (int) posX * 64, z + (int) posZ* 64));
        if (((int)(voxelElevation/64))*64 < (posY+1)*64) {
            if (((int)(voxelElevation/64))*64 > (posY-1)*64) {
                return voxelElevation%64;
            }else{
                return 0;
            }
        }else{
            return  64;
        }
    }
}
