package com.ritualsoftheold.weltschmerz.core;

public enum Shape {
    SEA(0, 0.6),
    SHORELINE(0.6, 1.6),
    PLAINS(1.6, 2.6),
    HILLS(2.6, 3.6),
    MOUNTAINS(4.6, 5.6);

    public final double min;
    public final double max;
    Shape(double min, double max){
        this.min = min;
        this.max = max;
    }
}
