package com.ritualsoftheold.weltschmerz.environment;

import com.ritualsoftheold.weltschmerz.misc.units.Vector;

import java.awt.*;

public class Biom {
    public double temperature;
    public double precipitation;
    public Vector airFlow;
    public final Color color;

    public Biom(String key, int color){
        this.color = new Color(color);
    }

    public Biom(double temperature, double precipitation, Vector airFlow, int color){
        this.temperature = temperature;
        this.airFlow = airFlow;
        this.precipitation = precipitation;
        this.color = new Color(color);
    }
}
