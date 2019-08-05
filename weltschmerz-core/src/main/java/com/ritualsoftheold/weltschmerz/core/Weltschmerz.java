package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;

import xerial.larray.LByteArray;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class Weltschmerz {
    //Generate map image
    public static void main(String[] args) {
        Weltschmerz weltschmerz = new Weltschmerz();

        Configuration configuration = weltschmerz.configuration;
        BufferedImage image = new BufferedImage(configuration.longitude, configuration.latitude, BufferedImage.TYPE_INT_ARGB);

        Graphics g = image.getGraphics();
        for (Location location : weltschmerz.world.getLocations()) {
            g.setColor(location.getShape().color);
            g.drawPolygon(location.position.getSwingPolygon());
            g.setColor(Color.RED);
            g.drawOval((int) location.position.center.x, (int) location.position.center.y, 2, 2);
        }
        MapIO.saveImage(image);
    }

    private Configuration configuration;
    public final World world;

    public Weltschmerz() {
        configuration = MapIO.loadMapConfig();
        world = new World(configuration);
        System.out.println("Map generated");
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
