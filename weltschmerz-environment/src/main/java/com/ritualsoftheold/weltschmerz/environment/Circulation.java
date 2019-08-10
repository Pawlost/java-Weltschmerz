package com.ritualsoftheold.weltschmerz.environment;

import com.ritualsoftheold.weltschmerz.misc.misc.Utils;
import com.ritualsoftheold.weltschmerz.misc.units.Vector;
import com.typesafe.config.Config;

public class Circulation {

    private Equator equator;
    private int latitude;
    private int longitude;
    private double circulationDecline;
    private double temperatureInfluence;
    private double exchangeCoefficient;
    private int octaves;

    public Circulation(Equator equator, Config config) {
        this.equator = equator;
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
            Vector delta = calculateDensityDelta(posX, posY, (int)(Math.pow(octave,2)));
            x += delta.x * intensity;
            y += delta.y * intensity;
            z += delta.z * intensity;
            w += delta.w * intensity;

            range += intensity;
            intensity *= circulationDecline;
        }

        return new Vector(x/range, y/range, z/range, w/range);
    }

    private Vector calculateDensityDelta(int posX, int posY, int distance){
        // west - east
        double x = calculateDensity(Math.max(posX  - distance, 0), posY) -
                calculateDensity(Math.min(posX  + distance, longitude), posY);
        // north - south
        double y = calculateDensity(posX,  Math.max(posY  - distance, 0)) - calculateDensity(posX,
                Math.min(posY + distance, latitude));

        // south-west - north-east
        double z = calculateDensity(Math.max(posX  - distance, 0), Math.max(posY  - distance, 0))
                - calculateDensity(Math.min(posX  + distance, longitude),  Math.min(posY  + distance,
                latitude));

        // north-west - south-east
        double w = calculateDensity(Math.max(posX  - distance, 0),  Math.min(posY  + distance, latitude)) -
                calculateDensity(Math.min(posX  + distance, longitude),  Math.max(posY  - distance, 0));

        return new Vector(x, y, z, w);
    }

    public double calculateDensity(int posX, int posY){
        double density = calculateBaseDensity(posY);
        double temperature = equator.getTemperature(posX, posY);
        return (density * (1.0 - temperatureInfluence)) + ((1.0 - temperature) * temperatureInfluence);
    }

    private double calculateBaseDensity(int posY){
        double verticallity = Utils.toUnsignedRange(equator.getDistance(posY));
        return Utils.toUnsignedRange(Math.cos(Math.toRadians(verticallity * 3 * (Math.PI*2))));
    }

    private Vector applyCoriolisEffect(int posY, Vector airFlow){
        float coriolisLatitude = (float) posY / latitude;
        float equatorPosition = equator.getEquatorPosition();
        float direction = Math.signum(coriolisLatitude - equatorPosition);
        Vector matrix = Utils.rotation((Math.PI/2) * direction * airFlow.getLength());
        double x = (matrix.x * airFlow.x) + (matrix.z * airFlow.x);
        double y = (matrix.y * airFlow.y) + (matrix.w * airFlow.y);
        return new Vector(x, y);
    }

    public void changeConfiguration(Config config){
        latitude = config.getInt("map.latitude");
        longitude = config.getInt("map.longitude");
        circulationDecline = config.getDouble("circulation.circulation_decline");
        temperatureInfluence = config.getDouble("circulation.temperature_influence");
        exchangeCoefficient = config.getDouble("circulation.exchange_coefficient");
        octaves = config.getInt("circulation.circulation_octaves");
    }
}
