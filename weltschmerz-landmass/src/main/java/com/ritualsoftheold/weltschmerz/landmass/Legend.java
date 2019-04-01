package com.ritualsoftheold.weltschmerz.landmass;

import java.awt.*;

public enum Legend {
    OCEAN(0, 0, false, 0, Color.BLUE),
    SEA(0, 0.5, false, 1,Color.CYAN),
    SHORELINE(0.5, 1.5, true,2, Color.YELLOW),
    PLAIN(1.5, 2.5, true, 3, Color.GREEN),
    HILL(2.5, 3.5, true, 4, Color.ORANGE),
    VOLCANO(4.5, 5.0, true, 6, Color.RED),
    MOUNTAIN(4.5, 5.5, true, 5, Color.GRAY);

    public final double min;
    public final double max;
    public final boolean land;
    public final int position;
    public final Color color;
    Legend(double min, double max, boolean land, int position, Color color){
        this.min = min;
        this.max = max;
        this.land = land;
        this.position = position;
        this.color = color;
    }
}
