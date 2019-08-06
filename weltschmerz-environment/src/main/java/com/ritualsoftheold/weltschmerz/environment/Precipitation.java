package com.ritualsoftheold.weltschmerz.environment;

import com.ritualsoftheold.weltschmerz.geometry.misc.Utils;
import com.ritualsoftheold.weltschmerz.geometry.units.Vector;
import jdk.jshell.execution.Util;

public class Precipitation {

    private static final double TRANSPIRATION = 0.5;
    private static final double CIRCULATION = 0.5;
    private static final double EVAPORATION = 1.0;
    private static final  double ORTOGRAPHICEFFECT = 1.0;
    private static final double INTESITY = 1.0;
    private static final int ITERATION = 1;


    private Equator equator;
    private Circulation circulation;
    private WorldNoise worldNoise;
    private double orographicEffect;
    private double intensity;


    public Precipitation(Equator equator, Circulation circulation, WorldNoise worldNoise){
        this.equator = equator;
        this.circulation = circulation;
        this.worldNoise =worldNoise;
    }

    public double getMoisture(int posY){
        double verticality = equator.getDistance(posY);
        double mix = Utils.mix(-Math.cos(Math.toRadians(verticality*3*Math.PI*2)), -Math.cos(Math.toRadians(verticality*Math.PI*2)), 0.33);
        return Utils.toUnsignedRange(mix);
    }

    public double getPrecipitation(int posX, int posY){
        double humidity = getHumidity(posX, posY);
        double temperature = equator.getTemperature(posX, posY);
        double estimated = (1.0 - CIRCULATION) * getBasePrecipitation(posY);
        double simulated = (2.0 * CIRCULATION) * (temperature + orographicEffect) * humidity;

        return intensity * (estimated + simulated);
    }

    public double getHumidity(int posX, int posY){
        double elevation = worldNoise.getNoise(posX, posY);
        boolean isLand = Utils.isLand(elevation);
        double humidity = getEvapotranspiration(posY, isLand);
        Vector wind = circulation.getAirFlow(posX, posY);

        orographicEffect = getOrographicEffect(elevation, wind, isLand);
        double inverseOrographicEffect = 1.0 - orographicEffect;

        intensity = isLand? 1.0 * INTESITY : 0;
        double scale = (double)ITERATION * 0.01;

        // circulate humidity
        double inflow_humidity = humidity - wind.x * scale;
        double outflow_humidity =humidity + wind.x * scale;

        double inflow = Math.max(inflow_humidity - humidity, 0.0);
        double outflow = Math.max(humidity - outflow_humidity, 0.0);
        humidity += inflow * intensity * inverseOrographicEffect;
        humidity -= outflow * intensity;
        return humidity;
    }

    public double getEvapotranspiration(int posY, boolean isLand){
        double evapotranspiration;
        if(isLand){
            evapotranspiration = TRANSPIRATION;
        }else{
            evapotranspiration = EVAPORATION;
        }

        evapotranspiration *= getMoisture(posY);
        return evapotranspiration;
    }

    private double getOrographicEffect(double elevation, Vector wind, boolean isLand){
        double slope = isLand? (1.0 - (elevation * 1.0)) : 0.0;
        double uphill = Math.max(Math.max(wind.x * -elevation,  wind.y * -elevation), 0.0);
        return uphill * slope * ORTOGRAPHICEFFECT;
    }

    private double getBasePrecipitation(int posY) {
        double verticality = Utils.toUnsignedRange(equator.getDistance(posY));
        return Utils.toUnsignedRange(-Math.cos(verticality * 3 * Math.PI*2));
    }
}