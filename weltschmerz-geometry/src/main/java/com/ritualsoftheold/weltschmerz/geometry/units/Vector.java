package com.ritualsoftheold.weltschmerz.geometry.units;

public class Vector {
    public final double x;
    public final double y;
    public final double z;
    public final double w;

    public Vector(double x, double y) {
        this(x, y, 0.0, 0.0);
    }

    public Vector(double x, double y, double z) {
        this(x, y, z, 0.0);
    }

    public Vector(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public double getLength() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2) + Math.pow(w, 2));
    }
}