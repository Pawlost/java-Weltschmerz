package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.environment.Biom;

import com.typesafe.config.Config;
import xerial.larray.LByteArray;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class Weltschmerz {
    //Generate map image
    public static void main(String[] args) {
        Weltschmerz weltschmerz = new Weltschmerz();

        Config configuration = weltschmerz.configuration;

        int latitude = configuration.getInt("map.latitude");
        int longitude = configuration.getInt("map.longitude");

        BufferedImage image = new BufferedImage(longitude, latitude, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < longitude; x++) {
            for (int y = 0; y < latitude; y++) {
                Biom biom = weltschmerz.world.getBiom(x, y);
                image.setRGB(x, y, biom.color.getRGB());
            }
        }
        MapIO.saveImage(image);
    }

    private Config configuration;
    public final World world;

    public Weltschmerz() {
            configuration = MapIO.loadMapConfig();
            world = new World(configuration);
    }

    public LByteArray getChunk(int x, int y, int z, LByteArray chunk) {
        x = x/16;
        z = z/16;
        y = y * 4;
        ByteBuffer blockBuffer = world.getChunk(x, y, z, (int)chunk.size());
        chunk.write(blockBuffer);
        return chunk;
    }

    public boolean isDifferent(){
        return world.isDifferent();
    }

    public void setMaterialID(int grassID, int dirtID) {
        world.setMaterials(dirtID, grassID);
    }

    public Config getConfiguration() {
        return configuration;
    }
}
