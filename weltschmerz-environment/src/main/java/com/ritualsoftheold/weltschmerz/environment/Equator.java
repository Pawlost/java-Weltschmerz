package com.ritualsoftheold.weltschmerz.environment;

import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;
import com.ritualsoftheold.weltschmerz.geometry.misc.Utils;
import jdk.jshell.execution.Util;

public class Equator {
    final int equatorPosition;
    private double tempDifference;
    final Configuration conf;
    private WorldNoise noise;
    private double lapseRate;

    public Equator(WorldNoise noise, Configuration conf){
        equatorPosition = conf.latitude/2;
        tempDifference = (Math.abs(conf.minTemperature) + Math.abs(conf.maxTemperature))/equatorPosition;
        this.conf = conf;
        this.noise = noise;
    }

    public double getTemperature(int posX, int posY){
        double basicTemperature;
        if(posY <= equatorPosition){
            basicTemperature = (tempDifference * posY) + conf.minTemperature;
        }else{
            basicTemperature = ((conf.latitude-posY) * tempDifference)+conf.minTemperature;
        }

        double elevation = noise.getNoise(posX, posY) * conf.temperatureDecrease;
        if (elevation > 0) {
            return basicTemperature - elevation;
        } else {
            return basicTemperature;
        }
    }

    public double getDistance(int posY){
        return Math.abs((conf.longitude/2) - posY);
    }
}
