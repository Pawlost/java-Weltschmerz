package com.ritualsoftheold.weltschmerz.core;

import com.sudoplay.joise.module.ModuleAutoCorrect;

import java.util.concurrent.ThreadLocalRandom;

public class Location {
    private Shape shape;
    private int innerx;
    private int innery;
    private int width;
    private int height;
    private int outerx;
    private int outery;
    private int outerendx;
    private int outerendy;
    private double[][] terrain;

    public Location(int startx, int starty, int width, int height, Shape shape){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        this.innerx = startx;
        this.innery = starty;
        this.width = width;
        this.height = height;
        this.shape = shape;
        terrain = new double[getHeight()][getWidth()];
//          outerx = random.nextInt(startx + 1, startx + width -2);
 //      outery = random.nextInt(starty + 1, starty + height -2);
    //   outerendx = random.nextInt(outerx, outerx + width -1);
    //   outerendy = random.nextInt(outery, outery + height -1);
    }

    public void fill(ModuleAutoCorrect mod){
        Shape randShape = Shape.SEA;
        int middlex = width/2;
        int middley = height/2;
        int startx = 0;
        int starty = 0;

        while (shape != Generation.getPrevious(randShape)) {
            int layer = Generation.getNumShapes(randShape);
            int maxposx = width / layer;
            int maxposy = height / layer;
            startx += middlex - maxposx / 2;
            starty += middley - maxposy / 2;
            for (int y = starty; y < maxposy - 1; y++) {
                for (int x = startx; x < maxposx - 1; x++) {
                    double r = 1.0;
                    if(randShape != Shape.SEA) {
                       r = mod.get(y, x);
                    }
                    terrain[y + starty][x + startx] = r + randShape.min;
                }
            }
            middlex = maxposx / 2;
            middley = maxposy / 2;
            randShape = Generation.getNext(randShape);
        }
        makeContinent();
    }

    private void makeContinent(){

    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public Shape getShape(){
        return shape;
    }

    public int getStartx(){
        return innerx;
    }

    public int getStarty() {
        return innery;
    }

    public double[][] getTerrain() {
        return terrain;
    }
}
