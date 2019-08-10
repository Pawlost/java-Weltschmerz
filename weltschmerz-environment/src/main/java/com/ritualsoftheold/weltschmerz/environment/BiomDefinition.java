package com.ritualsoftheold.weltschmerz.environment;

import com.typesafe.config.Config;
import org.apache.commons.collections4.map.MultiKeyMap;
import squidpony.SquidTags;

public class BiomDefinition {

    private static final double OCEAN_DOUBLE = -50.0;
    static final int MAXIMUM_PRECIPITATION = 400;
    static final  int MAXIMUM_TEMPERATURE_DIFFERENCE = 100;
    public final String key;
    public final Integer color;
    private MultiKeyMap<Integer, Integer> shape;

    public BiomDefinition(String key, int color){
        shape = new MultiKeyMap<>();
        this.color = color;
        this.key = key;
    }

    public void addPoint(int posX, int posY) {
        shape.put(posX, posY, color);
    }

    public boolean define(double precipitation, double temperature){
       int y = (int)(precipitation * (1000/MAXIMUM_PRECIPITATION));
        int x;
       if(temperature > 0) {
            x = (int)((temperature * 10) + MAXIMUM_TEMPERATURE_DIFFERENCE);
       }else if(temperature > -10){
            x = (int)(MAXIMUM_TEMPERATURE_DIFFERENCE/Math.abs(temperature));
       }else {
           return false;
       }

       return shape.get(x, y) != null;
    }

    public static BiomDefinition selectDefault(double temperature,double elevation){
        if(elevation <= 0){
            if(elevation < OCEAN_DOUBLE){
                return new BiomDefinition("OCEAN", Integer.parseInt("000066", 16));
            }else {
                return new BiomDefinition("SEA", Integer.parseInt("0099FF", 16));
            }
        }else {
            if (temperature <= 0) {
                return new BiomDefinition("ICELAND", Integer.parseInt("FFFFFF", 16));
            } else {
                return new BiomDefinition("DESERT", Integer.parseInt("FFCC00", 16));
            }
        }
    }

    //For future
    public void changeConfiguration(Config config){

    }
}
