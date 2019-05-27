package com.ritualsoftheold.weltschmerz.landmass;

import java.awt.*;

public class Shape {
    public final double min;
    public final double max;
    public final int position;

    public final boolean land;
    public final String key;
    public final Color color;

    public Shape(double min, double max) {
        this(min, max, 0, false, "", Color.WHITE);
    }
    public Shape(double min, double max, int position, boolean land, String key, Color color){
        this.max = max;
        this.min = min;
        this.position = position;
        this.land = land;
        this.key = key;
        this.color = color;
    }
}
