package com.ritualsoftheold.weltschmerz.environment;

import com.ritualsoftheold.weltschmerz.misc.utils.Utils;
import com.ritualsoftheold.weltschmerz.misc.units.Vector;
import com.typesafe.config.Config;

public class Circulation {

    private WorldNoise noise;
    private Equator equator;
    private int latitude;
    private int longitude;
    private double maxElevation;
    private double circulationDecline;
    private double temperatureInfluence;
    private double exchangeCoefficient;
    private int octaves;

    public Circulation(Equator equator, WorldNoise noise, Config config) {
        this.equator = equator;
        this.noise = noise;
        changeConfiguration(config);
    }

    public Vector getAirFlow(int posX, int posY) {
        Vector airExchange = calculateAirExchange(posX, posY);
        double x = 0;
        double y = 0;

        airExchange = new Vector(airExchange.x * exchangeCoefficient,
                airExchange.y * exchangeCoefficient,
                airExchange.z * exchangeCoefficient,
                airExchange.w * exchangeCoefficient);
        airExchange = Utils.clamp(airExchange, -1.0, 1.0);

        x += airExchange.x;
        y += airExchange.y;

        x += (1 / Math.sqrt(2)) * airExchange.z;
        y += (1 / Math.sqrt(2)) * airExchange.z;

        x += (1 / Math.sqrt(2)) * airExchange.w;
        y += -(1 / Math.sqrt(2)) * airExchange.w;

        return applyCoriolisEffect(posY, new Vector(x, y));
    }

    private Vector calculateAirExchange(int posX, int posY) {
        double x = 0;
        double y = 0;
        double z = 0;
        double w = 0;
        float range = 0.0f;
        float intensity = 1.0f;

        for (int octave = 0; octave < octaves; octave++) {
            Vector delta = calculateDensityDelta(posX, posY, (int) (Math.pow(octave, 2)));
            x += delta.x * intensity;
            y += delta.y * intensity;
            z += delta.z * intensity;
            w += delta.w * intensity;

            range += intensity;
            intensity *= circulationDecline;
        }

        return new Vector(x / range, y / range, z / range, w / range);
    }

    private Vector calculateDensityDelta(int posX, int posY, int distance) {

        int posXPositive = Math.min(posX + distance, longitude - 1);
        int posXNegative = Math.max(posX - distance, 0);
        int posYPositive = Math.min(posY + distance, latitude - 1);
        int posYNegative = Math.max(posY - distance, 0);

        double xNegativeDensity = calculateDensity(posXNegative, posY);
        double xPositiveDensity;
        double yNegativeDensity;
        double yPositiveDensity;
        double zPositiveDensity;
        double zNegativeDensity;
        double wPositiveDensity;
        double wNegativeDensity;

        // west - east
        if(posXNegative == posXPositive){
            xPositiveDensity = xNegativeDensity;
        }else{
            xPositiveDensity = calculateDensity(posXPositive, posY);
        }

        double x = xNegativeDensity - xPositiveDensity;

        // north - south
        if (posYNegative == posY && posX == xNegativeDensity) {
            yNegativeDensity = xNegativeDensity;
        }else if(posYNegative == posY && posX == xPositiveDensity){
            yNegativeDensity = xPositiveDensity;
        }else{
            yNegativeDensity = calculateDensity(posX, posYNegative);
        }

        if (posYPositive == posY && posX == xNegativeDensity) {
            yPositiveDensity = xNegativeDensity;
        }else if(posYPositive == posY && posX == xPositiveDensity){
            yPositiveDensity = xPositiveDensity;
        }else {
            yPositiveDensity = calculateDensity(posX, posYPositive);
        }

        double y = yNegativeDensity - yPositiveDensity;

        // south-west - north-east
        if(posX == posXNegative){
            zNegativeDensity = yNegativeDensity;
        }else if(posY == posYNegative){
            zNegativeDensity = xNegativeDensity;
        }else{
            zNegativeDensity = calculateDensity(posXNegative, posYNegative);
        }

        if(posX == posXPositive){
            zPositiveDensity = yPositiveDensity;
        }else if(posY == posYPositive){
            zPositiveDensity = xPositiveDensity;
        }else{
            zPositiveDensity = calculateDensity(posXPositive, posYPositive);
        }

        double z = zNegativeDensity - zPositiveDensity;

        // north-west - south-east
        if(posX == posXNegative){
            wNegativeDensity = yPositiveDensity;
        }else if(posY == posYPositive){
            wNegativeDensity = xNegativeDensity;
        }else{
            wNegativeDensity = calculateDensity(posXNegative, posYPositive);
        }

        if(posY == posYNegative){
            wPositiveDensity = xPositiveDensity;
        }else if(posX == posXPositive){
            wPositiveDensity = yNegativeDensity;
        }else{
            wPositiveDensity = calculateDensity(posXPositive, posYNegative);
        }

        double w = wNegativeDensity - wPositiveDensity;

        return new Vector(x, y, z, w);
    }

    public double calculateDensity(int posX, int posY) {
        double density = calculateBaseDensity(posY);
        double elevation = noise.getNoise(posX, posY)/maxElevation;
        double temperature = equator.getTemperature(posY, elevation);
        return (density * (1.0 - temperatureInfluence)) + ((1.0 - temperature) * temperatureInfluence);
    }

    private double calculateBaseDensity(int posY) {
        double verticallity = Utils.toUnsignedRange(equator.getDistance(posY));
        return Utils.toUnsignedRange(Math.cos(Math.toRadians(verticallity * 3 * (Math.PI * 2))));
    }

    private Vector applyCoriolisEffect(int posY, Vector airFlow) {
        float coriolisLatitude = (float) posY / latitude;
        double equatorPosition = equator.getEquatorPosition();
        double direction = Math.signum(coriolisLatitude - equatorPosition);
        Vector matrix = Utils.rotation((Math.PI / 2) * direction * airFlow.getLength());
        double x = (matrix.x * airFlow.x) + (matrix.z * airFlow.x);
        double y = (matrix.y * airFlow.y) + (matrix.w * airFlow.y);
        return new Vector(x, y);
    }

    public void changeConfiguration(Config config) {
        latitude = config.getInt("map.latitude");
        longitude = config.getInt("map.longitude");
        circulationDecline = config.getDouble("circulation.circulation_decline");
        temperatureInfluence = config.getDouble("circulation.temperature_influence");
        exchangeCoefficient = config.getDouble("circulation.exchange_coefficient");
        octaves = config.getInt("circulation.circulation_octaves");
        maxElevation = config.getInt("map.max_elevation");
    }
}
