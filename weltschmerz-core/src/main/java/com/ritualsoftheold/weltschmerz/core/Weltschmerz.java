package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.environment.Biom;
import com.ritualsoftheold.weltschmerz.misc.misc.Configuration;

import xerial.larray.LByteArray;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class Weltschmerz {
    //Generate map image
    public static void main(String[] args) {
        Weltschmerz weltschmerz = new Weltschmerz();

        Configuration configuration = weltschmerz.configuration;
        BufferedImage image = new BufferedImage(configuration.longitude, configuration.latitude, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < configuration.longitude; x++) {
            for (int y = 0; y < configuration.latitude; y++) {
                Biom biom = weltschmerz.world.getBiom(x, y);
                image.setRGB(x, y, biom.color.getRGB());
            }
        }
        MapIO.saveImage(image);
    }

    private Configuration configuration;
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

    public Configuration getConfiguration() {
        return configuration;
    }
}
