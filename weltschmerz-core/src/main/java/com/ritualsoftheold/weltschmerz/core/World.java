package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.WeltschmerzNoise;
import com.sudoplay.joise.module.ModuleAutoCorrect;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class World extends ArrayList<Place> {
    private int width;
    private int height;
    private WeltschmerzNoise noise;

    public World(int width, int height, int seed, int octaves, int freqeuncy) {
        noise = new WeltschmerzNoise(seed, octaves, freqeuncy);

        this.width = width;
        this.height = height;
    }

    public void generateWorld(int maxContinentSize) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int width = this.width;
        int height = this.height;

        while (width > 0 && height > 0) {
            int continentStructure = random.nextInt(1, maxContinentSize);
            int startx = random.nextInt(this.width-1);
            int starty = random.nextInt(this.height-1);
            int endx = width + 1;
            int endy = height + 1;
                endx = startx + random.nextInt(2, this.width - startx - 1);
                endy = starty + random.nextInt(2, this.height - starty - 1);

            Shape shape = Generation.getRandomShape(random.nextInt(1, Generation.SHAPES));
            for (int i = 0; i <= continentStructure; i++) {
                if (width > endx - startx && height > endy - starty) {
                    Place place = new Place(startx, starty, endx, endy, shape);
                    place.fill(noise);
                    this.add(place);

                    width -= place.getWidth();
                    height -= place.getHeight();

                    //  int direction = random.nextInt(4);

                        startx = endx;
                        starty = endy;
                    if(this.width - startx - 1 > 1 && this.height - starty - 1 > 1) {
                        endx = startx + random.nextInt(1, this.width - startx - 1 );
                        endy = starty + random.nextInt(1, this.height - starty - 1);

                        shape = Generation.getNext(place.getShape(), random.nextBoolean());
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
        for (Place place : this) {
            double z = 0;
            System.out.println(place.getHeight());
            System.out.println(place.getWidth());
            for (int y = 0; y < place.getHeight() - 1; y++) {
                for (int x = 0; x < place.getWidth() - 1; x++) {
                    worldMap[y + place.getStarty()][x + place.getStartx()] = place.getTerrain()[y][x];
                }
            }
        }
        return worldMap;
    }
}
