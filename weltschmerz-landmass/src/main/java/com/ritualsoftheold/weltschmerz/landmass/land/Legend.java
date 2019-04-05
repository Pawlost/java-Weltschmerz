package com.ritualsoftheold.weltschmerz.landmass.land;

import java.awt.*;

public enum Legend {
    OCEAN(0, 3, false, 0, Color.BLUE),
    SEA(3, 3.5, false, 1,Color.CYAN),
    SHORELINE(3.5, 4, true,2, Color.YELLOW),
    PLAIN(4, 6, true, 3, Color.GREEN),
    HILL(6, 6.5, true, 4, Color.ORANGE),
    VOLCANO(4.5, 5.0, true, 6, Color.RED),
    MOUNTAIN(6.5, 6.6, true, 5, Color.GRAY);

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
