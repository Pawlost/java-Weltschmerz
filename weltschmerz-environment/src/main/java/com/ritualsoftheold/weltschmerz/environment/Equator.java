package com.ritualsoftheold.weltschmerz.environment;

import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;
import com.ritualsoftheold.weltschmerz.geometry.misc.Utils;
import jdk.jshell.execution.Util;

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

    public double getDistance(int posY){
        return Utils.toUnsignedRange(Math.abs(equatorPosition - posY));
    }
}
