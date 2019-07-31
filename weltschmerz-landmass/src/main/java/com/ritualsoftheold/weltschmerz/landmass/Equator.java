package com.ritualsoftheold.weltschmerz.landmass;

import com.ritualsoftheold.weltschmerz.geometry.misc.Configuration;

public class Equator {
    private int equatorPosition;
    private float tempDifference;
    private int minTemperature;
    private int latitude;

    public Equator(Configuration conf){
        equatorPosition = conf.latitude/2;
        tempDifference = (Math.abs(conf.maxTemperature) + Math.abs(conf.minTemperature))/(float)equatorPosition;
        this.minTemperature = conf.minTemperature;
        this.latitude = conf.latitude;
    }

    public float getTemperature(int posY){
        if(posY <= equatorPosition){
            return (tempDifference * posY) + minTemperature;
        }else{
            return ((latitude-posY) * tempDifference)+minTemperature;
        }
    }
}
