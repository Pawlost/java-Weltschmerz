package com.ritualsoftheold.weltschmerz.core;

import com.ritualsoftheold.weltschmerz.WeltschmerzNoise;
import com.sudoplay.joise.module.ModuleAutoCorrect;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Place{
    private Shape shape;
    private int startx;
    private int starty;
    private int endx;
    private int endy;
    private int size;
    private double[][] terrain;

    public Place(int startx, int starty, int endx, int endy, Shape shape){
        this.startx = startx;
        this.starty = starty;
        this.endx = endx;
        this.endy = endy;
        this.shape = shape;
        terrain = new double[getHeight()][getWidth()];
    }

    public void fill(WeltschmerzNoise ma){
        for(int y = 0; y < getHeight(); y++){
            for(int x = 0; x < getWidth(); x++){
                double r = ThreadLocalRandom.current().nextDouble(shape.min, shape.max);
                terrain[y][x] = r;
            }
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
