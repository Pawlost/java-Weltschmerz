package com.ritualsoftheold.weltschmerz.environment;

import com.ritualsoftheold.weltschmerz.misc.misc.Configuration;

public class Equator {
    final int equatorPosition;
    Configuration conf;
    private WorldNoise noise;

    public Equator(WorldNoise noise, Configuration conf){
        equatorPosition = (conf.latitude/2);
        this.conf = conf;
        this.noise = noise;
    }

    public double getTemperature(int posX, int posY){
        double tempDifference = 2 * (Math.abs(conf.minTemperature) + Math.abs(conf.maxTemperature)) / conf.latitude;
        double basicTemperature = (getDistance(posY) * -tempDifference) + conf.maxTemperature;

        double elevation = noise.getNoise(posX, posY) * conf.temperatureDecrease;
        if (elevation > 0) {
            return basicTemperature - elevation;
        } else {
            return basicTemperature;
        }
    }

    public double getDistance(int posY){
        return Math.abs(equatorPosition - posY);
    }

    public void changeConfiguration(Configuration configuration){
        this.conf = configuration;
    }
}
