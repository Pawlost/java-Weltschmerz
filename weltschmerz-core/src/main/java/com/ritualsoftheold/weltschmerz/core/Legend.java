package com.ritualsoftheold.weltschmerz.core;

import java.awt.*;

public enum Legend {
    OCEAN(0, 0, false, Color.BLUE),
    SEA(0, 0.5, false, Color.CYAN),
    SHORELINE(0.5, 1.5, true, Color.YELLOW),
    PLAIN(1.5, 2.5, true, Color.GREEN),
    HILL(2.5, 3.5, true, Color.GRAY),
    VOLCANO(4.5, 5.0, true, Color.RED),
    MOUNTAIN(4.5, 5.5, true, Color.WHITE);

    public final double min;
    public final double max;
    public final boolean land;
    public final Color color;
    Legend(double min, double max, boolean land, Color color){
        this.min = min;
        this.max = max;
        this.land = land;
        this.color = color;
    }
}
