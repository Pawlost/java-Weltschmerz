package com.ritualsoftheold.weltschmerz.environment;

import com.ritualsoftheold.weltschmerz.geometry.misc.Utils;
import com.ritualsoftheold.weltschmerz.geometry.units.Vector;

public class Circulation {

    private Equator equator;

    public static final double TEMPERATURE_INFLUENCE = 0.5;
    public static final int OCTAVES = 7;
    public static double EXCHANGE_COEFIXIENT = 1.5;

    public Circulation(Equator equator) {
        this.equator = equator;
    }

    public Vector getAirFlow(int posX, int posY) {
        Vector airExchange = calculateAirExchange(posX, posY, 7);
        double x = 0;
        double y = 0;

        airExchange = new Vector(airExchange.x * EXCHANGE_COEFIXIENT, airExchange.y * EXCHANGE_COEFIXIENT,airExchange.z * EXCHANGE_COEFIXIENT,airExchange.w * EXCHANGE_COEFIXIENT);
        airExchange = Utils.clamp(airExchange, -1.0, 1.0);

        x += airExchange.x;
        y += airExchange.y;

        x += (1 / Math.sqrt(2)) * airExchange.z;
        y += (1 / Math.sqrt(2)) * airExchange.z;

        x += (1 / Math.sqrt(2)) * airExchange.w;
        y += -(1 / Math.sqrt(2)) * airExchange.w;

        return applyCoriolisEffect(posY, new Vector(x, y));
    }

    private Vector calculateAirExchange(int posX, int posY, double decline) {
        double x = 0;
        double y = 0;
        double z = 0;
        double w = 0;
        float range = 0.0f;
        float intensity = 1.0f;

        for (int octave = 0; octave < OCTAVES; octave++) {
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
        double x = calculateDensity(posX - distance, posY) - calculateDensity(posX + distance, posY);
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
        return (density * (1.0 - TEMPERATURE_INFLUENCE)) + ((1.0 - temperature) * TEMPERATURE_INFLUENCE);
    }

    private double calculateBaseDensity(int posY){
        double verticallity = Utils.toUnsignedRange(equator.getDistance(posY));
        return Utils.toUnsignedRange(Math.cos(Math.toRadians(verticallity * 3 * (Math.PI*2))));
    }

    public Vector applyCoriolisEffect(int posY, Vector airFlow){
        float latitude = (float) posY / equator.conf.latitude;
        float equatorPosition = equator.equatorPosition;
        float direction = Math.signum(latitude - equatorPosition);
        Vector matrix = Utils.rotation((Math.PI/2) * direction * airFlow.getLength());
        double x = (matrix.x * airFlow.x) + (matrix.z * airFlow.x);
        double y = (matrix.y * airFlow.y) + (matrix.w * airFlow.y);
        return new Vector(x, y);
    }
}
