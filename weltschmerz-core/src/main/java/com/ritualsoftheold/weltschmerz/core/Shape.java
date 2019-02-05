package com.ritualsoftheold.weltschmerz.core;

public enum Shape {

    SEA(0.5, 1.6),
    SHORELINE(1.6, 2.6),
    PLAINS(2.6, 3.6),
    HILLS(4.6, 5.6),
    MOUNTAINS(6.6, 10.0);

    public final double min;
    public final double max;
    Shape(double min, double max){
        this.min = min;
        this.max = max;
    }
}
