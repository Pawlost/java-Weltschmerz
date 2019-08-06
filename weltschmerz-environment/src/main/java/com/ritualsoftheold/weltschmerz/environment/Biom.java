package com.ritualsoftheold.weltschmerz.environment;

import com.ritualsoftheold.weltschmerz.geometry.units.Vector;

import java.awt.*;

public class Biom {
    public final double temperature;
    public final double precipitation;
    public final Vector airFlow;
    public final Color color;
    public final BiomDefinition biomDefinition;

    public Biom(double temperature, double precipitation, Vector airFlow, BiomDefinition biomDefinition, int color){
        this.temperature = temperature;
        this.airFlow = airFlow;
        this.precipitation = precipitation;
        this.color = new Color(color);
        this.biomDefinition = biomDefinition;
    }
}
