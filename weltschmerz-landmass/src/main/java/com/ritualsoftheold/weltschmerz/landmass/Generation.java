package com.ritualsoftheold.weltschmerz.landmass;

import com.ritualsoftheold.weltschmerz.landmass.land.Location;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Generation {

    static final int SHAPES = 4;

    private static Legend getPrevious(Legend legend){
        switch (legend){
            case SHORELINE:
                return Legend.SEA;
            case PLAIN:
                return Legend.SHORELINE;
            case HILL:
                return  Legend.PLAIN;
            case MOUNTAIN:
                return Legend.HILL;
                default:
                    return null;
        }
    }

    public static Legend getNext(Legend legend){
        switch (legend){
            case SEA:
                return Legend.SHORELINE;
            case SHORELINE:
                return Legend.PLAIN;
            case PLAIN:
                return Legend.HILL;
            case HILL:
                return  Legend.MOUNTAIN;
            case MOUNTAIN:
                return Legend.MOUNTAIN;
            default:
                return null;
        }
    }

    public static int getNumShapes(Legend legend){
        int num;
        for (num = 1; legend != Legend.SEA; num++){
            legend = getPrevious(Objects.requireNonNull(legend));
        }
        return num;
    }

    static Legend getShape(int index){
        switch (index){
            case 1:
                return Legend.SHORELINE;
            case 2:
                return Legend.PLAIN;
            case 3:
                return Legend.HILL;
            case 4:
                return Legend.MOUNTAIN;
         default:
             return Legend.SEA;
        }
    }

    public static Legend landGeneration(ArrayList<Double> elevation){
        int ocean = 0;
        int land = 0;

        for(double e:elevation) {
            if (e < 0.5) {
                ocean++;
            }else{
                land++;
            }
        }
        if (ocean > land) {
            return Legend.OCEAN;
        }else{
            return Legend.PLAIN;
        }
    }

    public static boolean isFreeTectonicPlate(int range, ArrayList<Location> world){
        int count = 0;
        for(Location location:world){
            if(location.getTectonicPlate() == null){
                count++;
            }
        }

        return count > range;
    }
}
