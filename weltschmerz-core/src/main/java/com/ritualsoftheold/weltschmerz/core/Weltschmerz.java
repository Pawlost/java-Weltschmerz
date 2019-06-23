package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.landmass.Zone;
import com.ritualsoftheold.weltschmerz.landmass.land.Location;
import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;

import com.ritualsoftheold.weltschmerz.noise.generators.WorldNoise;

import java.awt.*;
import java.awt.image.BufferedImage;

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
    private Zone currentZone;
    private WorldNoise noise;
    private int posY;

    public Weltschmerz() {
        configuration = MapIO.loadMapConfig();
        noise = new WorldNoise(configuration);
        world = new World(configuration, noise);
        currentZone = new Zone(noise);
        System.out.println("Map generated");
    }

    public void setChunk(int x, int y, int z) {
        x = x/16;
        z = z/16;
        posY = y/16;
        currentZone.updatePlayerPosition(x, posY, z);
    }

    //For future use
    public void setMaterialID(int grassID, int dirtID) {
        this.grassID = grassID;
        this.dirtID = dirtID;
    }

    public int generateVoxel(int x, int y, int z) {
       if(posY > noise.getMax()){
          return  1;
       }

        if(posY < noise.getMin()){
            return  dirtID;
        }

        long size = Math.round(currentZone.getNoise(x, z));

        if(size == 0){
            return 1;
        }

        if (size > y) {
            return dirtID;
        } else if (size == y) {
            return grassID;
        }else {
            return 1;
        }
    }
}
