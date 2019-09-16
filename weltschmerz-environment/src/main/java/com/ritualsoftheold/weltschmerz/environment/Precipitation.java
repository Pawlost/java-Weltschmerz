package com.ritualsoftheold.weltschmerz.environment;

import com.ritualsoftheold.weltschmerz.misc.utils.Constants;
import com.ritualsoftheold.weltschmerz.misc.utils.Utils;
import com.ritualsoftheold.weltschmerz.misc.units.Vector;
import com.typesafe.config.Config;

public class Precipitation {

    private int longitude;
    private int latitude;
    private double zoom;
    private double change;
    private double moistureIntensity;
    private double circulationIntensity;
    private double orographicEffect;
    private double precipitationIntensity;
    private double iteration;
    private double transpiration;
    private double evaporation;
    private Equator equator;
    private WorldNoise worldNoise;
    private double intensity;
    private int elevationDelta;

    public Precipitation(Equator equator, WorldNoise worldNoise, Config config) {
        this.equator = equator;
        this.worldNoise = worldNoise;
        changeConfiguration(config);
    }

    private Vector getElevationGradient(int posX, int posY) {
        double x = Math.min(Math.min(posX + elevationDelta, longitude - 1), 0);
        double y = Math.min(Math.min(posY + elevationDelta, latitude - 1), 0);

        x = worldNoise.getNoise(Math.min((int) x + elevationDelta, longitude - 1), posY)
                - worldNoise.getNoise(Math.max((int) x - elevationDelta, 0), posY);
        y = worldNoise.getNoise(posX, Math.min((int) y + elevationDelta, latitude - 1))
                - worldNoise.getNoise(posX, Math.max((int) y - elevationDelta, 0));

        return Utils.normalize(new Vector(x, 0.01 * elevationDelta, y));
    }

    public double getMoisture(int posY) {
        double verticality = (Utils.toUnsignedRange(equator.getDistance(posY)) / zoom);
        double mix = Utils.mix(-Math.cos(Math.toRadians(verticality * 3 * Math.PI * 2)),
                -Math.cos(Math.toRadians(verticality * Math.PI * 2)), change);
        return Utils.toUnsignedRange(Math.abs(mix) * moistureIntensity);
    }

    public double getPrecipitation(int posX, int posY, double elevation, double temperature, Vector wind) {
        double humidity = getHumidity(posX, posY, wind, elevation);
        double estimated = (1.0 - circulationIntensity) * getBasePrecipitation(posY);
        double elevationGradient = getElevationGradient(posX, posY).y;
        double simulated = (2.0 * circulationIntensity) * (temperature + 10 + getOrotographicEffect(elevation, elevationGradient, wind,
                orographicEffect)) * humidity;
        return Math.max(Math.min(intensity * (estimated + simulated), (int) (Math.abs(temperature + 10)
                * Constants.MAXIMUM_PRECIPITATION) / 50f), 0);
    }

    private double getHumidity(int posX, int posY, Vector wind, double elevation) {
        boolean isLand = Utils.isLand(elevation);
        double humidity = getEvapotranspiration(posY, isLand);
        double elevationGradient = getElevationGradient(posX, posY).y;

        double finalOrographicEffect = getOrotographicEffect(elevation, elevationGradient, wind, orographicEffect);
        double inverseOrographicEffect = 1.0 - finalOrographicEffect;

        intensity = isLand ? 1.0 * precipitationIntensity : 0;
        double scale = iteration * 0.01;

        // circulate humidity
        int x = Math.max(Math.min((int) (posX - (Utils.normalize(wind).x * wind.getLength() * scale)), longitude - 1), 0);
        int y = Math.max(Math.min((int) (posY - (Utils.normalize(wind).y * wind.getLength() * scale)), latitude - 1), 0);

        double inflowHumidity = getEvapotranspiration(y, Utils.isLand(worldNoise.getNoise(x, y)));

        x = Math.max(Math.min((int) (posX + (Utils.normalize(wind).x * wind.getLength() * scale)), longitude - 1), 0);
        y = Math.max(Math.min((int) (posY + (Utils.normalize(wind).y * wind.getLength() * scale)), latitude - 1), 0);

        double outflowHumidity = getEvapotranspiration(y, Utils.isLand(worldNoise.getNoise(x, y)));

        double inflow = Math.max(inflowHumidity - humidity, 0.0);
        double outflow = Math.max(humidity - outflowHumidity, 0.0);
        humidity += inflow * intensity * inverseOrographicEffect;
        humidity -= outflow * intensity;

        return humidity;
    }

    private double getEvapotranspiration(int posY, boolean isLand) {
        double evapotranspiration;
        if (isLand) {
            evapotranspiration = transpiration;
        } else {
            evapotranspiration = evaporation;
        }

        evapotranspiration *= getMoisture(posY);
        return evapotranspiration;
    }

    private static double getOrotographicEffect(double elevation, double elevationGradient, Vector wind, double ortographicEffect) {
        wind = Utils.normalize(wind);
        double slope = Utils.isLand(elevation) ? 1.0 - elevationGradient : 0.0;
        double uphill = Math.max(Math.max(wind.x * -elevation, wind.y * -elevation), 0.0);
        return uphill * slope * ortographicEffect;
    }

    private double getBasePrecipitation(int posY) {
        double verticality = Utils.toUnsignedRange(equator.getDistance(posY));
        return Utils.toUnsignedRange(-Math.cos(Math.toRadians(verticality * 3 * Math.PI * 2)));
    }

    public void changeConfiguration(Config config) {
        longitude = config.getInt("map.longitude");
        latitude = config.getInt("map.latitude");

        //Moisture
        zoom = config.getDouble("moisture.zoom");
        change = config.getDouble("moisture.change");
        moistureIntensity = config.getDouble("moisture.moisture_intensity");

        //Precipitation
        elevationDelta = config.getInt("precipitation.elevation_delta");
        circulationIntensity = config.getDouble("precipitation.circulation_intensity");
        orographicEffect = config.getDouble("precipitation.orographic_effect");
        precipitationIntensity = config.getDouble("precipitation.precipitation_intensity");
        iteration = config.getDouble("precipitation.iteration");

        //Humidity
        transpiration = config.getDouble("humidity.transpiration");
        evaporation = config.getDouble("humidity.evaporation");
    }
}