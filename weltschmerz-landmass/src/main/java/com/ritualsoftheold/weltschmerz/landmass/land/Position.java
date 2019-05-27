package com.ritualsoftheold.weltschmerz.landmass.land;

public class Position {
    public final int x;
    private double y;
    public final int z;
    public final int width;
    public final int height;

    public Position(int x, int z, int width, int height){
        this.x = x;
        this.z = z;
        this.width = x + width;
        this.height = z + height;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }
}
