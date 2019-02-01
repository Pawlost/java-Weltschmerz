package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.WeltschmerzNoise;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class World extends ArrayList<Location> {
    private int width;
    private int height;
    private WeltschmerzNoise noise;

    public World(int width, int height, int seed, int octaves, int freqeuncy) {
       // noise = new WeltschmerzNoise(seed, octaves, freqeuncy);
        this.width = width;
        this.height = height;
    }

    public void generateWorld(int maxContinentSize) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int width = this.width;
        int height = this.height;

        while (width > 0 && height > 0) {

            int continentStructure = random.nextInt(1, maxContinentSize);
            int startx = random.nextInt((this.width-1)/2);
            int starty = random.nextInt((this.height-1)/2);

            int endx = startx + random.nextInt(2, this.width - startx - 1);
            int endy = starty + random.nextInt(2, this.height - starty - 1);

            Shape shape = Generation.getRandomShape(random.nextInt(1, Generation.SHAPES));
            for (int i = 0; i <= continentStructure; i++) {
                if (width > endx - startx && height > endy - starty) {
                    Location location = new Location(startx, starty, endx, endy, shape);
                    location.fill(noise);
                    this.add(location);

                    width -= location.getWidth();
                    height -= location.getHeight();

                    if(this.width - startx - 1 > 1 && this.height - starty - 1 > 1) {
                        endx = startx + random.nextInt(1, this.width - startx - 1 );
                        endy = starty + random.nextInt(1, this.height - starty - 1);

                        shape = Generation.getNext(location.getShape());
                    }else{
                        break;
                    }
                }else{
                    width = 0;
                    height = 0;
                }
            }
        }
    }

    public double[][] getMap() {
        double[][] worldMap = new double[height][width];
        for (Location location : this) {
            for (int y = 0; y < location.getHeight() - 1; y++) {
                for (int x = 0; x < location.getWidth() - 1; x++) {
                    worldMap[y + location.getStarty()][x + location.getStartx()] = location.getTerrain()[y][x];
                }
            }
        }
        return worldMap;
    }
}
