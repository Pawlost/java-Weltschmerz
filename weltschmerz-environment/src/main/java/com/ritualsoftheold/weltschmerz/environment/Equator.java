package com.ritualsoftheold.weltschmerz.environment;

import com.typesafe.config.Config;

public class Equator {
    private int maxTemperature;
    private int minTemperature;
    private double temperatureDecrease;
    private double equatorPosition;

    public Equator(Config conf){
        changeConfiguration(conf);
    }

    public double getTemperature(int posY, double elevation){
        double tempDifference = (Math.abs(minTemperature) + Math.abs(maxTemperature)) / equatorPosition;
        double basicTemperature = (getDistance(posY) * -tempDifference) + maxTemperature;

        double decrease = elevation * temperatureDecrease;
        if (elevation > 0) {
            return basicTemperature - decrease;
        } else {
            return basicTemperature;
        }
    }

    public double getDistance(int posY){
        return Math.abs(equatorPosition - posY);
    }

    public double getEquatorPosition() {
        return equatorPosition;
    }

    public void changeConfiguration(Config config){
        double latitude = config.getInt("map.latitude");
        maxTemperature = config.getInt("temperature.max_temperature");
        minTemperature = config.getInt("temperature.min_temperature");
        temperatureDecrease = config.getDouble("temperature.temperature_decrease");
        equatorPosition = (latitude /2.0);
    }
}
