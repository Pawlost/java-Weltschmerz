package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.Zone;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;

import com.ritualsoftheold.weltschmerz.noise.generators.WorldNoise;
import scala.Int;
import xerial.larray.LArray;
import xerial.larray.LByteArray;
import xerial.larray.LIntArray;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
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
            g.drawPolygon(location.position.getSwingPolygon());
            g.setColor(Color.RED);
            g.drawOval((int) location.position.center.x, (int) location.position.center.y, 2, 2);
        }
        MapIO.saveHeightmap(image);
    }

    private Configuration configuration;
    public final World world;
    private int grassID;
    private int dirtID;
    private Zone zone;

    public Weltschmerz() {
        configuration = MapIO.loadMapConfig();
        WorldNoise noise = new WorldNoise(configuration);
        world = new World(configuration, noise);
        zone = new Zone(noise);
        System.out.println("Map generated");
    }

    public LByteArray getChunk(int x, int y, int z, LByteArray chunk) {
        x = x/16;
        z = z/16;
        y = y * 4;
        ByteBuffer blockBuffer = zone.getChunk(x, y, z, (int)chunk.size());
        chunk.write(blockBuffer);
        return chunk;
    }

    public boolean isDifferent(){
        return zone.isDifferent();
    }

    //For future use
    public void setMaterialID(int grassID, int dirtID) {
        zone.setMaterials(dirtID, grassID);
    }
}
