package com.ritualsoftheold.weltschmerz.landmass;

import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.noise.generators.WorldNoise;
import org.apache.commons.collections4.map.MultiKeyMap;

import java.util.ArrayList;

public class Zone extends ArrayList<Location> {
    private WorldNoise worldNoise;
    private MultiKeyMap<Integer, Integer> materials;
    private int dirtID;
    private int grassID;

    public Zone (WorldNoise worldNoise) {
        this.worldNoise = worldNoise;
        materials = new MultiKeyMap<>();
    }

    public void updatePlayerPosition(int posX, int posZ) {
        for(int x = 0; x < 64; x++){
            for(int z = 0; z < 64; z++){
                int y = (int)Math.round(worldNoise.getNoise(x + (int) posX * 64, z + (int) posZ* 64));
                materials.put(x, z, y);
            }
        }
    }

    public void setMaterials(int dirtID, int grassID){
        this.dirtID = dirtID;
        this.grassID = grassID;
    }

    public int getNoise(int x, int y, int z){
        int posY = materials.get(x, z);
        if(posY > y){
            return dirtID;
        }else if(posY < y){
            return 1;
        }else{
            return grassID;
        }
    }
}
