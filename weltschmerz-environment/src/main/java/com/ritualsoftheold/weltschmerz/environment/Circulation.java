package com.ritualsoftheold.weltschmerz.environment;

import com.ritualsoftheold.weltschmerz.geometry.misc.Utils;
import com.ritualsoftheold.weltschmerz.geometry.units.Vector;

public class Circulation {
    private int octaves = 7;
    private float decline = 0.5f;
    private double temperatureInfluence = 0.5;


    private Equator equator;

    public Circulation(Equator equator) {
        this.equator = equator;
    }

    public Vector getAirFlow(int posX, int posY) {
        Vector airExchange = calculateAirExchange(posX, posY);
        float exchangeCoefficient = 1.5f;
        double x = 0;
        double y = 0;

        x += airExchange.x * exchangeCoefficient;
        y += airExchange.y * exchangeCoefficient;

        x += (1 / Math.sqrt(2)) * airExchange.z * exchangeCoefficient;
        y += (1 / Math.sqrt(2)) * airExchange.z * exchangeCoefficient;

        x += (1 / Math.sqrt(2)) * airExchange.w * exchangeCoefficient;
        y += -(1 / Math.sqrt(2)) * airExchange.w * exchangeCoefficient;

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
            Vector delta = calculateDensityDelta(posX, posY, (int)(Math.pow(octave,2)));
            x += delta.x * intensity;
            y += delta.y * intensity;
            z += delta.z * intensity;
            w += delta.w * intensity;

            range += intensity;
            intensity *= decline;
        }

        return new Vector(x/range, y/range, z/range, w/range);
    }

    private Vector calculateDensityDelta(int posX, int posY, int distance){
        // west - east
        double x = calculateDensity(posX - distance, posY) - calculateDensity(posX, posY);
        // north - south
        double y = calculateDensity(posX, posY -distance) - calculateDensity(posX, posY + distance);

        // south-west - north-east
        double z = calculateDensity(posX - distance, posY - distance) - calculateDensity(posX + distance, posY + distance);

        // north-west - south-east
        double w = calculateDensity(posX - distance, posY + distance) - calculateDensity(posX + distance, posY - distance);

        return new Vector(x, y, z, w);
    }

    public double calculateDensity(int posX, int posY){
        double density = calculateBaseDensity(posY);
        double temperature = equator.getTemperature(posX, posY);
        return (density * (1.0 - temperatureInfluence)) + ((1 - temperature)*temperatureInfluence);
    }

    private double calculateBaseDensity(int posY){
        double verticallity = Utils.toUnsignedRange(equator.getDistance(posY));
        return Utils.toUnsignedRange(Math.cos(Math.toRadians(verticallity * 3 * (Math.PI*2))));
    }

    public Vector applyCoriolisEffect(int posY, Vector airFlow){
        float latitude = (float)posY / equator.conf.latitude;
        float equatorPosition = equator.equatorPosition;
        float direction = Math.signum(latitude - equatorPosition);
        Vector matrix = Utils.rotation((Math.PI/2) * direction * airFlow.getLength());
        double x = (matrix.x * airFlow.x) + (matrix.y * airFlow.y);
        double y = (matrix.z * airFlow.x) + (matrix.w * airFlow.y);
        return new Vector(x, y);
    }
}
