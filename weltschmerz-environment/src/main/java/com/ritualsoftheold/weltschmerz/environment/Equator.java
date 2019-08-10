package com.ritualsoftheold.weltschmerz.environment;

import com.typesafe.config.Config;

public class Equator {
    private int latitude;
    private int maxTemperature;
    private int minTemperature;
    private double temperatureDecrease;
    private int equatorPosition;
    private WorldNoise noise;

    public Equator(WorldNoise noise, Config conf){
        changeConfiguration(conf);
        this.noise = noise;
    }

    public double getTemperature(int posX, int posY){
        double tempDifference = (Math.abs(minTemperature) + Math.abs(maxTemperature)) / equatorPosition;
        double basicTemperature = (getDistance(posY) * -tempDifference) + maxTemperature;

        double elevation = noise.getNoise(posX, posY) * temperatureDecrease;
        if (elevation > 0) {
            return basicTemperature - elevation;
        } else {
            return basicTemperature;
        }
    }

    public double getDistance(int posY){
        return Math.abs(equatorPosition - posY);
    }

    public int getEquatorPosition() {
        return equatorPosition;
    }

    public void changeConfiguration(Config config){
        latitude = config.getInt("map.latitude");
        maxTemperature = config.getInt("temperature.max_temperature");
        minTemperature = config.getInt("temperature.min_temperature");
        temperatureDecrease = config.getInt("temperature.temperature_decrease");
        equatorPosition = (latitude/2);
    }
}
