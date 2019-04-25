package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.Configuration;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.landmass.land.Sector;
import com.ritualsoftheold.weltschmerz.noise.ChunkNoise;
import com.ritualsoftheold.weltschmerz.noise.WorldNoise;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Weltschmerz {
    //Generate map image
    public static void main(String[] args) {
        Weltschmerz weltschmerz = new Weltschmerz();

        Configuration configuration = weltschmerz.configuration;
        BufferedImage image = new BufferedImage(configuration.width, configuration.height, BufferedImage.TYPE_INT_ARGB);

        Graphics g = image.getGraphics();
        for (Location location : weltschmerz.world.getLocations()) {
            g.setColor(location.getShape().color);
            Sector rectangle = location.getSector();
            g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
        MapIO.saveHeightmap(image);
    }

    private Configuration configuration;
    private World world;

    public Weltschmerz(){
        configuration = MapIO.loadMapConfig();
        WorldNoise noise = new WorldNoise(configuration);
        world = new World(configuration, noise);
        world.firstGeneration();
        System.out.println("Map generated");
    }

    //For future use
    public void setMaterialID(){
    }

    public int[] getChunk(int x, int y, int z, int maxBlocks){
        return generatePlains(x, y, z, maxBlocks);
    }
    public int[] generatePlains(int chunkx, int chunky, int chunkz, int maxBlocks){
        ChunkNoise noise = new ChunkNoise(world.getLocations()[0]);
        int[] blocks = new int[maxBlocks];
        if (chunky <= 130) {
            if(chunky<=70) {
                Arrays.fill(blocks, 1);
            }else{
                Arrays.fill(blocks, 2);
            }
        }else {
            for (int i = 0; i < maxBlocks; i++) {
                if(i%4096 < 64) {
                    blocks[i] = 2;
                }else{
                    int x = i%64;
                    int z = i/4096;
                    int y = (i - 4096 * z) / 64;
                    long size = Math.round(noise.getNoise(x, z));
                    if(size < y) {
                        blocks[i] = 1;
                    }else if (size == y){
                        blocks[i] = 3;
                    }else{
                        blocks[i] = 2;
                    }
                }
            }
        }
        return blocks;
    }
}
