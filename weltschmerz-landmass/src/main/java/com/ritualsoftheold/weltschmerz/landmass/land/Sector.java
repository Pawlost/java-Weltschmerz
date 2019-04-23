package com.ritualsoftheold.weltschmerz.landmass.land;

public class Sector {
    public final int x;
    public final int y;
    public final int width;
    public final int height;

    public Sector(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = x + width;
        this.height = y + height;
    }
}
