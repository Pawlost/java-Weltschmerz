package com.ritualsoftheold.weltschmerz.environment;

import org.apache.commons.collections4.map.MultiKeyMap;

public class BiomDefinition {

    private static final double oceanDepth = -50.0;
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
       int y = (int)Math.round(precipitation * 2.5);
       int x = (int)Math.round(temperature * 40);

       return shape.get(x, y) != null;
    }

    public static BiomDefinition selectDefault(double temperature,double elevation){
        if(temperature <= 0){
            return new BiomDefinition("ICELAND", Integer.parseInt("FFFFFF", 16));
        }else{
            if(elevation <= 0){
                if(elevation < oceanDepth){
                    return new BiomDefinition("OCEAN", Integer.parseInt("000066", 16));
                }else {
                    return new BiomDefinition("SEA", Integer.parseInt("0000F", 16));
                }
            }
            return new BiomDefinition("DESERT", Integer.parseInt("FFCC00", 16));
        }
    }
}
