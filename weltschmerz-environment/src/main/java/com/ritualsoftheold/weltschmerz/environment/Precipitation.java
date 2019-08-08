package com.ritualsoftheold.weltschmerz.environment;

import com.ritualsoftheold.weltschmerz.geometry.misc.Utils;
import com.ritualsoftheold.weltschmerz.geometry.units.Vector;

public class Precipitation {

    private Equator equator;
    private Circulation circulation;
    private WorldNoise worldNoise;
    private double intensity;

    public Precipitation(Equator equator, Circulation circulation, WorldNoise worldNoise){
        this.equator = equator;
        this.circulation = circulation;
        this.worldNoise =worldNoise;
    }

    private Vector getElevationGradient(int posX, int posY){
        double x = Math.min(Math.min(posX + equator.conf.elevationDelta, equator.conf.longitude), 0);
        double y = Math.min(Math.min(posY + equator.conf.elevationDelta, equator.conf.latitude), 0);

         x = worldNoise.getNoise(Math.max((int)x + equator.conf.elevationDelta, equator.conf.longitude), posY)
                 - worldNoise.getNoise(Math.max((int)x - equator.conf.elevationDelta,0), posY);
         y = worldNoise.getNoise(posX, Math.max((int)y + equator.conf.elevationDelta, equator.conf.latitude))
                 - worldNoise.getNoise(posX, Math.max((int) y - equator.conf.elevationDelta, 0));

        return Utils.normalize(new Vector(x, 0.01 * equator.conf.elevationDelta, y));
    }

    public double getMoisture(int posY){
        double verticality = (Utils.toUnsignedRange(equator.getDistance(posY))/equator.conf.zoom)+equator.conf.placement;
        double mix = Utils.mix(-Math.cos(Math.toRadians(verticality*3*Math.PI*2)),
                -Math.cos(Math.toRadians(verticality*Math.PI*2)), equator.conf.change);
        return Utils.toUnsignedRange(Math.abs(mix) * equator.conf.moistureIntensity);
    }

    public double getPrecipitation(int posX, int posY){
        double elevation = worldNoise.getNoise(posX, posY);
        Vector wind = circulation.getAirFlow(posX, posY);
        double humidity = getHumidity(posX, posY);
        double temperature = equator.getTemperature(posX, posY);
        double estimated = (1.0 - equator.conf.circulation) * getBasePrecipitation(posY);
        double elevationGradient = getElevationGradient(posX, posY).y;
        double simulated =  (2.0 * equator.conf.circulation) *  (temperature + 10 + getOrotographicEffect(elevation, elevationGradient, wind,
                equator.conf.orographicEffect)) * humidity;
       return Math.max(Math.min(intensity * (estimated + simulated), (int)(Math.abs(temperature + 10)
               * BiomDefinition.MAXIMUM_PRECIPITATION) / BiomDefinition.MAXIMUM_TEMPERATURE_DIFFERENCE), 0);
    }

    public double getHumidity(int posX, int posY){
        double elevation = worldNoise.getNoise(posX, posY);
        boolean isLand = Utils.isLand(elevation);
        double humidity = getEvapotranspiration(posX, posY);
        Vector wind = circulation.getAirFlow(posX, posY);
        double elevationGradient = getElevationGradient(posX, posY).y;

        double orographicEffect = getOrotographicEffect(elevation, elevationGradient, wind, equator.conf.orographicEffect);
        double inverseOrographicEffect = 1.0 - orographicEffect;

        intensity = isLand ? 1.0 * equator.conf.precipitationIntensity : 0;
        double scale = equator.conf.iteration * 0.01;

        // circulate humidity
        double inflowHumidity = getEvapotranspiration((int)(posX - (Utils.normalize(wind).x * wind.getLength() * scale)),
                (int)(posY - (Utils.normalize(wind).y * wind.getLength() * scale)));
        double outflowHumidity = getEvapotranspiration((int)(posX + (Utils.normalize(wind).x * wind.getLength() * scale)),
                (int) (posY + (Utils.normalize(wind).y * wind.getLength() * scale)));

        double inflow = Math.max(inflowHumidity - humidity, 0.0);
        double outflow = Math.max(humidity - outflowHumidity, 0.0);
        humidity += inflow * intensity * inverseOrographicEffect;
        humidity -= outflow * intensity;
        //TODO Intensity
        return humidity;
    }

    public double getEvapotranspiration(int posX, int posY){
        double elevation = worldNoise.getNoise(posX, posY);
        double evapotranspiration;
        if(Utils.isLand(elevation)){
            evapotranspiration = equator.conf.traspiration;
        }else{
            evapotranspiration = equator.conf.evaporation;
        }

        evapotranspiration *= getMoisture(posY);
        return evapotranspiration;
    }

    private static double getOrotographicEffect(double elevation, double elevationGradient, Vector wind, double ortographicEffect) {
            wind = Utils.normalize(wind);
            double slope = Utils.isLand(elevation)? 1.0 - elevationGradient:0.0;
            double uphill = Math.max(Math.max(wind.x * -elevation, wind.y * -elevation), 0.0);
            return uphill * slope * ortographicEffect;
    }

    private double getBasePrecipitation(int posY) {
        double verticality = Utils.toUnsignedRange(equator.getDistance(posY));
        return Utils.toUnsignedRange(-Math.cos(Math.toRadians(verticality * 3 * Math.PI*2)));
    }
}