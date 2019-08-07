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
    private double intensity;


    public Precipitation(Equator equator, Circulation circulation, WorldNoise worldNoise){
        this.equator = equator;
        this.circulation = circulation;
        this.worldNoise =worldNoise;
    }

    public Vector getElevationGradient(int posX, int posY, int delta){
        double x = Math.min(Math.max(posX + delta, equator.conf.longitude), 0);
        double y = Math.min(Math.max(posY + delta, equator.conf.latitude), 0);
         x = worldNoise.getNoise((int)x + delta, posY) - worldNoise.getNoise((int)x - delta, posY);
         y = worldNoise.getNoise(posX, (int)y + delta) - worldNoise.getNoise(posX, (int) y - delta);
        return Utils.normalize(new Vector(x, 0.01 * delta, y));
    }

    public double getMoisture(int posY){
        double verticality = equator.getDistance(posY);
        double mix = Utils.mix(-Math.cos(Math.toRadians(verticality*3*Math.PI*2)), -Math.cos(Math.toRadians(verticality*Math.PI*2)), 0.33);
        return Utils.toUnsignedRange(Math.abs(mix));
    }

    public double getPrecipitation(int posX, int posY){
        double elevation = worldNoise.getNoise(posX, posY);
        Vector wind = circulation.getAirFlow(posX, posY);
        double humidity = getHumidity(posX, posY);
        double temperature = equator.getTemperature(posX, posY);
        double estimated = (1.0 - CIRCULATION) * getBasePrecipitation(posY);
        double elevationGradient = getElevationGradient(posX, posY, 5).y;
        double simulated =  (2.0 * CIRCULATION) *  (temperature + 10 + getOrotographicEffect(elevation, elevationGradient, wind)) * humidity;
        return Math.max(Math.min ( intensity * (estimated + simulated), (int)(Math.abs(temperature + 10) * 400) / 50), 0);
    }

    public double getHumidity(int posX, int posY){
        double elevation = worldNoise.getNoise(posX, posY);
        boolean isLand = Utils.isLand(elevation);
        double humidity = getEvapotranspiration(posX, posY);
        Vector wind = circulation.getAirFlow(posX, posY);
        double elevationGradient = getElevationGradient(posX, posY, 5).y;

        double orographicEffect = getOrotographicEffect(elevation, elevationGradient, wind);
        double inverseOrographicEffect = 1.0 - orographicEffect;

        intensity = isLand ? 1.0 * INTESITY : 0;
        double scale = (double)ITERATION * 0.01;

        // circulate humidity
        double inflowHumidity = getEvapotranspiration((int)(posX - (Utils.normalize(wind).x * wind.getLength() * scale)),
                (int)(posY - (Utils.normalize(wind).y * wind.getLength() * scale)));
        double outflowHumidity = getEvapotranspiration((int)(posX + (Utils.normalize(wind).x * wind.getLength() * scale)),
                (int) (posY + (Utils.normalize(wind).y * wind.getLength() * scale)));

        double inflow = Math.max(inflowHumidity - humidity, 0.0);
        double outflow = Math.max(humidity - outflowHumidity, 0.0);
        humidity += inflow * intensity * inverseOrographicEffect;
        humidity -= outflow * intensity;
        return humidity;
    }

    public double getEvapotranspiration(int posX, int posY){
        double elevation = worldNoise.getNoise(posX, posY);
        double evapotranspiration;
        if(Utils.isLand(elevation)){
            evapotranspiration = TRANSPIRATION;
        }else{
            evapotranspiration = EVAPORATION;
        }

        evapotranspiration *= getMoisture(posY);
        return evapotranspiration;
    }

    public static double getOrotographicEffect(double elevation, double elevationGradient, Vector wind) {
            wind = Utils.normalize(wind);
            double slope = Utils.isLand(elevation)? 1.0 - elevationGradient:0.0;
            double uphill = Math.max(Math.max(wind.x * -elevation, wind.y * -elevation), 0.0);
            return uphill * (0.95) * ORTOGRAPHICEFFECT;
    }

    private double getBasePrecipitation(int posY) {
        double verticality = Utils.toUnsignedRange(equator.getDistance(posY));
        return Utils.toUnsignedRange(-Math.cos(Math.toRadians(verticality * 3 * Math.PI*2)));
    }
}