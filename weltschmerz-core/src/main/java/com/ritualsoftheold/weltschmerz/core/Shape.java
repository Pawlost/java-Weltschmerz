package com.ritualsoftheold.weltschmerz.core;

public enum Shape {

    SEA(0.5, 1.0),
    SHORELINE(1.0, 1.5),
    PLAINS(1.5, 3.0),
    HILLS(3.0, 4.0),
    MOUNTAINS(4.0, 5.0);

    public final double min;
    public final double max;
    Shape(double min, double max){
        this.min = min;
        this.max = max;
    }
}
