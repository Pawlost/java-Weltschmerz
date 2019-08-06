package com.ritualsoftheold.weltschmerz.environment;

import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;

public class Equator {
    public final int equatorPosition;
    private float tempDifference;
    private int minTemperature;
    public final Configuration conf;
    private WorldNoise noise;

    public Equator(WorldNoise noise, Configuration conf){
        equatorPosition = conf.latitude/2;
        tempDifference = (Math.abs(conf.maxTemperature) + Math.abs(conf.minTemperature))/(float)equatorPosition;
        this.minTemperature = conf.minTemperature;
        this.conf = conf;
        this.noise = noise;
    }

    public double getTemperature(int posX, int posY){
        double basicTemperature;
        if(posY <= equatorPosition){
            basicTemperature = (tempDifference * posY) + minTemperature;
        }else{
            basicTemperature = ((conf.latitude-posY) * tempDifference)+minTemperature;
        }

        double elevation = noise.getNoise(posX, posY) * conf.temperatureDecrease;
        if (elevation > 0) {
            return basicTemperature - elevation;
        } else {
            return basicTemperature;
        }
    }

    public float getDistance(int posY){
        if(posY < equatorPosition){
            return equatorPosition - posY;
        }else if(posY > equatorPosition){
            return posY - equatorPosition;
        }
        return 0;
    }
}
