package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.WeltschmerzNoise;
import java.util.concurrent.ThreadLocalRandom;

public class Location {
    private Shape shape;
    private int startx;
    private int starty;
    private int endx;
    private int endy;
    private double[][] terrain;

    public Location(int startx, int starty, int endx, int endy, Shape shape){
        this.startx = startx;
        this.starty = starty;
        this.endx = endx;
        this.endy = endy;
        this.shape = shape;
        terrain = new double[getHeight()][getWidth()];
        terrain = new double[getHeight()][getWidth()];
    }

    public void fill(WeltschmerzNoise ma){
        ThreadLocalRandom  random = ThreadLocalRandom.current();
        Shape randShape = Shape.SEA;
        int middlex = getWidth()/2;
        int middley = getHeight()/2;
        int startx = 0;
        int starty = 0;
        while (shape != Generation.getPrevious(randShape))  {
            int layer = Generation.getNumShapes(randShape);
            int maxposx = getWidth()/layer;
            int maxposy = getHeight()/layer;
            startx += middlex - maxposx/2;
            starty += middley - maxposy/2;
            for (int y = 0; y < maxposy - 1; y++) {
                for (int x = 0; x < maxposx - 1; x++) {
                    double r = ThreadLocalRandom.current().nextDouble(randShape.min, randShape.max);
                    terrain[y + starty][x + startx] = r;
                }
            }
            middlex = maxposx/2;
            middley = maxposy/2;
            randShape = Generation.getNext(randShape);
        }
    }

    public int getWidth(){
        return endx - startx;
    }

    public int getHeight(){
        return endy - starty;
    }

    public Shape getShape(){
        return shape;
    }

    public int getStartx(){
        return startx;
    }

    public int getStarty() {
        return starty;
    }

    public int getEndx(){
        return endx;
    }

    public int getEndy() {
        return endy;
    }

    public double[][] getTerrain() {
        return terrain;
    }
}
